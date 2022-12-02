import { createMachine, assign } from 'xstate';
import { MomentDate } from '../utils/moment';
import { TaskCollection } from '../utils/task';
var moment = require('moment');

interface MainContext {
  // todo: set context that makes sense
  task_data: TaskCollection | null ;
  week_start: MomentDate;
}

export const mainMachine = createMachine<MainContext>({
  id: "fetch",
  initial: "init",
  context: {
    task_data: null,
    week_start: moment().startOf('week'),
  },
  states: {
    init: {
      // first "dummy" state
      // can be used to re-initialise app with an "entry" action
      always: [
        {target: 'auth'}
      ]
    },
    auth: {
      // used for authentication / user selection
      // skipped for now
      always: [
        {target: 'fetch'} // use "cond" to execute transition give a condition
      ],
    },
    fetch: {
      // todo: fetching logic
      always: [
        {target: 'wait'} // use "cond" to execute transition give a condition
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
}
);

      // todo: use assign actions
      // on: {
      //   STEP: {
      //     target: "target",
      //     actions: assign({
      //       value: (context, event) => context.value + 1
      //     })
      //   },
      //   STOP: 'idle'
      // }