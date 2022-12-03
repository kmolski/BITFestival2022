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
    Saturday: [],
    Sunday: [],
}

function arrayTimeToStr(array_time: number[]): string{
    return array_time[0].toString()+'-'+array_time[1].toString()+'-'+array_time[2].toString()+' '+array_time[3].toString()+':'+array_time[4].toString()
}

export function getDay(task_list: any[], day: string): Task[]{
   const clean = task_list.filter((item) => {
    return moment(arrayTimeToStr(item.startTime)).format('d') === day
})
   const mapped_items = clean.map(item => {
    return {
    name: item.category, // todo: set correct value
    start: moment(arrayTimeToStr(item.startTime)),
    end: moment(arrayTimeToStr(item.endTime)),
   }})
   return mapped_items;
}

export function mapRawToTaskCollection(data: any): TaskCollection {
    return {
        Monday: getDay(data, '0'),
        Tuesday: getDay(data, '1'),
        Wednesday: getDay(data, '2'),
        Thursday: getDay(data, '3'),
        Friday: getDay(data, '4'),
        Saturday: getDay(data, '5'),
        Sunday: getDay(data, '6'),
    }
}