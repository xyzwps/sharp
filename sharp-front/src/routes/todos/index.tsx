import { DndProvider, useDrop } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import _ from 'lodash';
import dayjs from 'dayjs';

import { Card, SimpleGrid, Text } from '@mantine/core';

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
    <Card ref={drop} style={{ borderColor: isOver ? 'red' : 'black' }}>
      <Card.Section withBorder style={{ padding: '8px 16px' }}>
        <Text style={{ fontSize: '1.1rem', fontWeight: 'bold' }}>{title}</Text>
      </Card.Section>
      {filtered.map((it) => (
        <TodoItem key={it.id} todo={it} />
      ))}
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
      <SimpleGrid cols={3}>
        <TodosView todos={todos} status="TODO" title="待处理" />
        <TodosView todos={todos} status="IN_PROGRESS" title="进行中" />
        <TodosView todos={todos} status="DONE" title="已完成" />
      </SimpleGrid>
    </DndProvider>
  );
}
