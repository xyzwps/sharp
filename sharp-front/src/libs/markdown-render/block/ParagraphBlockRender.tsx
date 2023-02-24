import { ParagraphBlock } from '../parser';
import BlockRender from './BlockRender';

const paragraphBlockRender: BlockRender = (block, { renderInlines }) => {
  if (block.type == 'paragraph') {
    const { content } = block as ParagraphBlock;
    return <p>{renderInlines(content)}</p>;
  }
  throw new Error(`paragraph is required, but was a ${block.type}`);
};

export default paragraphBlockRender;
