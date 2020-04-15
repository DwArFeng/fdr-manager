package com.dwarfeng.fdrmanager.impl.service;

import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.fdr.stack.service.FilteredValueMaintainService;
import com.dwarfeng.fdrmanager.stack.service.FilteredValueResponseService;
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
public class FilteredValueResponseServiceImpl implements FilteredValueResponseService {

    @Autowired
    @Qualifier("filteredValueMaintainService")
    private FilteredValueMaintainService service;

    @Override
    public boolean exists(LongIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public FilteredValue get(LongIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public LongIdKey insert(FilteredValue filteredValue) throws ServiceException {
        return service.insert(filteredValue);
    }

    @Override
    public void update(FilteredValue filteredValue) throws ServiceException {
        service.update(filteredValue);
    }

    @Override
    public void delete(LongIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<FilteredValue> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }

    @Override
    public PagedData<FilteredValue> allBetween(
            Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                FilteredValueMaintainService.BETWEEN,
                new Object[]{startDate, endDate},
                pagingInfo
        );
    }

    @Override
    public PagedData<FilteredValue> childForPoint(LongIdKey pointKey, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                FilteredValueMaintainService.CHILD_FOR_POINT,
                new Object[]{pointKey},
                pagingInfo
        );
    }

    @Override
    public PagedData<FilteredValue> childForPointBetween(
            LongIdKey pointKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                FilteredValueMaintainService.CHILD_FOR_POINT_BETWEEN,
                new Object[]{pointKey, startDate, endDate},
                pagingInfo
        );
    }

    @Override
    public PagedData<FilteredValue> childForFilter(
            LongIdKey filterKey, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                FilteredValueMaintainService.CHILD_FOR_FILTER,
                new Object[]{filterKey, pagingInfo},
                pagingInfo
        );
    }

    @Override
    public PagedData<FilteredValue> childForFilterBetween(
            LongIdKey filterKey, Date startDate, Date endDate, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(
                FilteredValueMaintainService.CHILD_FOR_FILTER_BETWEEN,
                new Object[]{filterKey, startDate, endDate},
                pagingInfo
        );
    }
}
