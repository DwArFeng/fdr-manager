package com.dwarfeng.fdrmanager.impl.service;

import com.dwarfeng.fdr.stack.bean.entity.MapperSupport;
import com.dwarfeng.fdr.stack.service.MapperSupportMaintainService;
import com.dwarfeng.fdrmanager.stack.service.MapperSupportResponseService;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class MapperSupportResponseServiceImpl implements MapperSupportResponseService {

    @Autowired
    @Qualifier("mapperSupportMaintainService")
    private MapperSupportMaintainService service;

    @Override
    public boolean exists(StringIdKey key) throws ServiceException {
        return service.exists(key);
    }

    @Override
    public MapperSupport get(StringIdKey key) throws ServiceException {
        return service.get(key);
    }

    @Override
    public StringIdKey insert(MapperSupport mapperSupport) throws ServiceException {
        return service.insertIfNotExists(mapperSupport);
    }

    @Override
    public void update(MapperSupport mapperSupport) throws ServiceException {
        service.update(mapperSupport);
    }

    @Override
    public void delete(StringIdKey key) throws ServiceException {
        service.delete(key);
    }

    @Override
    public PagedData<MapperSupport> all(PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(pagingInfo);
    }

    @Override
    public PagedData<MapperSupport> idLike(String pattern, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(MapperSupportMaintainService.ID_LIKE, new Object[]{pattern}, pagingInfo);
    }

    @Override
    public PagedData<MapperSupport> labelLike(String pattern, PagingInfo pagingInfo) throws ServiceException {
        return service.lookup(MapperSupportMaintainService.LABEL_LIKE, new Object[]{pattern}, pagingInfo);
    }
}
