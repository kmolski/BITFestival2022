import { mockComponent } from "react-dom/test-utils"
import moment from 'moment' // temporary import for example intialisation

export type Task = {
    name: string
    start: moment.Moment
    end: moment.Moment
}

export type TaskCollection = {
    Monday: Task[],
    Tuesday: Task[],
    Wednesday: Task[],
    Thursday: Task[],
    Friday: Task[],
    Saturday: Task[],
    Sunday: Task[],
}

export const emptyTaskCollection = {
    Monday: [],
    Tuesday: [],
    Wednesday: [],
    Thursday: [],
    Friday: [],
    Saturday: [{name: "Hacking", start: moment(moment().calendar()), end: moment(moment().calendar()).add(1, 'h')},
               {name: "Hacking2", start: moment(moment().calendar()).add(2, 'h'), end: moment(moment().calendar()).add(4, 'h')}], // placeholder example
    Sunday: [],
}