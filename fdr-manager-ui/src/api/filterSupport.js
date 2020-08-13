import { get } from '@/util/http';

export function exists(key) {
  return get(`/filter-support/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/filter-support/${key}`, {});
}

export function all(page, rows) {
  return get('/filter-support/all', {
    page,
    rows,
  });
}

export function idLike(pattern, page, rows) {
  return get('/filter-support/id-like', {
    pattern,
    page,
    rows,
  });
}

export function labelLike(pattern, page, rows) {
  return get('/filter-support/label-like', {
    pattern,
    page,
    rows,
  });
}
