import { useState } from 'react';
import MarkdownEditor from '../../components/MarkdownEditor';

export default function MarkdownDemo() {
  const [text, setText] = useState<string>('');

  return (
    <div>
      <MarkdownEditor value={text} onChange={setText} style={{ height: '100vh' }} />
    </div>
  );
}
