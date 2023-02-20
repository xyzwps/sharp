import { Link } from 'react-router-dom';
import { Link as InlineLink } from '../parser';
import InlineRender from './InlineRender';

const linkRender: InlineRender = (inline, renderInlines) => {
  if (inline.type == 'link') {
    const { content, link } = inline as InlineLink;
    return <Link to={link}>{renderInlines(content)}</Link>;
  }

  throw new Error(`link is required, but was a ${inline.type}`);
};

export default linkRender;
