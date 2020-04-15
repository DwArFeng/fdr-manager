import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/triggered-value/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/triggered-value/${key}`, {});
}

export function insert(key, pointKey, happenedDate, value) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/triggered-value', {
    key: finalKey,
    point_key: pointKey,
    happened_date: happenedDate,
    value,
  });
}

export function remove(key) {
  return del(`/triggered-value/${key}`, {});
}

export function update(key, pointKey, happenedDate, value) {
  return patch('/triggered-value', {
    key: {
      long_id: key,
    },
    point_key: pointKey,
    happened_date: happenedDate,
    value,
  });
}

export function all(page, rows) {
  return get('/triggered-value/all', {
    page,
    rows,
  });
}

export function allBetween(startDate, endDate, page, rows) {
  return get('/triggered-value/all/between', {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForPoint(key, page, rows) {
  return get(`/point/${key}/triggered-value`, {
    page,
    rows,
  });
}

export function childForPointBetween(key, startDate, endDate, page, rows) {
  return get(`/point/${key}/triggered-value/between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}

export function childForTrigger(key, page, rows) {
  return get(`/trigger/${key}/triggered-value`, {
    page,
    rows,
  });
}

export function childForTriggerBetween(key, startDate, endDate, page, rows) {
  return get(`/trigger/${key}/triggered-value/between`, {
    'start-date': startDate,
    'end-date': endDate,
    page,
    rows,
  });
}
