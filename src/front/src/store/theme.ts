import { create } from 'zustand';

type ThemeName = 'dark' | 'light';

type ThemeStore = {
  theme: ThemeName;
  setTheme: (theme: ThemeName) => void;
};

export const useThemeStore = create<ThemeStore>((set, get) => ({
  theme: 'light',
  setTheme: (theme) => set({ theme }),
}));
