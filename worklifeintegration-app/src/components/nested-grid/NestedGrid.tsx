import * as React from 'react';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import { useState } from 'react';
import { TaskItem } from '../task-item/TaskItem';
import moment from 'moment';
import { emptyTaskCollection, Task } from '../../utils/task';
import { Typography } from '@mui/material';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

function Column(props:{columnName:string, taskList:Task[]}) {
    const [tasks, setTaskList] = useState(props.taskList);
    return (
        <React.Fragment>
        <Grid item xs={1.5}>
        <Box sx={{ width: '100%', maxWidth: 200, bgcolor: 'cyan' }}>
            <Box sx={{ my: 3, mx: 2 }}>
                <Grid container alignItems="center">
                    <Grid item xs>
                        <Typography gutterBottom variant="h6" component="div">
                        {props.columnName}
                        </Typography>
                    </Grid>
                </Grid>
            </Box>
        </Box>
        {tasks.map((task) => (
            <TaskItem task={{
                name: task.name,
                start: task.start,
                end: task.end
            }}/>
                ))}
        </Grid> 
        </React.Fragment>
    );
}

export default function NestedGrid() {

const [rows, setRows] = useState(emptyTaskCollection);

  const table = [{text:"some"}, {text:"some"}, {text:"some"}];
  return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container spacing={1}>
        <Column columnName="Monday" taskList={emptyTaskCollection.Monday}/>
        <Column columnName="Tuesday" taskList={emptyTaskCollection.Tuesday}/>
        <Column columnName="Wednesday" taskList={emptyTaskCollection.Wednesday}/>
        <Column columnName="Thursday" taskList={emptyTaskCollection.Thursday}/>
        <Column columnName="Friday" taskList={emptyTaskCollection.Friday}/>
        <Column columnName="Saturday" taskList={emptyTaskCollection.Saturday}/>
        <Column columnName="Sunday" taskList={emptyTaskCollection.Sunday}/>
      </Grid>
    </Box>
  );
}