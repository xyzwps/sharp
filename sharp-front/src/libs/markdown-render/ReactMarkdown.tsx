import { customParser, defaultBlockRules, defaultInlineRules } from './parser';
import ReactMarkdownRender from './ReactMarkdownRender';

type Props = {
  text: string;
};

export const parse = customParser(defaultBlockRules, defaultInlineRules, ['panel']);

export default function ReactMarkdown({ text }: Props) {
  return <ReactMarkdownRender parser={parse} blocks={parse(text)} />;
}
