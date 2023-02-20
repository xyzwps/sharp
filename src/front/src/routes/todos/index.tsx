import { DndProvider, useDrop } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import _ from 'lodash';
import dayjs from 'dayjs';

import ListGroup from 'react-bootstrap/ListGroup';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';

import { useTodoStore } from '../../store/todo';
import AddTodoFooter from './add-todo-footer';
import TodoItem from './todo-item';
import { SERVER_DATETIME_FORMAT } from '../../dict';
import AuthHelper from '../common/AuthHelper';
import { useEffect } from 'react';

type TodosViewProps = {
  todos: Todo[];
  status: TodoStatus;
  title: string;
};

function TodosView({ todos, status, title }: TodosViewProps) {
  const filtered = todos.filter((it) => it.status === status);
  const updateTodo = useTodoStore((state) => state.updateTodo);

  const [{ isOver }, drop] = useDrop(
    () => ({
      accept: 'todo',
      canDrop: (todo: Todo) => todo.status !== status,
      drop: (todo: Todo) => {
        const newTodo = _.cloneDeep(todo);
        newTodo.status = status;
        newTodo.updateTime = dayjs().format(SERVER_DATETIME_FORMAT);
        updateTodo(newTodo);
      },
      collect: (monitor) => ({
        isOver: !!monitor.isOver(),
      }),
    }),
    []
  );

  return (
    <Card ref={drop} border={isOver ? 'info' : 'default'}>
      <Card.Header>{title}</Card.Header>
      <ListGroup variant="flush">
        {filtered.map((it) => (
          <TodoItem key={it.id} todo={it} />
        ))}
      </ListGroup>
      {status === 'TODO' && <AddTodoFooter />}
    </Card>
  );
}

export default function TodoPage() {
  return <AuthHelper onAuth={() => <Todos />} onUnauth="/a/login" />;
}

function Todos() {
  const todos = useTodoStore((state) => state.todos);
  const load = useTodoStore((state) => state.load);

  useEffect(() => {
    load();
  }, [0]);

  return (
    <DndProvider backend={HTML5Backend}>
      <Row>
        <Col>
          <TodosView todos={todos} status="TODO" title="待处理" />
        </Col>
        <Col>
          <TodosView todos={todos} status="IN_PROGRESS" title="进行中" />
        </Col>
        <Col>
          <TodosView todos={todos} status="DONE" title="已完成" />
        </Col>
      </Row>
    </DndProvider>
  );
}
