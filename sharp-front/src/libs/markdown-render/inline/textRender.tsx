import { Inline, InlineText } from '../parser';
import InlineRender from './InlineRender';

const textRender: InlineRender = (inline: Inline) => {
  if (inline.type == 'text') return <>{(inline as InlineText).text}</>;

  throw new Error(`${Text.name} is required, but was a ${inline.type}`);
};

export default textRender;
