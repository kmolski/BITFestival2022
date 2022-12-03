import { Button, Grid, Stack } from '@mui/material';
import NestedGrid from '../nested-grid/NestedGrid';

function RootView() {

  return (
    <div className="RootView">
        <Stack direction="row" spacing={2}>
            <NestedGrid/>
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