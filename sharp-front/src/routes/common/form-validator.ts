export interface ValidateFn<V> {
  (v: V): string | null;
}

export function rules<V>(...vfns: ValidateFn<V>[]): ValidateFn<V> {
  return (v) => {
    for (const fn of vfns) {
      const r = fn(v);
      if (r != null) return r;
    }
    return null;
  };
}

export const s = {
  minLength:
    (min: number, msg: string): ValidateFn<string> =>
      (v) =>
        v.length < min ? msg : null,
  maxLength:
    (max: number, msg: string): ValidateFn<string> =>
      (v) =>
        v.length > max ? msg : null,
  pattern:
    (regexp: RegExp, msg: string): ValidateFn<string> =>
      (v) =>
        regexp.test(v) ? null : msg,
};
