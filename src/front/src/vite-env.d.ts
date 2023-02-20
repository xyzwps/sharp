/// <reference types="vite/client" />

type UserDetails = {
  id: number;
  username: string;
  /**
   * 形如: `2022-12-05T23:49:25.143`
   */
  registerTime: string;
  displayName: string;
};

type Todo = {
  id: string;
  details: string;
  status: TodoStatus;
  createTime: string;
  updateTime: string;
};

type TodoStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';

type PostType = 'HTML' | 'MD' | 'TXT';
