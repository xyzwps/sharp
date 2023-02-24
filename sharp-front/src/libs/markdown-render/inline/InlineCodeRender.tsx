import { Inline, InlineCode } from '../parser';
import InlineRender from './InlineRender';

const inlineCodeRender: InlineRender = (inline: Inline) => {
  if (inline.type == 'inline-code') return <code>{(inline as InlineCode).code}</code>;

  throw new Error(`inline-code is required, but was a ${inline.type}`);
};

export default inlineCodeRender;
