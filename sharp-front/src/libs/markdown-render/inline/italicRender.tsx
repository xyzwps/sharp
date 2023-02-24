import { Italic } from '../parser';
import InlineRender from './InlineRender';

const italicRender: InlineRender = (inline, renderInlines) => {
  if (inline.type == 'italic') return <i>{renderInlines((inline as Italic).content)}</i>;

  throw new Error(`italic is required, but was a ${inline.type}`);
};

export default italicRender;
