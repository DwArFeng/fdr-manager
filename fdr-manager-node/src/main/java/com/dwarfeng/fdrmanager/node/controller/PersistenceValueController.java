package com.dwarfeng.fdrmanager.node.controller;

import com.dwarfeng.fdr.sdk.bean.entity.JSFixedFastJsonPersistenceValue;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputPersistenceValue;
import com.dwarfeng.fdr.stack.bean.entity.PersistenceValue;
import com.dwarfeng.fdrmanager.stack.service.PersistenceValueResponseService;
import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.sdk.bean.dto.JSFixedFastJsonPagedData;
import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.bean.dto.ResponseDataUtil;
import com.dwarfeng.subgrade.sdk.bean.key.JSFixedFastJsonLongIdKey;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.http.BindingCheck;
import com.dwarfeng.subgrade.sdk.validation.group.Insert;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 实时数据控制器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1")
public class PersistenceValueController {

    @Autowired
    private PersistenceValueResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<PersistenceValue, JSFixedFastJsonPersistenceValue> beanTransformer;

    @GetMapping("/persistence-value/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            boolean exists = service.exists(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/persistence-value/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPersistenceValue> get(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            PersistenceValue persistenceValue = service.get(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPersistenceValue.of(persistenceValue)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPersistenceValue.class, e, sem));
        }
    }

    @PostMapping("/persistence-value")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<JSFixedFastJsonLongIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputPersistenceValue persistenceValue, BindingResult bindingResult) {
        try {
            LongIdKey insert = service.insert(WebInputPersistenceValue.toStackBean(persistenceValue));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonLongIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonLongIdKey.class, e, sem));
        }
    }

    @PatchMapping("/persistence-value")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputPersistenceValue persistenceValue, BindingResult bindingResult) {
        try {
            service.update(WebInputPersistenceValue.toStackBean(persistenceValue));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/persistence-value/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            service.delete(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/persistence-value/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonPersistenceValue>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<PersistenceValue> all = service.all(new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonPersistenceValue> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/persistence-value/all/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonPersistenceValue>> allBetween(
            HttpServletRequest request,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<PersistenceValue> all = service.allBetween(new Date(startDate), new Date(endDate),
                    new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonPersistenceValue> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/persistence-value")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonPersistenceValue>> childForPoint(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<PersistenceValue> childForPoint =
                    service.childForPoint(new LongIdKey(pointId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonPersistenceValue> transform = PagingUtil.transform(childForPoint, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/persistence-value/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonPersistenceValue>> childForPointBetween(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<PersistenceValue> childForPoint = service.childForPointBetween(new LongIdKey(pointId),
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonPersistenceValue> transform = PagingUtil.transform(childForPoint, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
