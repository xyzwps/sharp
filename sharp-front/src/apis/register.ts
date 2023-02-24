import { request } from './http';

type RegisterData = {
  username: string;
  password: string;
};

export const register = (data: RegisterData) =>
  request({
    method: 'post',
    url: '/api/register/naive',
    json: data,
  });
