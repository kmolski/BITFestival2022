import moment from "moment"

const LINK_BASE = 'http://worklifeintegration-env.eba-xbs3ibpj.eu-central-1.elasticbeanstalk.com'
const LINK_TASKS = LINK_BASE + '/tasks'

export function fetchWeek(week_start: moment.Moment): Promise<any> {
    const start = week_start.format("YYYY-MM-DDTHH:mm:ss");
    const end = moment(week_start.add(7, 'd')).format("YYYY-MM-DDTHH:mm:ss");

    return fetch(LINK_TASKS + '?start=' + start + '&end=' + end,
        {
            method: 'GET',
            headers: { 'Accept': '*/*'},
        }).then(result => result.json())
}
