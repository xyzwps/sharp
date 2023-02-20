import { showNotification } from '@mantine/notifications';

// TODO: 搞好一点
export const toastError = (err: unknown, message?: string) =>
  showNotification({
    title: message || (err as { message?: string }).message || '未知错误',
    message: <pre>{JSON.stringify(err, null, '  ')}</pre>,
  });
