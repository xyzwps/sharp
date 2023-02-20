import ky, { HTTPError, SearchParamsOption } from 'ky';

export type StandardErrorData = {
  type: string;
  title: string;
  details: string;
};

export class StandardError extends Error {
  public standard: StandardErrorData;
  constructor(data: StandardErrorData) {
    super(data.title);
    this.standard = data;
  }
}

const TIMEOUT_DEFAULT = 2000;

type HttpOptions =
  | { method?: 'get'; url: string; qs?: SearchParamsOption }
  | { method: 'post'; url: string; json?: unknown }
  | { method: 'patch'; url: string; json?: unknown }
  | { method: 'put'; url: string; json?: unknown }
  | { method: 'delete'; url: string };

const sleep = (ms: number) =>
  new Promise<void>((resolve) => {
    const t = setTimeout(() => {
      resolve();
      clearTimeout(t);
    }, ms);
  });

export async function request<T = unknown>(options: HttpOptions): Promise<T> {
  await sleep(2000); // TODO: 注掉
  try {
    const { method, url } = options;
    switch (method) {
      case undefined:
      case 'get':
        return await ky.get(url, { timeout: TIMEOUT_DEFAULT, retry: { limit: 0 }, searchParams: options.qs }).json();
      case 'post':
        return await ky.post(url, { timeout: TIMEOUT_DEFAULT, json: options.json }).json();
      case 'patch':
        return await ky.patch(url, { timeout: TIMEOUT_DEFAULT, json: options.json }).json();
      case 'put':
        return await ky.put(url, { timeout: TIMEOUT_DEFAULT, json: options.json }).json();
      case 'delete':
        return await ky.delete(url, { timeout: TIMEOUT_DEFAULT }).json();
      default:
        throw new Error("Unsupported http method for request. It's a bug during development stage.");
    }
  } catch (err) {
    if (err instanceof HTTPError) {
      const contentType = err.response.headers.get('content-type');
      if (contentType && contentType.toLocaleLowerCase().includes('application/json')) {
        const data = (await err.response.json()) as StandardErrorData;
        throw new StandardError(data);
      }
    }
    throw err;
  }
}
