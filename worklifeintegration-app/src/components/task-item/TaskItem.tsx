import React, { useState } from 'react';
import { Task } from '../../utils/task';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';

{/* <div className="TaskItem">
<>{props.task.name}</>
</div> */}

export function TaskItem(props: {task: Task}) {
  return (
    <Box sx={{ width: '100%', maxWidth: 360, bgcolor: 'cyan' }}>
      <Box sx={{ my: 3, mx: 2 }}>
        <Grid container alignItems="center">
          <Grid item xs>
            <Typography gutterBottom variant="h4" component="div">
              {props.task.name}
            </Typography>
          </Grid>
        </Grid>
        <Typography color="text.secondary" variant="body2">
          Godzina taska
        </Typography>
      </Box>
    </Box>
  );
}
