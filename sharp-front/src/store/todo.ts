import { create } from 'zustand';
import _ from 'lodash';
import { getTodos, patch as patchTodo, create as createTodo } from '../apis/todos';
import * as dayjs from 'dayjs';
import { SERVER_DATETIME_FORMAT } from '../dict';

export type TodoSM = { state: 'loading' } | { state: 'succeed' } | { state: 'failed'; error: Error };

export type TodoStore = {
  status: TodoSM;
  todos: Todo[];
  load: () => Promise<void>;
  __load_without_loading__: () => Promise<void>;
  createTodo: (details: string) => Promise<void>;
  updateTodo: (changedTodo: Todo) => Promise<void>;
};

const upsertTodos = (todo: Todo, oldTodos: Todo[]): Todo[] => {
  const newTodos = _.cloneDeep(oldTodos);
  const index = newTodos.findIndex((it) => it.id === todo.id);
  if (index >= 0) {
    newTodos[index] = todo;
  } else {
    newTodos.push(todo);
  }
  return newTodos;
};

export const useTodoStore = create<TodoStore>((set, get) => ({
  status: { state: 'loading' },
  todos: [],
  load: async () => {
    set({ status: { state: 'loading' } });
    await get().__load_without_loading__();
  },
  __load_without_loading__: async () => {
    try {
      set({ status: { state: 'succeed' }, todos: await getTodos() });
    } catch (err) {
      set({ status: { state: 'failed', error: err as Error } });
    }
  },
  createTodo: async (details: string) => {
    const newTodo: Todo = {
      id: dayjs().format('YYYYMMDDHHmmssSSS'),
      details,
      status: 'TODO',
      createTime: dayjs().format(SERVER_DATETIME_FORMAT),
      updateTime: dayjs().format(SERVER_DATETIME_FORMAT),
    };
    set({ status: { state: 'succeed' }, todos: upsertTodos(newTodo, get().todos) });

    try {
      await createTodo({ todoId: newTodo.id, details: newTodo.details, status: newTodo.status });
      get().__load_without_loading__();
    } catch (err) {
      set({ status: { state: 'failed', error: err as Error } });
    }
  },
  updateTodo: async (changedTodo: Todo) => {
    set({ status: { state: 'succeed' }, todos: upsertTodos(changedTodo, get().todos) });
    try {
      await patchTodo(changedTodo.id, { details: changedTodo.details, status: changedTodo.status });
      get().__load_without_loading__();
    } catch (err) {
      set({ status: { state: 'failed', error: err as Error } });
    }
  },
}));
