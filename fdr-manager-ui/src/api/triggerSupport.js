import { get } from '@/util/http';

export function exists(key) {
  return get(`/trigger-support/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/trigger-support/${key}`, {});
}

export function all(page, rows) {
  return get('/trigger-support/all', {
    page,
    rows,
  });
}

export function idLike(pattern, page, rows) {
  return get('/trigger-support/id-like', {
    pattern,
    page,
    rows,
  });
}

export function labelLike(pattern, page, rows) {
  return get('/trigger-support/label-like', {
    pattern,
    page,
    rows,
  });
}
