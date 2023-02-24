import { Inline, InlineMath } from '../parser';
import InlineRender from './InlineRender';
import katex from 'katex';

import 'katex/dist/katex.min.css';

const inlineMathRender: InlineRender = (inline: Inline) => {
  if (inline.type == 'inline-math') {
    const html = katex.renderToString((inline as InlineMath).code, { throwOnError: false });
    return <span dangerouslySetInnerHTML={{ __html: html }}></span>;
  }

  throw new Error(`inline-math is required, but was a ${inline.type}`);
};

export default inlineMathRender;
