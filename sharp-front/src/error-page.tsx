import { useRouteError } from 'react-router-dom';
import { StandardError } from './apis/http';

// TODO: 美化错误信息
export default function ErrorPage() {
  const error = useRouteError();

  if (error instanceof StandardError) {
    return (
      <div id="error-page">
        <h1>Oops!</h1>
        <StandardErrorAlert error={error} />
      </div>
    );
  }

  return (
    <div id="error-page">
      <h1>Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p>
        <i>{((err) => err.statusText || err.message)(error as { statusText?: string; message?: string })}</i>
      </p>
    </div>
  );
}

function StandardErrorAlert({ error }: { error: StandardError }) {
  const s = error.standard;
  return (
    <div>
      <ul>
        <li>错误类型: {s.type}</li>
        <li>错误简述: {s.title}</li>
        <li>错误描述: {s.details}</li>
      </ul>
    </div>
  );
}
