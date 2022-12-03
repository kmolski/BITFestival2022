import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { FormLabel, TextField } from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers';
import Stack from '@mui/material/Stack';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { AdapterMoment } from '@mui/x-date-pickers/AdapterMoment';
import { DesktopDatePicker } from '@mui/x-date-pickers/DesktopDatePicker';
import moment, { Moment } from 'moment';
import { Task } from '../../utils/task';
import { margin } from '@mui/system';

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


export default function RemoveTask(props: {task: Task}) {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <div>
      <Button onClick={handleOpen} size="small">Remove</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
            <Box>
                <tr>Name: {props.task.name}</tr>
                <tr>Start time: {props.task.start.format('MMMM Do YYYY, h:mm:ss a')}</tr>
                <tr>End time: {props.task.end.format('MMMM Do YYYY, h:mm:ss a')}</tr>
                <tr>Place: {props.task.placeId}</tr>
                <tr>Placement limit: {props.task.placementLimitId}</tr>
                <tr>Priority: {props.task.taskPriority}</tr>
            </Box>
          <Button variant="contained" color="error" 
          onClick={()=>{/*komunikat do backendu żeby usunąć task*/setOpen(false)}}>Remove</Button>
        </Box>
      </Modal>
    </div>
  );
}