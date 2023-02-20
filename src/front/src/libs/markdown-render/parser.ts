//#region lines

export type TextLine = { no: number; text: string }

export type Line = 'EOF' | TextLine

export const isEmptyLine = (line: TextLine) => /^\s*$/.test(line.text);

export const isSpace = (code: number) => code === 0x09 /* \t */ || code === 0x20; /* space */

export interface LineReader {
  nextLine(): Line
  giveBack(line: TextLine): void
}

class SimpleLineReader implements LineReader {
  private currentLineNo: number;
  private lines: string[];

  constructor(lines: string[], currentLineNo: number) {
    this.lines = lines;
    this.currentLineNo = currentLineNo;
  }

  nextLine(): Line {
    const nextLineNo = this.currentLineNo + 1;
    if (nextLineNo < this.lines.length) {
      this.currentLineNo++;
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      return { no: this.currentLineNo, text: this.lines[this.currentLineNo] };
    } else {
      return 'EOF';
    }
  }

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  giveBack(line: TextLine): void {
    if (this.currentLineNo >= 0) {
      this.currentLineNo--;
    }
  }
}

export const createLineReaderFromText = (text: string): LineReader => new SimpleLineReader(text.split(/\r\n?|\n/), -1);

export const createLineReaderFromLines = (lines: string[]): LineReader => new SimpleLineReader(lines, -1);

//#endregion

//#region rule interface

export interface Inline {
  readonly type: string
}

export type InlineRuleParseResult = false | { start: number; end: number; inline: Inline }

export type InlineRule = {
  /**
   * @param line line to parse
   * @param start start position, included, start from 0
   * @param end end position, excluded, always greater than or equal to `start`
   * @param parseInline parse nested inline code
   */
  parse: (line: TextLine, start: number, end: number, parseInline: ParseInlineFn) => InlineRuleParseResult
}

export interface Block {
  readonly type: string
}

export type BlockRule = {
  /**
   * @param lines line reader
   * @param parseInline parse textline to inline elements
   * @returns a `Block` of a `false` if this rule is not matched
   */
  parse: (lines: LineReader, parseInline: ParseInlineFn, parseBlock: BlockParserFn) => false | Block
}

//#endregion

//#region blocks

export interface CodeBlock extends Block {
  type: 'code'
  codes: string[]
  lang?: string
}

export const newCodeBlock = (codes: string[], lang?: string): CodeBlock => ({ type: 'code', codes, lang });

export interface HeadingBlock extends Block {
  type: 'heading'
  depth: number
  content: Inline[]
}

export const newHeading = (depth: number, content: Inline[]): HeadingBlock => ({ type: 'heading', depth, content });

export interface MathBlock extends Block {
  type: 'math'
  codes: string[]
}

export const newMathBlock = (codes: string[]): MathBlock => ({ type: 'math', codes });

export interface ParagraphBlock extends Block {
  type: 'paragraph'
  content: Inline[]
}

export const newParagraph = (content: Inline[]): ParagraphBlock => ({ type: 'paragraph', content });

export interface ListItemBlock extends Block {
  type: 'list-item'
  blocks: Block[]
}

export const newListItem = (blocks: Block[]): ListItemBlock => ({ type: 'list-item', blocks });

export interface ListBlock extends Block {
  type: 'list'
  ordered: boolean
  items: ListItemBlock[]
}

export const newOrderedList = (items: ListItemBlock[]): ListBlock => ({ type: 'list', ordered: true, items });
export const newUnorderedList = (items: ListItemBlock[]): ListBlock => ({ type: 'list', ordered: false, items });

type Attrs = Record<string, unknown>
export interface BlockMacro extends Block {
  type: 'macro'
  dsl: string[]
  name: string
  attrs: Attrs
}

export const newBlockMacro = (name: string, dsl: string[], attrs: Attrs): BlockMacro => ({
  type: 'macro',
  dsl,
  name,
  attrs,
});

//#endregion

//#region inlines

export interface Bold extends Inline {
  type: 'bold'
  content: Inline[]
}

export const newBold = (content: Inline[]): Bold => ({ type: 'bold', content });

export interface Italic extends Inline {
  type: 'italic'
  content: Inline[]
}

export const newItalic = (content: Inline[]): Italic => ({
  type: 'italic',
  content,
});

export interface Link extends Inline {
  type: 'link'
  link: string
  content: Inline[]
}

export const newLink = (content: Inline[], link: string): Link => ({
  type: 'link',
  link,
  content,
});

export interface InlineCode extends Inline {
  type: 'inline-code'
  code: string
}

export const newInlineCode = (code: string): InlineCode => ({
  type: 'inline-code',
  code,
});

export interface InlineMath extends Inline {
  type: 'inline-math'
  code: string
}

export const newInlineMath = (code: string): InlineMath => ({
  type: 'inline-math',
  code,
});

export interface LineBreak extends Inline {
  type: 'line-break'
}

export const newLineBreak = (): LineBreak => ({ type: 'line-break' });

export interface Todo extends Inline {
  type: 'todo'
}

export const newTodo = (): Todo => ({ type: 'todo' });

export interface Fixme extends Inline {
  type: 'fixme'
}

export const newFixme = (): Fixme => ({ type: 'fixme' });

export interface InlineText extends Inline {
  type: 'text'
  text: string
}

export const newText = (text: string): InlineText => ({ type: 'text', text });

//#endregion

//#region block rules

// FIXME: 优化
const countSharps = (text: string): number => {
  const trimEndText = text.trimEnd();
  if (trimEndText.startsWith('# ')) return 1;
  if (trimEndText.startsWith('## ')) return 2;
  if (trimEndText.startsWith('### ')) return 3;
  if (trimEndText.startsWith('#### ')) return 4;
  if (trimEndText.startsWith('##### ')) return 5;
  if (trimEndText.startsWith('###### ')) return 6;
  return 0;
};

export const atxHeadingRule: BlockRule = {
  parse: (lines: LineReader, parseInline: ParseInlineFn): false | Block => {
    const line = lines.nextLine();
    if (line == 'EOF') return false;

    const { text } = line;

    const start = 0;
    const sharpCount = countSharps(text);
    if (sharpCount <= 0 || sharpCount > 6) {
      lines.giveBack(line);
      return false;
    }

    if (!isSpace(text.charCodeAt(start + sharpCount))) {
      lines.giveBack(line);
      return false; // sharps should be followed by spaces
    }

    if (text.length - start <= sharpCount + 1) {
      return false;
    }

    return newHeading(sharpCount, parseInline(line, start + sharpCount + 1, text.length));
  },
};

const codeLang = ({ text }: TextLine): string | undefined => {
  const tail = text.substring(3).trim();
  return tail.length === 0 ? undefined : tail;
};

type TestBoundaryFn = (line: TextLine) => boolean

type BoundaryRuleConfig = {
  isFirstBoundary: TestBoundaryFn
  isLastBoundary: TestBoundaryFn
  makeBlocks: (contentLines: TextLine[], firstBoundary: TextLine) => false | Block
}

export const blockBoundaryRuleTemplate = (conf: BoundaryRuleConfig): BlockRule => {
  return {
    parse: function (lines: LineReader): false | Block {
      const firstLine = lines.nextLine();
      if (firstLine == 'EOF') return false;

      if (!conf.isFirstBoundary(firstLine)) {
        lines.giveBack(firstLine);
        return false;
      }

      const contentLines: TextLine[] = [];

      while (true) {
        const line = lines.nextLine();
        if (line === 'EOF') break;
        if (conf.isLastBoundary(line)) break;
        contentLines.push(line);
      }

      return conf.makeBlocks(contentLines, firstLine);
    },
  };
};

export const fencedCodeRule = blockBoundaryRuleTemplate({
  isFirstBoundary: ({ text }: TextLine) => /^`{3}\w*\s*$/.test(text),
  isLastBoundary: ({ text }: TextLine) => /^`{3}\s*$/.test(text),
  makeBlocks: (contentLines: TextLine[], firstBoundary: TextLine) =>
    newCodeBlock(
      contentLines.map((it) => it.text),
      codeLang(firstBoundary)
    ),
});

export const mathRule = ((isBoundary: TestBoundaryFn) =>
  blockBoundaryRuleTemplate({
    isFirstBoundary: isBoundary,
    isLastBoundary: isBoundary,
    makeBlocks: (contentLines: TextLine[]) => newMathBlock(contentLines.map((it) => it.text)),
  }))(({ text }: TextLine) => /^\${3}\s*$/.test(text));

const parseAttrs = (str: string): Attrs => {
  str = str.trim();
  if (str.length == 0) return {};

  return str
    .split(';')
    .map((it) => it.trim())
    .filter((it) => it.length > 0)
    .reduce((prevAttrs: Attrs, seg: string) => {
      const mid = [seg.indexOf(':'), seg.indexOf('=')] //
        .filter((it) => it >= 0)
        .reduce((prev, curr) => (prev > curr ? curr : prev), 100000000);
      const namePart = seg.substring(0, mid).trim();
      const valuePart = seg.substring(mid + 1).trim();
      if (namePart && valuePart) {
        prevAttrs[namePart] = valuePart;
      }
      return prevAttrs;
    }, {});
};

export const blockMacroRuleTemplate = (macroName: string) => {
  const macroStart = /^\{[\w-]+(\s[\w\W]*)?}\s*$/;
  const macroEnd = /^\{[\w-]+\}\s*$/;

  return blockBoundaryRuleTemplate({
    isFirstBoundary: ({ text }: TextLine) => macroStart.test(text) && text.startsWith('{' + macroName),
    isLastBoundary: ({ text }: TextLine) => macroEnd.test(text) && text.startsWith('{' + macroName + '}'),
    makeBlocks: (contentLines: TextLine[], { text }: TextLine) =>
      newBlockMacro(
        macroName,
        contentLines.map((it) => it.text),
        parseAttrs(text.substring(macroName.length + 1, text.lastIndexOf('}')))
      ),
  });
};

const listRuleTemplate = (listItemPrefix: '- ' | '+ '): BlockRule => {
  const isItemLine = ({ text }: TextLine) => text.startsWith(listItemPrefix);
  return {
    parse: function (lines: LineReader, _pi: ParseInlineFn, parseBlock: BlockParserFn): false | Block {
      const firstLine = lines.nextLine();
      if (firstLine === 'EOF') return false;

      const listItems: ListItemBlock[] = [];

      lines.giveBack(firstLine);
      while (true) {
        const line = lines.nextLine();
        if (line === 'EOF') break;

        if (isItemLine(line)) {
          //  collect items
          const itemLines: TextLine[] = [{ no: line.no, text: line.text.substring(2) }];
          while (true) {
            const line = lines.nextLine();
            if (line === 'EOF') break;

            if (isEmptyLine(line)) {
              itemLines.push({ no: line.no, text: '' });
            } else if (line.text.startsWith('  ')) {
              itemLines.push({ no: line.no, text: line.text.substring(2) });
            } else {
              lines.giveBack(line);
              break;
            }
          }

          listItems.push(newListItem(parseBlock(createLineReaderFromLines(itemLines.map((it) => it.text)))));
        } else {
          lines.giveBack(line);
          break;
        }
      }

      if (listItems.length === 0) return false;

      return (listItemPrefix == '- ' ? newUnorderedList : newOrderedList)(listItems);
    },
  };
};

export const unorderedlistRule = listRuleTemplate('- ');
export const orderedlistRule = listRuleTemplate('+ ');

//#endregion

//#region inline rules

// FIXME: 优化
export const boldItalicRule: InlineRule = {
  parse: function (line: TextLine, start: number, end: number, parseInline): InlineRuleParseResult {
    const { text } = line;
    if (end - start > 6 && text.startsWith('***', start)) {
      const next = text.indexOf('***', start + 4);
      if (next > 0) {
        return {
          start,
          end: next + 3,
          inline: newBold([newItalic(parseInline(line, start + 3, next))]),
        };
      }
    }

    if (end - start > 4 && text.startsWith('**', start)) {
      const next = text.indexOf('**', start + 3);
      if (next > 0) {
        return {
          start,
          end: next + 2,
          inline: newBold(parseInline(line, start + 2, next)),
        };
      }
    }

    if (end - start > 2 && text.startsWith('*', start)) {
      const next = text.indexOf('*', start + 2);
      if (next > 0) {
        return {
          start,
          end: next + 1,
          inline: newItalic(parseInline(line, start + 1, next)),
        };
      }
    }

    return false;
  },
};

type MakeInlineFn = (line: TextLine, start: number, end: number) => Inline

export function boundaryInlineRuleTemplate(boundaryCodePoint: number, makeInline: MakeInlineFn): InlineRule {
  const BOUNDARY = boundaryCodePoint;
  return {
    parse: function (line: TextLine, start: number, end: number): InlineRuleParseResult {
      const { text } = line;
      const firstCodePoint = text.charCodeAt(start);
      if (firstCodePoint !== BOUNDARY) return false;

      type Index = false | number

      let lastIndex: Index = false;
      for (let i = start + 1; i < end; i++) {
        const code = text.charCodeAt(i);

        if (code == BOUNDARY) {
          if (lastIndex === false) {
            lastIndex = i;
            break;
          } // else // impossible
        }
      }
      if (typeof lastIndex === 'number') {
        if (start == lastIndex - 1) {
          return false;
        }

        return {
          start,
          end: lastIndex + 1,
          inline: makeInline(line, start, lastIndex + 1),
        };
      } else {
        return false;
      }
    },
  };
}

export const inlineCodeRule = boundaryInlineRuleTemplate(0x60 /* ` */, (line, start, end) =>
  newInlineCode(line.text.substring(start + 1, end - 1))
);

export const inlineMathRule = boundaryInlineRuleTemplate(0x24 /* $ */, (line, start, end) =>
  newInlineMath(line.text.substring(start + 1, end - 1))
);

export const tagInlineRuleTemplate = (tag: string, newInline: () => Inline): InlineRule => {
  return {
    parse: function (line: TextLine, start: number, end: number): InlineRuleParseResult {
      const { text } = line;
      const t1 = tag + ': ';
      if (end - start >= t1.length && text.startsWith(t1, start)) {
        return {
          start,
          end: start + t1.length,
          inline: newInline(),
        };
      }

      const t2 = tag + ':';
      if (end - start >= t2.length && text.startsWith(t2, start)) {
        return {
          start,
          end: start + t2.length,
          inline: newInline(),
        };
      }

      return false;
    },
  };
};

export const todoRule: InlineRule = tagInlineRuleTemplate('TODO', newTodo);

export const fixmeRule: InlineRule = tagInlineRuleTemplate('FIXME', newFixme);

export const linkRule: InlineRule = {
  // [text](url)
  parse: function (line: TextLine, start: number, _end: number, parseInline): InlineRuleParseResult {
    const { text } = line;
    if (!text.startsWith('[', start)) return false;

    const mid = text.indexOf('](', start + 1);
    if (mid < 0) return false;

    const last = text.indexOf(')', mid + 2);
    if (last < 0) return false;

    return {
      start,
      end: last + 1,
      inline: newLink(parseInline(line, start + 1, mid), text.substring(mid + 2, last)),
    };
  },
};

//#endregion

//#region parser

type ParseStates = {
  paragraphLines: TextLine[]
  blocks: Block[]
}

export type ParseInlineFn = (line: TextLine, start: number, end: number) => Inline[]

// TODO: 处理转义字符
const createInlineParser = (rules: InlineRule[]): ParseInlineFn => {
  const parseInline = (line: TextLine, start: number, end: number): Inline[] => {
    const { text } = line;

    const results: Inline[] = [];
    const textPos = { start: -1, count: 0 };

    for (let i = start; i < end; ) {
      let result: InlineRuleParseResult = false;

      for (const rule of rules) {
        result = rule.parse(line, i, end, parseInline);
        if (result !== false) break; // matched
      }

      if (result === false) {
        if (textPos.start < 0) {
          textPos.start = i;
          textPos.count = 1;
        } else {
          textPos.count++;
        }
        i++;
      } else {
        // collect prev text
        if (textPos.start >= 0) {
          results.push(newText(text.substring(textPos.start, textPos.start + textPos.count)));
          textPos.start = -1;
        }

        results.push(result.inline);
        i = result.end;
      }
    }

    if (textPos.start >= 0) {
      results.push(newText(text.substring(textPos.start, textPos.start + textPos.count)));
    }

    return results;
  };
  return parseInline;
};

export type BlockParserFn = (lines: LineReader) => Block[]

const createBlockParser = (rules: BlockRule[], parseInline: ParseInlineFn): BlockParserFn => {
  const parseBlock = (lines: LineReader): Block[] => {
    const states: ParseStates = {
      paragraphLines: [],
      blocks: [],
    };

    while (true) {
      const line = lines.nextLine();
      if (line === 'EOF') {
        break; // end
      }

      if (isEmptyLine(line)) {
        makeParagraphs(states);
        continue;
      }

      lines.giveBack(line);
      let blockRuleMatched = false;
      for (const rule of rules) {
        const result = rule.parse(lines, parseInline, parseBlock);
        if (result !== false) {
          makeParagraphs(states);
          states.blocks.push(result);
          blockRuleMatched = true;
          break;
        }
      } // end for

      if (!blockRuleMatched) {
        const line = lines.nextLine();
        if (line === 'EOF') {
          break; // end
        } else {
          states.paragraphLines.push(line);
        }
      }
    }
    makeParagraphs(states);
    return states.blocks;
  };

  return parseBlock;

  function makeParagraphs(states: ParseStates) {
    const { paragraphLines, blocks } = states;
    if (paragraphLines.length == 0) return;

    const content: Inline[] = [];
    let first = true;
    for (const line of paragraphLines) {
      if (first) first = false;
      else content.push(newLineBreak());

      content.push(...parseInline(line, 0, line.text.length));
    }

    blocks.push(newParagraph(content));
    states.paragraphLines = [];
  }
};

export const customParser = (blockRules: BlockRule[], inlineRules: InlineRule[], blockMacroNames: string[]) => {
  const parseInline = createInlineParser(inlineRules);
  const parseBlock = createBlockParser(
    [...blockRules, ...blockMacroNames.filter((it) => it).map((it) => blockMacroRuleTemplate(it))],
    parseInline
  );
  return (md: string): Block[] => parseBlock(createLineReaderFromText(md));
};

export const defaultBlockRules: BlockRule[] = [atxHeadingRule, fencedCodeRule, unorderedlistRule, orderedlistRule, mathRule];
export const defaultInlineRules: InlineRule[] = [inlineCodeRule, inlineMathRule, boldItalicRule, todoRule, fixmeRule, linkRule];

export const parser = customParser(defaultBlockRules, defaultInlineRules, []);
