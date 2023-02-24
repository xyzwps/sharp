import { Avatar, Card, Flex, Grid, Space, Text } from '@mantine/core';
import { ReactNode } from 'react';
import { Link, LoaderFunctionArgs, useLoaderData } from 'react-router-dom';

import { getSinglePost, PostContentDto, RichPost } from '../../apis/post';
import Unsupported from '../../components/Unsupported';
import ReactMarkdown from '../../libs/markdown-render/ReactMarkdown';
import AuthHelper from '../common/AuthHelper';
import TagBar from '../common/TagBar';

export async function loader({ params }: LoaderFunctionArgs) {
  const { id } = params;
  if (id == null) throw new Error('Impossible!');

  const postId = +id;
  if (Number.isNaN(postId) || postId < 0) throw new Error('Invalid postId');

  return await getSinglePost(postId);
}

export default function SinglePostPage() {
  const post = useLoaderData() as RichPost;

  switch (post.content.type) {
    case 'MD':
      return (
        <PostCommon post={post}>
          <MarkdownContent content={post.content} />
        </PostCommon>
      );
    default:
      return <Unsupported>尚未支持的文章格式: {post.content.type}</Unsupported>;
  }
}

function MarkdownContent({ content }: { content: PostContentDto }) {
  return <ReactMarkdown text={content.content} />;
}

type PostCommonProps = {
  post: RichPost;
  children?: ReactNode;
};

function PostCommon({ post, children }: PostCommonProps) {
  const { author } = post;
  return (
    <Grid>
      <Grid.Col sm={12} md={9} lg={8}>
        <Card withBorder shadow="xs" p={24}>
          <h1 style={{ margin: '16px 0px' }}>{post.title}</h1>
          <Flex align="center" style={{ margin: '20px 0px' }}>
            <Avatar color="pink" />
            <Space w="xs" />
            <Text style={{ color: 'peru' }}>{author?.username}</Text>
            <Space w="xs" />
            <Text style={{ color: 'darkgray', fontSize: '0.9rem' }}>发布于：{post.createTime.substring(0, 10)}</Text>
            <Space w="xs" />
            <AuthHelper
              onAuth={(user) =>
                user.id == author?.id ? (
                  <Link to={`/posts/${post.id}/edit`}>
                    <small>📝&nbsp;编辑</small>
                  </Link>
                ) : (
                  <></>
                )
              }
              onUnauth={() => <></>}
            />
          </Flex>
          {children}
          <TagBar type="POST" id={post.id + ''} />
        </Card>
      </Grid.Col>
      <Grid.Col sm={12} md={3} lg={4}>
        <Card withBorder shadow="xs">
          dd
        </Card>
      </Grid.Col>
    </Grid>
  );
}
