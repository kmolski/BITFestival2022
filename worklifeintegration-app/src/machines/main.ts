import { createMachine, assign, spawn } from 'xstate';
import { emptyTaskCollection, mapRawToTaskCollection, rawDataToTaskList, Task, TaskCollection } from '../utils/task';
import moment from 'moment'
import { commitTask, deleteTask, fetchWeek, suggestTask } from '../utils/endpoint';
import { log, pure } from 'xstate/lib/actions';

export interface MainContext {
  // todo: set context that makes sense
  task_data: TaskCollection | null ;
  week_start: moment.Moment;
  suggestion_data: Task[];
  suggestion_data_dont_use: any;
}

const initialContext: MainContext = {
  task_data: null,
  week_start: moment().startOf('week'),
  suggestion_data: [],
  suggestion_data_dont_use: {},
}

export const mainMachine = createMachine<MainContext>({
  id: "mainMachine",
  initial: "init",
  context: initialContext,
  states: {
    init: {
      // first "dummy" state
      // can be used to re-initialise app with an "entry" action
      // todo: clear on init
      always: [
        {target: 'auth'}
      ],
      entry: ['clearState']
    },
    auth: {
      // used for authentication / user selection
      // skipped for now
      always: [
        {target: 'fetch'} // use "cond" to execute transition give a condition
      ],
    },
    fetch: {
      invoke: {
        id: 'getWeekData',
        src: (context, event) => {
          console.log("Invoke fetch");
          return fetchWeek(context.week_start)
        },
        onDone: {
          target: 'fetch',
          internal: true,
          actions: assign({ task_data: (context, event) => {
            console.log("Init mapping");
            return mapRawToTaskCollection(event.data.items) }})
        },
        onError: {
          target: 'init',
          actions: log("Data loading error")
        }
      },
      always: [
        {target: 'wait', cond: 'isDataLoaded'} // use "cond" to execute transition give a condition
      ],
    },
    wait: {
      // wait for user to make an action
      on: {
        CHANGE_WEEK: {target: 'fetch'},
        DELETE: {target: 'delete'},
        ADD: {target: 'suggest'},
        COMMIT: {target: 'commit'},
      },
    },
    popup: {
      // A popup is open - user is trying to add a new event or sth
      always: [{target: 'suggest'}]
    },
    suggest: {
      invoke: {
        id: 'suggestTask',
        src: (context, event) => {
          console.log("Invoke suggest");
          return suggestTask({title: event.placeholder})
        },
        onDone: {
          target: 'wait',
          actions: assign({ suggestion_data: (_context, event) => {
            console.log("Suggestion data returned");
            return rawDataToTaskList(event.data.newTasks) },
            suggestion_data_dont_use: (_context, event) => event.data
          }
          )
        },
        onError: {
          target: 'wait', // todo: handle errors
          actions: log("Data suggestion error")
        }
      },
    },
    commit: {
      invoke: {
        id: 'commitTask',
        src: (context, event) => {
          console.log("Commit task");
          return commitTask({blob: context.suggestion_data_dont_use})
        },
        onDone: {
          target: 'wait',
          actions: assign({ suggestion_data: (_context, event) => {
            console.log("Suggestion data commited");
            return [] },
            suggestion_data_dont_use: (_context, event) => undefined
          }
          )
        },
        onError: {
          target: 'wait', // todo: handle errors
          actions: log("Data suggestion error")
        }
      },
    },
    delete: {
      invoke: {
        id: 'deleteTask',
        src: (context, event) => {
          console.log("Delete task");
          return deleteTask({id: event.id})
        },
        onDone: {target: 'fetch'},
        onError: {target: 'fetch'},
      },
    }
  },
},
{
  guards: {
    isDataLoaded:  (context, event) => {
      console.log("IS DATA LOADED?", context.task_data !== null);
      return context.task_data !== null;
    },
  },
  actions: {
    clearState: (context, event) => {
      console.log("Set context to", initialContext)
      return {  task_data: null,
        week_start: moment().startOf('week'),
        suggestion_data: [],
        suggestion_data_dont_use: {},}
    },
  }
}
);