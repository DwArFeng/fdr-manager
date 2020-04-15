package com.dwarfeng.fdrmanager.stack.service;

import com.dwarfeng.fdr.stack.bean.entity.RealtimeValue;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

/**
 * 实时值响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface RealtimeValueResponseService extends Service {

    /**
     * 获取服务中是否有指定的实时值。
     *
     * @param key 指定的实时值的键。
     * @return 服务中是否有指定的实时值。
     * @throws ServiceException 服务异常。
     */
    boolean exists(LongIdKey key) throws ServiceException;

    /**
     * 获取服务中指定的主键对应的实时值。
     *
     * @param key 指定的主键。
     * @return 指定的主键对应的实时值。
     * @throws ServiceException 服务异常。
     */
    RealtimeValue get(LongIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的实时值。
     *
     * @param realtimeValue 指定的实时值。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    LongIdKey insert(RealtimeValue realtimeValue) throws ServiceException;

    /**
     * 向服务中更新指定的实时值。
     *
     * @param realtimeValue 指定的实时值。
     * @throws ServiceException 服务异常。
     */
    void update(RealtimeValue realtimeValue) throws ServiceException;

    /**
     * 从服务中删除指定的主键对应的实时值。
     *
     * @param key 指定的主键。
     * @throws ServiceException 服务异常。
     */
    void delete(LongIdKey key) throws ServiceException;

    /**
     * 从服务中获取所有的实时值。
     *
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的实时值。
     * @throws ServiceException 服务异常。
     */
    PagedData<RealtimeValue> all(PagingInfo pagingInfo) throws ServiceException;
}
