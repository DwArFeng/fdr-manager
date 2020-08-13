import {
  del, get, patch, post,
} from '@/util/http';

export function exists(key) {
  return get(`/filtered-value/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/filtered-value/${key}`, {});
}

export function insert(key, pointKey, happenedDate, value) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/filtered-value', {
    key: finalKey,
    point_key: pointKey,
    happened_date: happenedDate,
    value,
  });
}

export function remove(key) {
  return del(`/filtered-value/${key}`, {});
}

export function update(key, pointKey, happenedDate, value) {
  return patch('/filtered-value', {
    key: {
      long_id: key,
    },
    point_key: pointKey,
    happened_date: happenedDate,
    value,
  });
}

export function all(page, rows) {
  return get('/filtered-value/all', {
    page,
    rows,
  });
}

export function allBetween(startDate, endDate, page, rows) {
  return get('/filtered-value/all/between', {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForPoint(key, page, rows) {
  return get(`/point/${key}/filtered-value`, {
    page,
    rows,
  });
}

export function childForPointBetween(key, startDate, endDate, page, rows) {
  return get(`/point/${key}/filtered-value/between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForFilter(key, page, rows) {
  return get(`/filter/${key}/filtered-value`, {
    page,
    rows,
  });
}

export function childForFilterBetween(key, startDate, endDate, page, rows) {
  return get(`/filter/${key}/filtered-value/between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}
