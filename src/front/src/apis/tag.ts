import { request } from './http';

export type SimpleTagView = {
  id: number;
  name: string;
};

export type TaggedType = 'POST';

export const getResourceTags = (type: TaggedType, id: string): Promise<SimpleTagView[]> =>
  request({
    method: 'get',
    url: `/api/tag/${type.toLowerCase()}/${id}`,
  });

export const searchTags = (q: string): Promise<SimpleTagView[]> =>
  q == ''
    ? Promise.resolve([])
    : request({
      url: '/api/tag/search',
      qs: { q },
    });

export const createTag = (name: string): Promise<SimpleTagView> =>
  request({
    method: 'post',
    url: '/api/tag',
    json: { name },
  });

/**
 * @returns 达标后，被打标对象上的全部标签
 */
export const doTag = (type: TaggedType, id: string, tagIds: number[]): Promise<SimpleTagView[]> =>
  request({
    method: 'post',
    url: `/api/tag/${type}/${id}`,
    json: { ids: tagIds },
  });
