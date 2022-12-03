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

interface Dictionary<T> {
  [Key: string]: T;
}
const dictionary_of_colors: any = {"LOW":"green", "MEDIUM":"cyan", "HIGH":"red"};
export function TaskItem(props: {task: Task, height:number, }) {
  const [show, setShow] = useState(false);
  const hour_start = props.task.start.format("H:mm")
  const hour_end = props.task.end.format("H:mm")

  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

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
          onClick={()=>{props.send('DELETE', {id: props.task.id});setOpen(false)}}>Remove</Button>
        </Box>
      </Modal>
    </Card>
  );
}
