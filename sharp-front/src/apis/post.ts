import { request } from './http';
import { UserSummary } from './user';

export type CreatePostData = {
  type: PostType;
  title: string;
  content: string;
  idem: string;
};

export type UpdatePostData = {
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
  createTime: string;
  updateTime: string;
};

export type RichPost = {
  id: number;
  title: string;
  createTime: string;
  updateTime: string;
  content: PostContentDto;
  author?: UserSummary | null;
};

export const createPost = (data: CreatePostData): Promise<{ postId: number }> =>
  request({ method: 'post', url: '/api/posts', json: data });

export const updatePost = (postId: number, data: UpdatePostData): Promise<{ postId: number }> =>
  request({ method: 'patch', url: `/api/posts/${postId}`, json: data });

export const getMyPosts = (): Promise<PostDto[]> => request({ method: 'get', url: '/api/posts' });

export const getSinglePost = (id: number): Promise<RichPost> =>
  request({ method: 'get', url: `/api/posts/${id}` });
