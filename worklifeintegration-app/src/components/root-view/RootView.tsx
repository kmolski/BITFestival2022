import { Button, Grid, Stack } from '@mui/material';
import { State } from 'xstate';
import AddTask from '../add-task/AddTask';
import NestedGrid from '../nested-grid/NestedGrid';
import './RootView.css'

function RootView(props: {state: any, send: any
}) {

  return (
    <div className="RootView">
        <Stack direction="row" spacing={2}>
            <div className='Grid'><NestedGrid taskData={props.state.context.task_data}/></div>
            <Stack direction="column" spacing={2}>
                <AddTask/>
            </Stack>
        </Stack>
    </div>
  );
}

export default RootView;