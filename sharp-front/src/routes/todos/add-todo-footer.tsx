import _ from 'lodash';
import { useState } from 'react';

import { Card } from '@mantine/core';

import { useTodoStore } from '../../store/todo';

function AddTodoFooter() {
  const [details, setDetails] = useState<string>('');
  const createTodo = useTodoStore((state) => state.createTodo);

  const handleSave = () => {
    const trimed = _.trim(details);
    if (trimed.length > 0) {
      createTodo(details);
      setDetails('');
    }
  };

  return (
    <Card.Section style={{ padding: 2, marginBottom: -16 }}>
      <input
        type="text"
        value={details}
        onChange={(e) => setDetails(e.target.value)}
        onBlur={handleSave}
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            e.preventDefault();
            handleSave();
          }
        }}
        placeholder="添加待办事项"
        style={{
          backgroundColor: 'transparent',
          border: 0,
          padding: 8,
          boxSizing: 'border-box',
          width: '100%',
        }}
      />
    </Card.Section>
  );
}

export default AddTodoFooter;
