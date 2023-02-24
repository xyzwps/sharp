import { Inline } from '../parser';
import InlineRender from './InlineRender';

const lineBreakRender: InlineRender = (inline: Inline) => {
  if (inline.type == 'line-break') return <> </>;

  throw new Error(`line-break is required, but was a ${inline.type}`);
};

export default lineBreakRender;
