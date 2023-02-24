import { Badge } from '@mantine/core';
import { Inline } from '../parser';
import InlineRender from './InlineRender';

const todoRender: InlineRender = (inline: Inline) => {
  if (inline.type == 'todo')
    return (
      <Badge color="orange" radius="sm" variant="filled">
        TODO
      </Badge>
    );

  throw new Error(`todo is required, but was a ${inline.type}`);
};

export default todoRender;
