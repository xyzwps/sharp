import { Avatar, Card, Flex, Grid, Space, Text } from '@mantine/core';
import AuthHelper from '../common/AuthHelper';
import PostModule from './PostModule';
import avatarUrl from '../../assets/avatar.png';

export default function ProfilePage() {
  return <AuthHelper onAuth={(user) => <Profile user={user} />} onUnauth="/" />;
}

function Profile({ user }: { user: UserDetails }) {
  return (
    <Grid>
      <Grid.Col span={4}>
        <UserDetailsModule user={user} />
      </Grid.Col>
      <Grid.Col span={8}>
        <PostModule />
      </Grid.Col>
    </Grid>
  );
}

function UserDetailsModule({ user }: { user: UserDetails }) {
  return (
    <Card withBorder shadow="xs">
      <Flex>
        <Avatar src={avatarUrl} size="xl" radius="lg" style={{ border: '1px solid purple' }} />
        <Space w="sm" />
        <div>
          <Text style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{user.username}</Text>
          <Text style={{ fontSize: '0.8rem', color: 'darkgrey' }}>注册于：{user.registerTime.substring(0, 10)}</Text>
        </div>
      </Flex>
    </Card>
  );
}
