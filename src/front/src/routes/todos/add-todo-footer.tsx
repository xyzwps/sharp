import _ from 'lodash';
import { useState } from 'react';

import Card from 'react-bootstrap/Card';
import FormControl from 'react-bootstrap/FormControl';

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
    <Card.Footer style={{ padding: 3 }}>
      <FormControl
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
        style={{ backgroundColor: 'transparent', border: 0 }}
      />
    </Card.Footer>
  );
}

export default AddTodoFooter;
