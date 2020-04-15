package com.dwarfeng.fdrmanager.impl.service;

import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.fdr.stack.service.PersistenceValueMaintainService;
import com.dwarfeng.fdrmanager.stack.service.PersistenceValueResponseService;
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
public class PersistenceValueResponseServiceImpl implements PersistenceValueResponseService {

    @Autowired
    @Qualifier("persistenceValueMaintainService")
    private PersistenceValueMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public PersistenceValue get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(PersistenceValue persistenceValue) throws ServiceException {
        return service.insert(persistenceValue);
    }

    @Override
    public void update(PersistenceValue persistenceValue) throws ServiceException {
        service.update(persistenceValue);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<PersistenceValue> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }

    @Override
    public PagedData<PersistenceValue> allBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                PersistenceValueMaintainService.BETWEEN,
                new Object[]{startDate, endDate},
                pagingInfo
        );
    }

    @Override
    public PagedData<PersistenceValue> childForPoint(
            LongIdKey pointKey, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                PersistenceValueMaintainService.CHILD_FOR_POINT,
                new Object[]{pointKey},
                pagingInfo
        );
    }

    @Override
    public PagedData<PersistenceValue> childForPointBetween(
            LongIdKey pointKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                PersistenceValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                new Object[]{pointKey, startDate, endDate},
                pagingInfo
        );
    }
}
