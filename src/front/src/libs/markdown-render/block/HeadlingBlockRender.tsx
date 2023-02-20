import React from 'react';
import { HeadingBlock } from '../parser';
import BlockRender from './BlockRender';

const H: Record<number, boolean> = { 1: true, 2: true, 3: true, 4: true, 5: true, 6: true };

const headingBlockRender: BlockRender = (block, { renderInlines }) => {
  if (block.type === 'heading') {
    const { content, depth } = block as HeadingBlock;
    const inlines = renderInlines(content);
    if (H[depth]) {
      return React.createElement(`h${depth}`, {}, inlines);
    } else {
      throw new Error(`Invalid heading depth ${depth}`);
    }
  }

  throw new Error(`heading is required, but was a ${block.type}`);
};

export default headingBlockRender;
