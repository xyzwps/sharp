import { useDrag } from 'react-dnd';
import { useState } from 'react';
import _ from 'lodash';

import { Card } from '@mantine/core';

import { useTodoStore } from '../../store/todo';
import dayjs from 'dayjs';
import { SERVER_DATETIME_FORMAT } from '../../dict';

type TodoItemProps = {
  todo: Todo;
};

function TodoItem({ todo }: TodoItemProps) {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: 'todo',
    item: todo,
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  }));

  const updateTodo = useTodoStore((state) => state.updateTodo);

  const [editing, setEditing] = useState<boolean>(false);
  const [editDetails, setEditDetails] = useState<string>(todo.details);

  const handleSave = () => {
    const trimed = _.trim(editDetails);
    if (trimed.length > 0) {
      const newTodo = _.cloneDeep(todo);
      newTodo.details = trimed;
      newTodo.updateTime = dayjs().format(SERVER_DATETIME_FORMAT);
      updateTodo(newTodo);
      setEditing(false);
    } else {
      setEditing(false);
      setEditDetails(todo.details);
    }
  };

  return (
    <Card.Section withBorder ref={drag} style={{ cursor: isDragging ? 'grabbing' : 'grab', padding: '8px 16px' }}>
      {editing ? (
        <input
          style={{ padding: '0px 4px', border: 0 }}
          onBlur={handleSave}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault();
              handleSave();
            }
          }}
          onChange={(e) => setEditDetails(e.target.value)}
          value={editDetails}
        />
      ) : (
        <span onDoubleClick={() => setEditing(true)}>{todo.details}</span>
      )}
    </Card.Section>
  );
}

export default TodoItem;
