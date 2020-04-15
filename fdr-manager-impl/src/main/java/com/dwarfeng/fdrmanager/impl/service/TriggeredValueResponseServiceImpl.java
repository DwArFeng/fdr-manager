package com.dwarfeng.fdrmanager.impl.service;

import com.dwarfeng.fdr.stack.bean.entity.TriggeredValue;
import com.dwarfeng.fdr.stack.service.TriggeredValueMaintainService;
import com.dwarfeng.fdrmanager.stack.service.TriggeredValueResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TriggeredValueResponseServiceImpl implements TriggeredValueResponseService {

    @Autowired
    @Qualifier("triggeredValueMaintainService")
    private TriggeredValueMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public TriggeredValue get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(TriggeredValue triggeredValue) throws ServiceException {
        return service.insert(triggeredValue);
    }

    @Override
    public void update(TriggeredValue triggeredValue) throws ServiceException {
        service.update(triggeredValue);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<TriggeredValue> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }

    @Override
    public PagedData<TriggeredValue> allBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                TriggeredValueMaintainService.BETWEEN,
                new Object[]{startDate, endDate},
                pagingInfo
        );
    }

    @Override
    public PagedData<TriggeredValue> childForPoint(LongIdKey pointKey, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                TriggeredValueMaintainService.CHILD_FOR_POINT,
                new Object[]{pointKey},
                pagingInfo
        );
    }

    @Override
    public PagedData<TriggeredValue> childForPointBetween(
            LongIdKey pointKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                TriggeredValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                new Object[]{pointKey, startDate, endDate},
                pagingInfo
        );
    }

    @Override
    public PagedData<TriggeredValue> childForTrigger(
            LongIdKey triggerKey, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                TriggeredValueMaintainService.CHILD_FOR_TRIGGER,
                new Object[]{triggerKey, pagingInfo},
                pagingInfo
        );
    }

    @Override
    public PagedData<TriggeredValue> childForTriggerBetween(
            LongIdKey triggerKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                TriggeredValueMaintainService.CHILD_FOR_TRIGGER_BETWEEN,
                new Object[]{triggerKey, startDate, endDate},
                pagingInfo
        );
    }
}
