import React, { useState } from 'react';
import { Task } from '../../utils/task';

export function TaskItem(props: {task: Task}) {
  return (
    <div className="TaskItem">
      <>{props.task.name}</>
    </div>
  );
}
