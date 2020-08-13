import {
  del, get, patch, post,
} from '@/util/http';

export function exists(key) {
  return get(`/persistence-value/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/persistence-value/${key}`, {});
}

export function insert(key, pointKey, happenedDate, value) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/persistence-value', {
    key: finalKey,
    point_key: pointKey,
    happened_date: happenedDate,
    value,
  });
}

export function remove(key) {
  return del(`/persistence-value/${key}`, {});
}

export function update(key, pointKey, happenedDate, value) {
  return patch('/persistence-value', {
    key: {
      long_id: key,
    },
    point_key: pointKey,
    happened_date: happenedDate,
    value,
  });
}

export function all(page, rows) {
  return get('/persistence-value/all', {
    page,
    rows,
  });
}

export function allBetween(startDate, endDate, page, rows) {
  return get('/persistence-value/all/between', {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForPoint(key, page, rows) {
  return get(`/point/${key}/persistence-value`, {
    page,
    rows,
  });
}

export function childForPointBetween(key, startDate, endDate, page, rows) {
  return get(`/point/${key}/persistence-value/between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}
