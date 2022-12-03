import React, { useState } from 'react';
import { Task } from '../../utils/task';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import { Card, Modal } from '@mui/material';
import RemoveTask from '../remove-task/RemoveTask';
import { sendType, stateType } from '../../machines/types';

{/* <div className="TaskItem">
<>{props.task.name}</>
</div> */}
function setState(){

}

export function TaskItem(props: {task: Task, height:number, state: stateType, send: sendType}) {
  const [show, setShow] = useState(false);
  return (
    <Card sx={{ width: '100%', height: '100%', maxWidth: 200, 
    maxHeight:props.height, bgcolor: 'cyan' }} onClick={() => setState()}>
        <Grid container alignItems="center">
          <Grid item xs>
            <Typography gutterBottom variant="h6" component="div">
              {props.task.name}
            </Typography>
          </Grid>
        </Grid>
        <Typography color="text.secondary" variant="body2">
          Godzina taska
        </Typography>
        <RemoveTask task={props.task} state={props.state} send={props.send}/>
    </Card>
  );
}
