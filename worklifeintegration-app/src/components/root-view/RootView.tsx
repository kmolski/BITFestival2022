import { Button, Grid, Stack } from '@mui/material';
import { State } from 'xstate';
import { sendType, stateType } from '../../machines/types';
import { TaskCollection } from '../../utils/task';
import AddTask from '../add-task/AddTask';
import NestedGrid from '../nested-grid/NestedGrid';
import './RootView.css'

function RootView(props: {state: stateType, send: sendType}) {

  return (
    <div className="RootView">
        <Stack direction="row" height="100%" spacing={2}>
        {/* <Stack direction="row" spacing={2}> */}
        <Stack height="100%" direction="column" spacing={2}>
                <AddTask state={props.state} send={props.send}/>
            </Stack>
            <div className='Grid'><NestedGrid 
            taskData={props.state.context.task_data as TaskCollection} 
            state={props.state} send={props.send}/></div>
            {/* <Stack height="100%" direction="column" spacing={2}> */}
        </Stack>
    </div>
  );
}

export default RootView;