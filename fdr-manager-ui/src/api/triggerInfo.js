import {
  del, get, patch, post,
} from '../util/http';

export function exists(key) {
  return get(`/trigger-info/${key}/exists`, {});
}

export function inspect(key) {
  return get(`/trigger-info/${key}`, {});
}

export function insert(key, pointkey, enabled, remark, content, type) {
  let finalKey = { long_id: key };
  if (key === '') {
    finalKey = null;
  }
  return post('/trigger-info', {
    key: finalKey,
    pointKey: {
      long_id: pointkey,
    },
    enabled,
    remark,
    content,
    type,
  });
}

export function remove(key) {
  return del(`/trigger-info/${key}`, {});
}

export function update(key, pointkey, enabled, remark, content, type) {
  return patch('/trigger-info', {
    key: {
      long_id: key,
    },
    pointKey: {
      long_id: pointkey,
    },
    enabled,
    remark,
    content,
    type,
  });
}

export function all(page, rows) {
  return get('/trigger-info/all', {
    page,
    rows,
  });
}

export function childForPoint(key, page, rows) {
  return get(`/point/${key}/trigger-infos`, {
    key,
    page,
    rows,
  });
}
