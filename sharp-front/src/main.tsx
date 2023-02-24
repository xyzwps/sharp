import React, { useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import Entry from './routes';
import { useAuthStore } from './store/auth';
import 'bootstrap/dist/css/bootstrap.min.css';
import { MantineProvider } from '@mantine/core';
import { NotificationsProvider } from '@mantine/notifications';
import { useThemeStore } from './store/theme';
import { MantineTheme } from '@mantine/core';

const themeBy = (theme: string): MantineTheme | undefined => {
  switch (theme) {
    case 'light':
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      return { colorScheme: 'light' };
    case 'dark':
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      return { colorScheme: 'dark' };
    default:
      return undefined;
  }
};

function Root() {
  const load = useAuthStore((state) => state.load);
  const theme = useThemeStore((state) => state.theme);

  useEffect(load, [0]);

  return (
    <MantineProvider theme={themeBy(theme)}>
      <NotificationsProvider position="top-center">
        <Entry />
      </NotificationsProvider>
    </MantineProvider>
  );
}

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <Root />
  </React.StrictMode>
);
