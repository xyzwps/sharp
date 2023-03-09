import { Loader } from '@mantine/core';

export default function BlockLoader() {
  return (
    <div style={{ width: '100%', textAlign: 'center' }}>
      <Loader variant="bars" />
    </div>
  );
}
