import { request } from './http';
import { UserDto } from './user';

export type CreatePostData = {
  type: PostType;
  title: string;
  content: string;
  idem: string
};

export type UpdatePostData = {
  id: number;
  title: string;
  content: string;
};

export type PostDto = {
  id: number;
  title: string;
  createTime: string;
  updateTime: string;
};

export type PostContentDto = {
  type: PostType;
  content: string;
};

export type PostWithContentDto = {
  post: PostDto;
  content: PostContentDto;
  author?: UserDto | null;
};

export const createPost = (data: CreatePostData): Promise<PostDto> =>
  request({ method: 'post', url: '/api/posts', json: data });

export const updatePost = (data: UpdatePostData): Promise<PostDto> =>
  request({ method: 'put', url: '/api/posts', json: data });

export const getMyPosts = (): Promise<PostDto[]> => request({ method: 'get', url: '/api/posts' });

export const getSinglePost = (id: number): Promise<PostWithContentDto> =>
  request({ method: 'get', url: `/api/posts/${id}` });
