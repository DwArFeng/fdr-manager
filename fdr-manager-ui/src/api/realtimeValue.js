import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/realtime-value/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/realtime-value/${key}`, {});
}

export function insert(key, name, remark, realtimeEnabled, persistenceEnabled) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/realtime-value', {
    key: finalKey,
    remark,
    name,
    persistence_enabled: persistenceEnabled,
    realtime_enabled: realtimeEnabled,
  });
}

export function remove(key) {
  return del(`/realtime-value/${key}`, {});
}

export function update(key, name, remark, realtimeEnabled, persistenceEnabled) {
  return patch('/realtime-value', {
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
  return get('/realtime-value/all', {
    page,
    rows,
  });
}
