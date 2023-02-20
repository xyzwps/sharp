import { request } from './http';

export const getTodos = (): Promise<Todo[]> =>
  request({
    method: 'get',
    url: '/api/todos',
  });

export const getTodoById = (todoId: number): Promise<Todo> =>
  request({
    method: 'get',
    url: `/api/todos/${todoId}`,
  });

type PatchTodo = {
  details: string;
  status: string;
};

export const patch = (todoId: string, data: PatchTodo): Promise<void> =>
  request({
    method: 'patch',
    url: `/api/todos/${todoId}`,
    json: data,
  });

type CreateTodo = {
  todoId: string;
  details: string;
  status: string;
};

export const create = (data: CreateTodo): Promise<void> =>
  request({
    method: 'post',
    url: '/api/todos',
    json: data,
  });
