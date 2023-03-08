import { Flex, Group, Header, Space } from '@mantine/core';
import NavLink from './NavLink';

import { useAuthStore } from '../../store/auth';
import { useThemeStore } from '../../store/theme';

export default function AppHeader() {
  const [userState, logout] = useAuthStore((state) => [state.status, state.logout]);
  const [theme, setTheme] = useThemeStore((state) => [state.theme, state.setTheme]);

  return (
    <Header height={56}>
      <Flex align="center" justify="space-between" h={56}>
        <Group spacing="xs">
          <Space w="xs" />
          <NavLink to="/">Antleg.run</NavLink>
          <NavLink to="/todos">Todo</NavLink>
          <NavLink to="/a/create-post">Create A Post</NavLink>
          <NavLink to="/demo/markdown">Markdown Demo</NavLink>
          <NavLink to="/">ddd</NavLink>
        </Group>
        <Group spacing="xs">
          <NavLink to={() => setTheme(theme === 'light' ? 'dark' : 'light')}>TH</NavLink>
          {(() => {
            switch (userState.state) {
              case 'auth':
                return (
                  <>
                    <NavLink to="/profile">个人信息</NavLink>
                    <NavLink to={() => logout()}>退出登录</NavLink>
                  </>
                );
              case 'unauth':
                return (
                  <>
                    <NavLink to="/a/login">登录</NavLink>
                    <NavLink to="/a/register">注册</NavLink>
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
