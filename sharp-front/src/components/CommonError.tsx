import { StandardError } from '../apis/http';

export default function CommonError(props: { error: Error }) {
  const { error } = props;
  if (error instanceof StandardError) {
    // TODO: 搞好一点
    return <pre>{JSON.stringify(error.standard, null, '  ')}</pre>;
  }

  return <div>Error: {error.message}</div>;
}
