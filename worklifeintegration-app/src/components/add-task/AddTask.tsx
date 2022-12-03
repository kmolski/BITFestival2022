import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { Card, Chip, Grid, TextField } from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers';
import Stack from '@mui/material/Stack';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { DesktopDatePicker } from '@mui/x-date-pickers/DesktopDatePicker';
import moment, { Moment } from 'moment';
import { sendType, stateType } from '../../machines/types';
import './AddTask.css'
import { State, stateValuesEqual } from 'xstate/lib/State';
import { Task } from '../../utils/task';
import { TaskItem } from '../task-item/TaskItem';
import { EventData, EventObject, TypegenDisabled } from 'xstate';

const style = {
  position: 'absolute' as 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
  display: 'flex',
  flexDirection: 'column',
  margin: '12px',
};

export function SimpleTaskItem(props: {task: Task}) {
  const day = props.task.start.format("dddd")
  const hour_start = props.task.start.format("H:mm")
  const hour_end = props.task.end.format("H:mm")

  return (
    <Card sx={{ width: '100%', height: '100%', maxWidth: 200, bgcolor: '#3cc6fc' }}>
        <Grid container alignItems="center">
          <Grid item xs>
            <Typography gutterBottom variant="h6" component="div">
              {props.task.name}
            </Typography>
          </Grid>
        </Grid>
        <Typography color="text.secondary" variant="body2">
         <Chip label={day} size="small" variant="outlined" />
          {hour_start + ' - ' +hour_end}
        </Typography>
    </Card>
  );
}

function TaskContainer(props: {tasks: Task[]}){
  

  if (props.tasks.length === 0) return <></>;
  
  return <div>  
    {props.tasks.map((task: Task) => <SimpleTaskItem task={task}/>)}
</div>

}

export default function AddTask(props: {state: stateType, send: sendType}) {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const sendCommitMessage = () => {
    props.send('COMMIT');
    setOpen(false);
  };

  const [title, setTitle] = React.useState("");
  
  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(event.target.value);
  };
  const [timeStart, setTimeStart] = React.useState<Moment | null>(
    moment(),
  );
  const [timeEnd, setTimeEnd] = React.useState<Moment | null>(
    moment(),
  );

  const handleDateChange = (newValue: Moment | null) => {
    setTimeStart(newValue);
    setTimeEnd(newValue);
  };
  const handleChangeTimeStart = (newValue: Moment | null) => {
    console.log(newValue);
    setTimeStart(newValue);
  };
  const handleChangeTimeEnd = (newValue: Moment | null) => {
    console.log(newValue);
    setTimeEnd(newValue);
  };


  const sendTaskMessage = () => {
    props.send('ADD', {title: title, start: timeStart, end: timeEnd})
  };

  return (
    <div>
      <Box m={2} pt={1}><Button variant="contained" onClick={handleOpen}>+&nbsp;Create</Button></Box>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <TextField id="outlined-basic" className='Paded'
          label="Name" variant="outlined" onChange={handleTitleChange}/>
            <LocalizationProvider dateAdapter={AdapterMoment}>
            <Box m={0} pt={1}><DesktopDatePicker
                className='Paded'
                label="Date"
                inputFormat="MM/DD/YYYY"
                value={timeStart}
                onChange={handleDateChange}
                renderInput={(params) => <TextField {...params} />}
                /></Box> 
                <Box m={0} pt={1}><TimePicker
                className='Paded'
                label="Start time"
                value={timeStart}
                onChange={handleChangeTimeStart}
                renderInput={(params) => <TextField {...params} />}
                /></Box> 
                <Box m={0} pt={1}><TimePicker
                className='Paded'
                label="End time"
                value={timeEnd}
                onChange={handleChangeTimeEnd}
                renderInput={(params) => <TextField {...params} />}
                /></Box> 
            </LocalizationProvider>
            <Box m={1} pt={0}><Button variant="contained" onClick={sendTaskMessage}>Find time</Button></Box> 
            <Box m={1} pt={1}><div onClick={sendCommitMessage}><TaskContainer tasks={props.state.context.suggestion_data}/></div></Box> 
        </Box>
      </Modal>
    </div>
  );
}