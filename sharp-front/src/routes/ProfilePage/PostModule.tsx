import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { getMyPosts, PostDto } from '../../apis/post';
import { Skeleton, Card, Text } from '@mantine/core';

type PostsSM = { state: 'loading' } | { state: 'succeed'; posts: PostDto[] } | { state: 'failed'; error: Error };

export default function PostModule() {
  const [sm, setSM] = useState<PostsSM>({ state: 'loading' });

  useEffect(() => {
    getMyPosts()
      .then((posts) => setSM({ state: 'succeed', posts }))
      .catch((err) => setSM({ state: 'failed', error: err }));
  }, []);

  switch (sm.state) {
    case 'loading':
      return (
        <div>
          <Skeleton height={32} radius="md" />
          <Skeleton height={32} radius="md" />
        </div>
      );
    case 'failed':
      return <div>TODO: 处理错误</div>;
    case 'succeed':
      return <PostList posts={sm.posts} />;
    default:
      throw new Error(`${PostModule.name} state bug!`);
  }
}

function PostList(props: { posts: PostDto[] }) {
  const navigate = useNavigate();
  return (
    <Card shadow="xs" withBorder>
      <Card.Section withBorder style={{ padding: '8px 16px' }}>
        <Text style={{ fontSize: '1.1rem', fontWeight: 'bold' }}>我的文章</Text>
      </Card.Section>
      {props.posts.map((it) => (
        <Card.Section withBorder style={{ padding: '8px 16px' }} key={it.id}>
          <Text style={{ fontSize: '1.5rem', cursor: 'pointer' }} onClick={() => navigate(`/posts/${it.id}`)}>
            {it.title}
          </Text>
          <small style={{ color: 'darkgray' }}>
            {it.createTime.substring(0, 10)} / {it.updateTime.substring(0, 10)}
          </small>
        </Card.Section>
      ))}
    </Card>
  );
}
