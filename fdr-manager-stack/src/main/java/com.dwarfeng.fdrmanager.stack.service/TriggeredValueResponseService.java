package com.dwarfeng.fdrmanager.stack.service;

import com.dwarfeng.fdr.stack.bean.entity.TriggeredValue;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

import java.util.Date;

/**
 * 实时值响应服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface TriggeredValueResponseService extends Service {

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
    TriggeredValue get(LongIdKey key) throws ServiceException;

    /**
     * 向服务中插入指定的实时值。
     *
     * @param triggeredValue 指定的实时值。
     * @return 插入后分配的新主键。
     * @throws ServiceException 服务异常。
     */
    LongIdKey insert(TriggeredValue triggeredValue) throws ServiceException;

    /**
     * 向服务中更新指定的实时值。
     *
     * @param triggeredValue 指定的实时值。
     * @throws ServiceException 服务异常。
     */
    void update(TriggeredValue triggeredValue) throws ServiceException;

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
    PagedData<TriggeredValue> all(PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取指定时间区间内的所有的实时值。
     *
     * @param pagingInfo 分页信息。
     * @param startDate  开始时间。
     * @param endDate    结束时间。
     * @return 指定的数据点对应的实时值。
     * @throws ServiceException 服务异常。
     */
    PagedData<TriggeredValue> allBetween(Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取指定数据点对应的所有被触发数据值。
     *
     * @param pointKey   指定的数据点对应的主键。
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的被触发数据值。
     * @throws ServiceException 服务异常。
     */
    PagedData<TriggeredValue> childForPoint(LongIdKey pointKey, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取指定数据点对应的所有被触发数据值。
     *
     * @param pointKey   指定的数据点对应的主键。
     * @param startDate  开始时间。
     * @param endDate    结束时间。
     * @param pagingInfo 分页信息。
     * @return 指定的数据点对应的被触发数据值。
     * @throws ServiceException 服务异常。
     */
    PagedData<TriggeredValue> childForPointBetween(
            LongIdKey pointKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取指定触发器对应的所有被触发数据值。
     *
     * @param triggerKey 指定的触发器对应的主键。
     * @param pagingInfo 分页信息。
     * @return 指定的触发器对应的被触发数据值。
     * @throws ServiceException 服务异常。
     */
    PagedData<TriggeredValue> childForTrigger(LongIdKey triggerKey, PagingInfo pagingInfo) throws ServiceException;

    /**
     * 从服务中获取指定触发器对应的所有被触发数据值。
     *
     * @param triggerKey 指定的触发器对应的主键。
     * @param startDate  开始时间。
     * @param endDate    结束时间。
     * @param pagingInfo 分页信息。
     * @return 指定的触发器对应的被触发数据值。
     * @throws ServiceException 服务异常。
     */
    PagedData<TriggeredValue> childForTriggerBetween(
            LongIdKey triggerKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException;
}
