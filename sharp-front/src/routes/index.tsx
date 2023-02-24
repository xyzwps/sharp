import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import ErrorPage from '../error-page';
import CommonShell from './CommonShell';
import ProfilePage from './ProfilePage';
import Todos from './todos';
import SuspenseWrapper from './SuspenseWrapper';
import SinglePostPage, { loader as singlePostLoader } from './posts/SinglePostPage';
import EditPostPage from './posts/EditPostPage';
import MarkdownDemo from './demo/markdown';
import HomePage from './HomePage';

const router = createBrowserRouter([
  { path: '/demo/markdown', element: <MarkdownDemo /> },
  { path: '/a/create-post', element: <SuspenseWrapper factory={() => import('./posts/CreatePostPage')} /> },
  { path: '/posts/:id/edit', element: <EditPostPage />, errorElement: <ErrorPage />, loader: singlePostLoader },
  {
    path: '/',
    element: <CommonShell />,
    children: [
      { path: '', element: <HomePage /> },
      { path: 'a/login', element: <SuspenseWrapper factory={() => import('./LoginPage')} /> },
      { path: 'a/register', element: <SuspenseWrapper factory={() => import('./RegisterPage')} /> },
      { path: 'profile', element: <ProfilePage />, errorElement: <ErrorPage /> },
      { path: 'todos', element: <Todos />, errorElement: <ErrorPage /> },
      { path: 'posts/:id', element: <SinglePostPage />, errorElement: <ErrorPage />, loader: singlePostLoader },
    ],
  },
]);

export default function Entry() {
  return (
    <>
      <RouterProvider router={router} />
    </>
  );
}
