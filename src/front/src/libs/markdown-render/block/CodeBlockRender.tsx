import { CodeBlock } from '../parser';
import BlockRender from './BlockRender';
import { Prism } from '@mantine/prism';
import { Language } from 'prism-react-renderer';

const codeBlockRender: BlockRender = (block) => {
  if (block.type === 'code') {
    const { lang, codes } = block as CodeBlock;
    const codeText = codes.join('\n');
    return (
      <Prism withLineNumbers language={lang as Language} my={8}>
        {codeText}
      </Prism>
    );
  }

  throw new Error(`code is required, but was a ${block.type}`);
};

export default codeBlockRender;
