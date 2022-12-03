import * as React from 'react';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import './NestedGrid.css';
import { useState } from 'react';
import { TaskItem } from '../task-item/TaskItem';
import { emptyTaskCollection, Task, TaskCollection } from '../../utils/task';
import { Typography } from '@mui/material';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

function Column(props:{columnName:string, taskList:Task[]}) {
    const oneDayEquivalentHeight = 500;
    const oneHourEquivalentHeight = oneDayEquivalentHeight / 24;
    const [tasks, setTaskList] = useState(props.taskList);
    return (
        <React.Fragment>
        <Grid item xs={1.5} sx={{bgcolor: 'blue' }}>
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
        {tasks.map((task) => {
            console.log(task.start);
            console.log(task.end);
            console.log('task.end.diff(task.start, "days")');
            console.log(task.end.diff(task.start));
            return (<TaskItem task={task} height={task.end.diff(task.start, "hours")*oneHourEquivalentHeight}/>)
        })}
        </Grid> 
        </React.Fragment>
    );
}

export default function NestedGrid(props: {taskData:TaskCollection}) {
  return (
    <div className='Column'>
    <Box sx={{ flexGrow: 1}}>
      <Grid container spacing={1}>
        <Column columnName="Monday" taskList={props.taskData.Monday}/>
        <Column columnName="Tuesday" taskList={props.taskData.Tuesday}/>
        <Column columnName="Wednesday" taskList={props.taskData.Wednesday}/>
        <Column columnName="Thursday" taskList={props.taskData.Thursday}/>
        <Column columnName="Friday" taskList={props.taskData.Friday}/>
        <Column columnName="Saturday" taskList={props.taskData.Saturday}/>
        <Column columnName="Sunday" taskList={props.taskData.Sunday}/>
      </Grid>
    </Box>
    </div>
  );
}