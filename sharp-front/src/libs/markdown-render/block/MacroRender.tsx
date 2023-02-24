import { BlockMacro } from '../parser';
import BlockRender from './BlockRender';

import 'prismjs/themes/prism.css';
import { Card } from '@mantine/core';

const macroRender: BlockRender = (block, { renderMd }) => {
  if (block.type === 'macro') {
    const { dsl, name, attrs } = block as BlockMacro;
    switch (name) {
      case 'panel':
        return (
          <Card withBorder shadow="sm">
            <div style={{ marginTop: 16 }}>{renderMd(dsl.join('\n'))}</div>
          </Card>
        );
      default:
        return <div>暂不支持的宏{name}</div>;
    }
  }

  throw new Error(`macro is required, but was a ${block.type}`);
};

export default macroRender;
