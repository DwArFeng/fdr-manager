package com.dwarfeng.fdrmanager.impl.service;

import com.dwarfeng.fdr.stack.bean.entity.RealtimeValue;
import com.dwarfeng.fdr.stack.service.RealtimeValueMaintainService;
import com.dwarfeng.fdrmanager.stack.service.RealtimeValueResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class RealtimeValueResponseServiceImpl implements RealtimeValueResponseService {

    @Autowired
    @Qualifier("realtimeValueMaintainService")
    private RealtimeValueMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public RealtimeValue get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(RealtimeValue realtimeValue) throws ServiceException {
        return service.insert(realtimeValue);
    }

    @Override
    public void update(RealtimeValue realtimeValue) throws ServiceException {
        service.update(realtimeValue);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<RealtimeValue> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }
}
