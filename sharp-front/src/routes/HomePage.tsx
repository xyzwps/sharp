import { Button } from '@mantine/core';
import { showNotification } from '@mantine/notifications';

export default function HomePage() {
  return (
    <div>
      主页，还没想好画什么
      <Button onClick={() => showNotification({ title: '哈哈', message: '哈哈哈' })}>Toast</Button>
    </div>
  );
}
