import { Bold, Inline } from '../parser';
import InlineRender from './InlineRender';

const boldRender: InlineRender = (inline, renderInlines) => {
  if (inline.type == 'bold') {
    const { content } = inline as Bold;
    return <b>{renderInlines(content)}</b>;
  }

  throw new Error(`bold is required, but was a ${inline.type}`);
};

export default boldRender;
