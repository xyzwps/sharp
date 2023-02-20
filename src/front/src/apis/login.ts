import { request } from './http';

export type LoginData = {
  username: string;
  password: string;
};

export const login = (data: LoginData): Promise<UserDetails> =>
  request({
    method: 'post',
    url: '/api/login',
    json: data,
  });
