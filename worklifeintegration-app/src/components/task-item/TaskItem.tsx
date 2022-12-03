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
import { EventData, State, EventObject, TypegenDisabled } from 'xstate';

{/* <div className="TaskItem">
<>{props.task.name}</>
</div> */}
function setState(){

}

interface Dictionary<T> {
  [Key: string]: T;
}

const dictionary_of_colors: any = {"LOW":"green", "MEDIUM":"cyan", "HIGH":"red"};
export function TaskItem(props: {task: Task, height:number, }) {
  const [show, setShow] = useState(false);
  return (
    <Card sx={{ width: '100%', height: '100%', maxWidth: 200, 
    maxHeight:props.height, bgcolor: dictionary_of_colors[props.task.taskPriority] }} 
    onClick={() => setState()}>
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
        <RemoveTask task={props.task} send={function (event: any, payload?: EventData | undefined): State<any, EventObject, any, { value: any; context: any; }, TypegenDisabled> {
        throw new Error('Function not implemented.');
      } }/>
    </Card>
  );
}
