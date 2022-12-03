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

export default function RemoveTask() {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const [value, setValue] = React.useState<Moment | null>(
    moment('2014-08-18T21:11:54'),
  );

  const handleChange = (newValue: Moment | null) => {
    setValue(newValue);
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
          <TextField id="outlined-basic" label="Name" variant="outlined" />
            <LocalizationProvider dateAdapter={AdapterMoment}>
                <DesktopDatePicker
                label="Date"
                inputFormat="MM/DD/YYYY"
                value={value}
                onChange={handleChange}
                renderInput={(params) => <TextField {...params} />}
                />
                <TimePicker
                label="Start time"
                value={value}
                onChange={handleChange}
                renderInput={(params) => <TextField {...params} />}
                />
                <TimePicker
                label="End time"
                value={value}
                onChange={handleChange}
                renderInput={(params) => <TextField {...params} />}
                />
            </LocalizationProvider>
        </Box>
      </Modal>
    </div>
  );
}