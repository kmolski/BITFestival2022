import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { TextField } from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers';
import Stack from '@mui/material/Stack';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { DesktopDatePicker } from '@mui/x-date-pickers/DesktopDatePicker';
import moment, { Moment } from 'moment';
import { sendType, stateType } from '../../machines/types';

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
};

export default function AddTask(props: {state: stateType, send: sendType}) {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const sendCommitMessage = () => {
    props.send('COMMIT')
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
      <Button onClick={handleOpen}>Add task</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <TextField id="outlined-basic" label="Name" variant="outlined" onChange={handleTitleChange}/>
            <LocalizationProvider dateAdapter={AdapterMoment}>
                <DesktopDatePicker
                label="Date"
                inputFormat="MM/DD/YYYY"
                value={timeStart}
                onChange={handleDateChange}
                renderInput={(params) => <TextField {...params} />}
                />
                <TimePicker
                label="Start time"
                value={timeStart}
                onChange={handleChangeTimeStart}
                renderInput={(params) => <TextField {...params} />}
                />
                <TimePicker
                label="End time"
                value={timeEnd}
                onChange={handleChangeTimeEnd}
                renderInput={(params) => <TextField {...params} />}
                />
            </LocalizationProvider>
            <button onClick={sendTaskMessage}>Add the task</button>
            <button onClick={sendCommitMessage}>Commit the task</button>
            {props.state.context.suggestion_data.map(item => item.name)}
        </Box>
      </Modal>
    </div>
  );
}