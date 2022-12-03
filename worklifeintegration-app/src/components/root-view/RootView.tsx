import { Button, Grid, Stack } from '@mui/material';
import { State } from 'xstate';
import NestedGrid from '../nested-grid/NestedGrid';

function RootView(props: {state: any, send: any
}) {

  return (
    <div className="RootView">
        <Stack direction="row" spacing={2}>
            <NestedGrid taskData={props.state.context.task_data}/>
            <Stack direction="column" spacing={2}>
                <Button variant="text">Text</Button>
                <Button variant="contained">Contained</Button>
                <Button variant="outlined">Outlined</Button>
            </Stack>
        </Stack>
    </div>
  );
}

export default RootView;