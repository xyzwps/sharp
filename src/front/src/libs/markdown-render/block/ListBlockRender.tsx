import React from 'react';
import { ListBlock } from '../parser';
import BlockRender from './BlockRender';

const listBlockRender: BlockRender = (block, { renderBlocks }) => {
  if (block.type === 'list') {
    const { ordered, items } = block as ListBlock;

    return React.createElement(
      ordered ? 'ol' : 'ul',
      {},
      items.map((it, i) => <li key={i}>{renderBlocks(it.blocks)}</li>)
    );
  }

  throw new Error(`list is required, but was a ${block.type}`);
};

export default listBlockRender;
