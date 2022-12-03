import * as React from 'react';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import './NestedGrid.css';
import { useState } from 'react';
import { TaskItem } from '../task-item/TaskItem';
import { emptyTaskCollection, Task, TaskCollection } from '../../utils/task';
import { Button, Typography } from '@mui/material';
import { sendType, stateType } from '../../machines/types';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

function Column(props:{columnName:string, taskList:Task[], state: stateType, send: sendType}) {
    const containerRef = React.useRef<HTMLDivElement | null>(null)
    const [oneDayEquivalentHeight, setOneDayEquivalentHeight] = useState(500)

    const oneHourEquivalentHeight = oneDayEquivalentHeight / 18;
    const tasks = props.taskList;

    React.useEffect(() => {
        const handleResize = () => {
            setOneDayEquivalentHeight( containerRef.current?.clientHeight || 500)
        }
        window.addEventListener('resize', handleResize)
        return () => {
            window.removeEventListener('resize', handleResize)
        }
    } ,[])

    return (
        <Grid height="100%" ref={containerRef} item xs={1.5} sx={{bgcolor: 'blue' }}>
            <Box sx={{ width: '100%', maxWidth: 200, bgcolor: 'cyan' }}>
                <Box sx={{ my: 3, mx: 2 }}>
                    <Grid container alignItems="center">
                        <Grid item xs>
                            <Button component="div">
                            {props.columnName}
                            </Button>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
            {tasks.map((task) => {
                // console.log(task.start);
                // console.log(task.end);
                // console.log('task.end.diff(task.start, "days")');
                // console.log(task.end.diff(task.start));
                return (<TaskItem task={task} height={task.end.diff(task.start, "hours")*oneHourEquivalentHeight} />)
            })}
        </Grid> 
    );
}

export default function NestedGrid(props: {taskData:TaskCollection, state: stateType, send: sendType}) {
  return (
    <div className='Column'>
    <Box sx={{ flexGrow: 1, height: '100%', width: '100%'}}>
      <Grid sx={{ height: '100%', width: '100%'}} container spacing={1}>
        <Column columnName="Monday" taskList={props.taskData.Monday} state={props.state} send={props.send}/>
        <Column columnName="Tuesday" taskList={props.taskData.Tuesday} state={props.state} send={props.send}/>
        <Column columnName="Wednesday" taskList={props.taskData.Wednesday} state={props.state} send={props.send}/>
        <Column columnName="Thursday" taskList={props.taskData.Thursday} state={props.state} send={props.send}/>
        <Column columnName="Friday" taskList={props.taskData.Friday} state={props.state} send={props.send}/>
        <Column columnName="Saturday" taskList={props.taskData.Saturday} state={props.state} send={props.send}/>
        <Column columnName="Sunday" taskList={props.taskData.Sunday} state={props.state} send={props.send}/>
      </Grid>
    </Box>
    </div>
  );
}