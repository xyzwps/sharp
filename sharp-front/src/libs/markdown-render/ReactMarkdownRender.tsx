import { Block, Inline } from './parser';

import InlineRender from './inline/InlineRender';
import inlineCodeRender from './inline/InlineCodeRender';
import inlineMathRender from './inline/InlineMathRender';
import lineBreakRender from './inline/LineBreakRender';
import BlockRender from './block/BlockRender';
import paragraphBlockRender from './block/ParagraphBlockRender';
import codeBlockRender from './block/CodeBlockRender';
import headingBlockRender from './block/HeadlingBlockRender';
import mathBlockRender from './block/MathBlockRender';
import listBlockRender from './block/ListBlockRender';
import boldRender from './inline/boldRender';
import italicRender from './inline/italicRender';
import textRender from './inline/textRender';
import macroRender from './block/MacroRender';
import todoRender from './inline/todoRender';
import fixmeRender from './inline/fixmeRender';
import linkRender from './inline/linkRender';

type Parser = (md: string) => Block[];

type Props = {
  blocks: Block[];
  parser: Parser;
};

const IRS: { [type: string]: InlineRender } = {
  bold: boldRender,
  fixme: fixmeRender,
  italic: italicRender,
  'inline-code': inlineCodeRender,
  'inline-math': inlineMathRender,
  'line-break': lineBreakRender,
  link: linkRender,
  text: textRender,
  todo: todoRender,
};

const BRS: { [type: string]: BlockRender } = {
  code: codeBlockRender,
  heading: headingBlockRender,
  list: listBlockRender,
  macro: macroRender,
  math: mathBlockRender,
  paragraph: paragraphBlockRender,
};

export default function ReactMarkdownRender({ blocks, parser }: Props) {
  return (
    <>
      {blocks.map((it, i) => (
        <SingleBlockRender
          key={i}
          block={it}
          renderMd={(md) => <ReactMarkdownRender blocks={parser(md)} parser={parser} />}
          renderBlocks={(it) => <ReactMarkdownRender parser={parser} blocks={it} />}
        />
      ))}
    </>
  );
}

const renderInlines = (inlines: Inline[]) => (
  <>
    {inlines.map((it, i) => (
      <SingleInlineRender key={i} inline={it} />
    ))}
  </>
);

function SingleBlockRender({
  block,
  renderBlocks,
  renderMd,
}: {
  block: Block;
  renderBlocks: (blocks: Block[]) => JSX.Element;
  renderMd: (md: string) => JSX.Element;
}) {
  if (block.type in BRS) {
    return BRS[block.type](block, { renderInlines, renderBlocks, renderMd });
  } else {
    return <pre>Unsupported Block Element: {JSON.stringify(block, null, '  ')}</pre>;
  }
}

function SingleInlineRender({ inline }: { inline: Inline }) {
  if (inline.type in IRS) {
    return IRS[inline.type](inline, renderInlines);
  } else {
    return <pre>Unsupporetd Inline Element: {JSON.stringify(inline, null, '  ')}</pre>;
  }
}
