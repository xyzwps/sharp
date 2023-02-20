import { Badge } from '@mantine/core';
import { Inline } from '../parser';
import InlineRender from './InlineRender';

const fixmeRender: InlineRender = (inline: Inline) => {
  if (inline.type == 'fixme')
    return (
      <Badge color="red" radius="sm" variant="filled">
        FIXME
      </Badge>
    );

  throw new Error(`fixme is required, but was a ${inline.type}`);
};

export default fixmeRender;
