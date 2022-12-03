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

function rawTaskToTask(item: any): Task {
    return {
        name: item.title,
        start: moment(item.startTime),
        end: moment(item.endTime),
    }
}

export function getDay(task_list: any[], day: string): Task[]{
   const clean = task_list.filter((item) => {
    return moment(item.startTime).format('d') === day
})
   const mapped_items = clean.map(item => {
    return rawTaskToTask(item)})
   return mapped_items;
}

export function mapRawToTaskCollection(data: any): TaskCollection {
    const result = {
        Monday: getDay(data, '0'),
        Tuesday: getDay(data, '1'),
        Wednesday: getDay(data, '2'),
        Thursday: getDay(data, '3'),
        Friday: getDay(data, '4'),
        Saturday: getDay(data, '5'),
        Sunday: getDay(data, '6'),
    };
    console.log("Mapping result", result);
    return result
}

export function rawDataToTaskList(data: any): Task[] {
    return data.map((item: any) => rawTaskToTask(item))
}