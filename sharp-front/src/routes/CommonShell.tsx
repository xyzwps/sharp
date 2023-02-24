import Container from 'react-bootstrap/Container';
import { Outlet } from 'react-router-dom';
import { AppShell } from '@mantine/core';
import AppHeader from './common/AppHeader';

export default function CommonShell() {
  return (
    <AppShell
      padding="md"
      header={<AppHeader />}
      styles={(theme) => ({
        main: { backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[8] : theme.colors.gray[0] },
      })}
    >
      <Container>
        <Outlet />
      </Container>
    </AppShell>
  );
}
