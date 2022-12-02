import { MomentDate } from "./moment"

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