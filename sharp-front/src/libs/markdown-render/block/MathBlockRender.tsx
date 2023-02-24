import { MathBlock } from '../parser';
import BlockRender from './BlockRender';
import katex from 'katex';

import 'katex/dist/katex.min.css';

const mathBlockRender: BlockRender = (block) => {
  if (block.type === 'math') {
    const { codes } = block as MathBlock;
    const html = katex.renderToString(codes.join('\n'), { throwOnError: false });
    return <div dangerouslySetInnerHTML={{ __html: html }}></div>;
  }

  throw new Error(`math is required, but was a ${block.type}`);
};

export default mathBlockRender;
