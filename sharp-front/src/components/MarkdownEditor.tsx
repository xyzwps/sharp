import { useState } from 'react';
import { Block } from '../libs/markdown-render/parser';
import CodeMirror from '@uiw/react-codemirror';
import ReactMarkdownRender from '../libs/markdown-render/ReactMarkdownRender';
import { parse } from '../libs/markdown-render/ReactMarkdown';

type Props = {
  value: string;
  onChange: (value: string) => void;
  style?: React.CSSProperties;
};

export default function MarkdownEditor({ value, onChange, style }: Props) {
  const [blocks, setBlocks] = useState<Block[]>(parse(value));

  const onTextChange = (t: string) => {
    onChange(t);
    setBlocks(parse(t));
  };

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', ...style }}>
      <CodeMirror
        style={{ overflowX: 'auto', border: '1px solid red' }}
        height="100%"
        value={value}
        onChange={(v) => onTextChange(v)}
      />
      <div style={{ border: '1px solid red', padding: 8, overflowY: 'auto' }}>
        <ReactMarkdownRender parser={parse} blocks={blocks} />
      </div>
    </div>
  );
}
