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
      <div style={{ margin: '16px auto', maxWidth: 1366 }}>
        <Outlet />
      </div>
    </AppShell>
  );
}
