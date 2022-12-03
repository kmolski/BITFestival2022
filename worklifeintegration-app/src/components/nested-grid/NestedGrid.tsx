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
    const containerRef = React.useRef<HTMLDivElement | null>(null)
    const [oneDayEquivalentHeight, setOneDayEquivalentHeight] = useState(500)

    const oneHourEquivalentHeight = oneDayEquivalentHeight / 24;
    const [tasks, setTaskList] = useState(props.taskList);

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
            <Box sx={{ height: "100%", width: '100%', maxWidth: 200, bgcolor: 'cyan' }}>
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
                return (<TaskItem task={{
                    name: task.name,
                    start: task.start,
                    end: task.end
                }} height={task.end.diff(task.start, "hours")*oneHourEquivalentHeight}/>)
            })}
        </Grid> 
    );
}

export default function NestedGrid(props: {taskData:TaskCollection}) {
  return (
    <div className='Column'>
    <Box sx={{ flexGrow: 1, height: '100%'}}>
      <Grid sx={{ height: '100%'}} container spacing={1}>
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