import { Block, Inline } from '../parser';

type Options = {
  renderInlines: (inlines: Inline[]) => JSX.Element;
  renderBlocks: (blocks: Block[]) => JSX.Element;
  renderMd: (md: string) => JSX.Element;
};

interface BlockRender {
  (block: Block, opts: Options): JSX.Element;
}

export default BlockRender;
