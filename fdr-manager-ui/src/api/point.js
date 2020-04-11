import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/point/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/point/${key}`, {});
}

export function insert(key, name, remark, realtimeEnabled, persistenceEnabled) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/point', {
    key: finalKey,
    remark,
    name,
    persistence_enabled: persistenceEnabled,
    realtime_enabled: realtimeEnabled,
  });
}

export function remove(key) {
  return del(`/point/${key}`, {});
}

export function update(key, name, remark, realtimeEnabled, persistenceEnabled) {
  return patch('/point', {
    key: {
      long_id: key,
    },
    remark,
    name,
    persistence_enabled: persistenceEnabled,
    realtime_enabled: realtimeEnabled,
  });
}

export function all(page, rows) {
  return get('/point/all', {
    page,
    rows,
  });
}

export function nameLike(pattern, page, rows) {
  return get('/point/name-like', {
    pattern,
    page,
    rows,
  });
}
