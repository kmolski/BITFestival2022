const LINK_BASE = 'http://worklifeintegration-env.eba-xbs3ibpj.eu-central-1.elasticbeanstalk.com'
const LINK_TASKS = LINK_BASE + '/tasks'

export function fetchWeek(): Promise<any> {
    return fetch(LINK_TASKS,
        {
            method: 'GET',
            headers: { 'Accept': '*/*'
        }
        }).then(result => result.json())
}
