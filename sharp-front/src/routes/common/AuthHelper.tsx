import { Navigate } from 'react-router';

import { useAuthStore } from '../../store/auth';
import { Loader } from '@mantine/core';

type Props = {
  onAuth: (user: UserDetails) => JSX.Element;
  onUnauth: string | (() => JSX.Element);
  onLoading?: () => JSX.Element;
};

export default function AuthHelper({ onAuth, onUnauth, onLoading }: Props) {
  const authState = useAuthStore((state) => state.status);

  switch (authState.state) {
    case 'auth': {
      return onAuth(authState.user);
    }
    case 'unauth': {
      if (typeof onUnauth == 'string') {
        return <Navigate to={onUnauth} />;
      } else {
        return onUnauth();
      }
    }
    case 'loading': {
      if (onLoading) {
        return onLoading();
      } else {
        return <Loader size="xl" variant="bars" />;
      }
    }
    default:
      throw new Error('Unsupported auth state, maybe a bug');
  }
}
