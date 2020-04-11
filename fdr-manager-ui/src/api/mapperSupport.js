import { get } from '../util/http';

export function exists(key) {
  return get(`/mapper-support/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/mapper-support/${key}`, {});
}

export function all(page, rows) {
  return get('/mapper-support/all', {
    page,
    rows,
  });
}

export function idLike(pattern, page, rows) {
  return get('/mapper-support/id-like', {
    pattern,
    page,
    rows,
  });
}

export function labelLike(pattern, page, rows) {
  return get('/mapper-support/label-like', {
    pattern,
    page,
    rows,
  });
}
