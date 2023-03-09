import { create } from 'zustand';
import { getProfile } from '../apis/profile';
import { login, LoginData } from '../apis/login';
import { logout } from '../apis/logout';
import { toastError } from '../error';

export type AuthStatus = { state: 'loading' } | { state: 'auth'; user: UserDetails } | { state: 'unauth' };

type AuthStore = {
  status: AuthStatus;
  login: (data: LoginData) => Promise<void>;
  logout: () => Promise<void>;
  load: () => void;
};

export const useAuthStore = create<AuthStore>((set, get) => ({
  status: { state: 'loading' },
  login: async (data: LoginData) => {
    if (get().status.state !== 'unauth') return;

    set({ status: { state: 'loading' } });
    try {
      const userDetails = await login(data);
      set({ status: { state: 'auth', user: userDetails } });
    } catch (err) {
      toastError(err, '登录失败');
      set({ status: { state: 'unauth' } });
    }
  },
  logout: async () => {
    set({ status: { state: 'loading' } });
    await logout();
    set({ status: { state: 'unauth' } });
  },
  load: () => {
    set({ status: { state: 'loading' } });
    getProfile()
      .then((user) => set({ status: { state: 'auth', user } }))
      .catch((err) => {
        set({ status: { state: 'unauth' } });
        console.error('尝试加载用户信息失败', err);
      });
  },
}));
