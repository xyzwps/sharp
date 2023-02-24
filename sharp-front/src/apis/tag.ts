import { request } from './http';

export type SimpleTagView = {
  id: number;
  name: string;
};

export type TaggedType = 'POST';

export const getResourceTags = (type: TaggedType, id: string): Promise<SimpleTagView[]> =>
  request({
    method: 'get',
    url: `/api/tags/${type.toLowerCase()}/${id}`,
  });

export const searchTags = (q: string): Promise<SimpleTagView[]> =>
  q == '' ? Promise.resolve([]) : request({ url: '/api/tags/search', qs: { q } });

export const createTag = (name: string): Promise<SimpleTagView> =>
  request({ method: 'post', url: '/api/tags', json: { name } });

/**
 * @returns 达标后，被打标对象上的本次打标标签
 */
export const doTag = (
  type: TaggedType,
  id: string,
  body: { added?: number[]; removed?: number[] }
): Promise<SimpleTagView[]> =>
  request({
    method: 'patch',
    url: `/api/tags/${type}/${id}`,
    json: body,
  });
