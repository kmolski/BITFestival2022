import React, { useState } from 'react';
import './Column.css';
import { convertTypeAcquisitionFromJson } from 'typescript';

const myBoolean: boolean = true;

function Column() {
  const timeRowData = [
      { hour: "08:00 - 09:00", monday: 'sometask', tuesday: 'sometask',  
    wednesday:'sometask', thursday:'sometask', friday:'sometask', 
    saturday:'sometask', sunday:'sometask'},
      { hour: "09:00 - 10:00", monday: 'sometask', tuesday: 'sometask',  
    wednesday:'sometask', thursday:'sometask', friday:'sometask', 
    saturday:'sometask', sunday:'sometask'},
      { hour: "10:00 - 11:00", monday: 'sometask', tuesday: 'sometask',  
    wednesday:'sometask', thursday:'sometask', friday:'sometask', 
    saturday:'sometask', sunday:'sometask'},
    ]
  const [timeRows, setTimeRows] = useState(timeRowData);

  return (
    <div className="App">
        <table>
          <thead>
              <tr>
                <th>Hour</th>
                <th>Monday</th>
                <th>Tuesday</th>
                <th>Wednesday</th>
                <th>Thursday</th>
                <th>Friday</th>
                <th>Saturday</th>
                <th>Sunday</th>
              </tr>
          </thead>
          <tbody>
            {timeRows.map((timeRow) => (
              <tr>
                <td>{timeRow.hour}</td>
                <td>{timeRow.monday}</td>
                <td>{timeRow.tuesday}</td>
                <td>{timeRow.wednesday}</td>
                <td>{timeRow.thursday}</td>
                <td>{timeRow.friday}</td>
                <td>{timeRow.saturday}</td>
                <td>{timeRow.sunday}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
  );
}

export default Column;
