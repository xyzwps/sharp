import { request } from './http';

export const getProfile = (): Promise<UserDetails> =>
  request({
    method: 'get',
    url: '/api/users/current',
  });
