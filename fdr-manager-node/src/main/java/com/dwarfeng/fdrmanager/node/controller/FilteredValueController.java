package com.dwarfeng.fdrmanager.node.controller;

import com.dwarfeng.fdr.sdk.bean.entity.JSFixedFastJsonFilteredValue;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputFilteredValue;
import com.dwarfeng.fdr.stack.bean.entity.FilteredValue;
import com.dwarfeng.fdrmanager.stack.service.FilteredValueResponseService;
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
public class FilteredValueController {

    @Autowired
    private FilteredValueResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<FilteredValue, JSFixedFastJsonFilteredValue> beanTransformer;

    @GetMapping("/filtered-value/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            boolean exists = service.exists(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/filtered-value/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonFilteredValue> get(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            FilteredValue filteredValue = service.get(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonFilteredValue.of(filteredValue)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonFilteredValue.class, e, sem));
        }
    }

    @PostMapping("/filtered-value")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<JSFixedFastJsonLongIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputFilteredValue filteredValue, BindingResult bindingResult) {
        try {
            LongIdKey insert = service.insert(WebInputFilteredValue.toStackBean(filteredValue));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonLongIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonLongIdKey.class, e, sem));
        }
    }

    @PatchMapping("/filtered-value")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputFilteredValue filteredValue, BindingResult bindingResult) {
        try {
            service.update(WebInputFilteredValue.toStackBean(filteredValue));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/filtered-value/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            service.delete(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/filtered-value/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilteredValue>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilteredValue> all = service.all(new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilteredValue> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/filtered-value/all/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilteredValue>> allBetween(
            HttpServletRequest request,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilteredValue> all = service.allBetween(new Date(startDate), new Date(endDate),
                    new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilteredValue> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/filtered-value")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilteredValue>> childForPoint(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilteredValue> childForPoint =
                    service.childForPoint(new LongIdKey(pointId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilteredValue> transform = PagingUtil.transform(childForPoint, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/filtered-value/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilteredValue>> childForPointBetween(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilteredValue> childForPoint = service.childForPointBetween(new LongIdKey(pointId),
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilteredValue> transform = PagingUtil.transform(childForPoint, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/filter/{filterId}/filtered-value")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilteredValue>> childForFilter(
            HttpServletRequest request,
            @PathVariable("filterId") long filterId,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilteredValue> childForFilter =
                    service.childForFilter(new LongIdKey(filterId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilteredValue> transform = PagingUtil.transform(childForFilter, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/filter/{filterId}/filtered-value/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilteredValue>> childForFilterBetween(
            HttpServletRequest request,
            @PathVariable("filterId") long filterId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilteredValue> childForFilter = service.childForFilterBetween(new LongIdKey(filterId),
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilteredValue> transform = PagingUtil.transform(childForFilter, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
