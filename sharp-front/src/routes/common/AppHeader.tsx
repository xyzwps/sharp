import { Button, Flex, Group, Header, Space } from '@mantine/core';
import { useNavigate } from 'react-router-dom';

import { useAuthStore } from '../../store/auth';
import { useThemeStore } from '../../store/theme';

type NavButtonProps = {
  to: string;
  children: React.ReactNode;
};

function NavButton({ to, children }: NavButtonProps) {
  const navigate = useNavigate();
  return <Button onClick={() => navigate(to)}>{children}</Button>;
}

export default function AppHeader() {
  const [userState, logout] = useAuthStore((state) => [state.status, state.logout]);
  const [theme, setTheme] = useThemeStore((state) => [state.theme, state.setTheme]);

  return (
    <Header height={56}>
      <Flex align="center" justify="space-between" h={56}>
        <Group spacing="xs">
          <Space w="xs" />
          <NavButton to="/">Antleg.run</NavButton>
          <NavButton to="/todos">Todo</NavButton>
          <NavButton to="/a/create-post">Create A Post</NavButton>
          <NavButton to="/demo/markdown">Markdown Demo</NavButton>
        </Group>
        <Group spacing="xs">
          <Button onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')}>TH</Button>
          {(() => {
            switch (userState.state) {
              case 'auth':
                return (
                  <>
                    <NavButton to="/profile">个人信息</NavButton>
                    <Button onClick={() => logout()}>退出登录</Button>
                  </>
                );
              case 'unauth':
                return (
                  <>
                    <NavButton to="/a/login">登录</NavButton>
                    <NavButton to="/a/register">注册</NavButton>
                  </>
                );
              default:
                return null;
            }
          })()}
          <Space w="xs" />
        </Group>
      </Flex>
    </Header>
  );
}
