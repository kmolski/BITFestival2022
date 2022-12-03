import { createMachine, assign, spawn } from 'xstate';
import { emptyTaskCollection, mapRawToTaskCollection, TaskCollection } from '../utils/task';
import moment from 'moment'
import { fetchWeek } from '../utils/endpoint';
import { log, pure } from 'xstate/lib/actions';

export interface MainContext {
  // todo: set context that makes sense
  task_data: TaskCollection | null ;
  week_start: moment.Moment;
}

const initialContext: MainContext = {
  task_data: null,
  week_start: moment().startOf('week'),
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
          return fetchWeek()
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
        DELETE: {target: 'fetch'},
        ADD: {target: 'popup'},
      },
    },
    popup: {
      // A popup is open - user is trying to add a new event or sth
      after: {
          // temporary go back after 10s
         10000: { target: 'fetch' }
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
    clearState: (context, event) => initialContext,
  }
}
);