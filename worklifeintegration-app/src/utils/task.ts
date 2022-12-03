import { mockComponent } from "react-dom/test-utils"
import moment from 'moment' // temporary import for example intialisation

export type Task = {
    id: number,
    name: string
    start: moment.Moment
    end: moment.Moment
    placeId: number,
    placementLimitId: number,
    taskPriority: string,
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
        id: item.id,
        name: item.title,
        start: moment(item.startTime),
        end: moment(item.endTime),
        placeId: item.placeId,
        placementLimitId: item.placementLimitId,
        taskPriority: item.taskPriority,
    }
}

export function getDay(task_list: any[], day: string): Task[]{
   const clean = task_list.filter((item) => {
    return moment(item.startTime).format('d') === day
})
   const mapped_items = clean.map(item => {
    return rawTaskToTask(item)}).sort((a: Task, b: Task) => {
        const a_m = parseFloat(a.start.format("H")) * 60 +  parseFloat(a.start.format("m"));
        const b_m =parseFloat(b.start.format("H")) * 60 +  parseFloat(b.start.format("m"));
        if (a_m < b_m) {
            return -1
        }
        if (a_m === b_m) {
            return 0
        }
        return 1;
    })    
   return mapped_items;
}

export function mapRawToTaskCollection(data: any): TaskCollection {
    const result = {
        Monday: getDay(data, '1'),
        Tuesday: getDay(data, '2'),
        Wednesday: getDay(data, '3'),
        Thursday: getDay(data, '4'),
        Friday: getDay(data, '5'),
        Saturday: getDay(data, '6'),
        Sunday: getDay(data, '0'),
    };
    console.log("Mapping result", result);
    return result
}

export function rawDataToTaskList(data: any): Task[] {
    return data.map((item: any) => rawTaskToTask(item))
}