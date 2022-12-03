import * as React from 'react';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import { useState } from 'react';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

function FormRow(props:{taskList:string[]}) {
    const [tasks, setTaskList] = useState(props.taskList);
    return (
        <React.Fragment>
        <Grid item xs={4}>
        {tasks.map((task) => (
            <Item>{task}</Item>
                ))}
        </Grid> 
        </React.Fragment>
    );
}

export default function NestedGrid() {

const timeRowData = [
  { hour: "08:00 - 09:00", monday: 'sometask', tuesday: 'sometask',  
wednesday:'sometask', thursday:'sometask', friday:'sometask', 
saturday:'sometask', sunday:'sometask', taskList:["item1", "item2", "item3"]},
  { hour: "09:00 - 10:00", monday: 'sometask', tuesday: 'sometask',  
wednesday:'sometask', thursday:'sometask', friday:'sometask', 
saturday:'sometask', sunday:'sometask', taskList:["item4"]},
  { hour: "10:00 - 11:00", monday: 'sometask', tuesday: 'sometask',  
wednesday:'sometask', thursday:'sometask', friday:'sometask', 
saturday:'sometask', sunday:'sometask', taskList:["item5", "item6"]},
]
const [timeRows, setTimeRows] = useState(timeRowData);

  const table = [{text:"some"}, {text:"some"}, {text:"some"}];
  return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container spacing={1}>
      {timeRows.map((timeRow) => (
            <FormRow taskList={timeRow.taskList}/>
            ))}
      </Grid>
    </Box>
  );
}