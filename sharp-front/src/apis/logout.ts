import { request } from './http';

export const logout = () =>
  request({
    method: 'post',
    url: '/api/auth/logout',
  });
