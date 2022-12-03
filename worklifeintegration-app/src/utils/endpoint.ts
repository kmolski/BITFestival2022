import moment from "moment"
import { title } from "process";

const LINK_BASE = 'http://worklifeintegration-env.eba-xbs3ibpj.eu-central-1.elasticbeanstalk.com'
const LINK_TASKS = LINK_BASE + '/tasks'

export function fetchWeek(week_start: moment.Moment): Promise<any> {
    const start = week_start.format("YYYY-MM-DDTHH:mm:ss");
    const end = moment(week_start.format()).add(7, 'd').format("YYYY-MM-DDTHH:mm:ss");

    return fetch(LINK_TASKS + '?start=' + start + '&end=' + end,
        {
            method: 'GET',
            headers: { 'Accept': '*/*'},
        }).then(result => result.json())
}

export function suggestTask(props: {title: string}): Promise<any> {
    return fetch(LINK_TASKS,
        {
            method: 'POST',
            headers: { 'Accept': '*/*', 'Content-Type': 'application/json'},
            body: JSON.stringify({
                title: props.title,
                startTime: "2022-12-02T02:00:00.000",
                endTime: "2022-12-02T04:00:00.000",
                placeId: 1,
                placementLimitId: 1,
                taskPriority: "MEDIUM"
            }),
        }).then(result => result.json())
}

export function commitTask(props: {blob: any}): Promise<any> {
    return fetch(LINK_TASKS,
        {
            method: 'POST',
            headers: { 'Accept': '*/*', 'Content-Type': 'application/json'},
            body: JSON.stringify(props.blob),
        }).then(result => result.json())
}

export function deleteTask(props: {id: number}): Promise<any> {
    return fetch(LINK_TASKS + '/tasks/' + props.id.toString(),
        {
            method: 'DELETE',
            headers: { 'Accept': '*/*', 'Content-Type': 'application/json'},
        }).then(result => result.text())
}
