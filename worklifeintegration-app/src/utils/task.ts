import { mockComponent } from "react-dom/test-utils"
import { MomentDate } from "./moment"
var moment = require('moment'); // temporary import for example intialisation

export type Task = {
    name: string
    start: MomentDate
    end: MomentDate
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
    Saturday: [{name: "Hacking", start: moment().calendar(), end: moment().calendar().add(1, 'h')},
               {name: "Hacking2", start: moment().calendar().add(2, 'h'), end: moment().calendar().add(4, 'h')}], // placeholder example
    Sunday: [],
}