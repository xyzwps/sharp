import { Inline } from '../parser';

type InlineRender = (inline: Inline, renderInlines: (inlines: Inline[]) => JSX.Element) => JSX.Element;

export default InlineRender;
