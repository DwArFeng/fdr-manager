package com.dwarfeng.fdrmanager.node.controller;

import com.dwarfeng.fdr.sdk.bean.entity.JSFixedFastJsonTriggeredValue;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputTriggeredValue;
import com.dwarfeng.fdr.stack.bean.entity.TriggeredValue;
import com.dwarfeng.fdrmanager.stack.service.TriggeredValueResponseService;
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
public class TriggeredValueController {

    @Autowired
    private TriggeredValueResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<TriggeredValue, JSFixedFastJsonTriggeredValue> beanTransformer;

    @GetMapping("/triggered-value/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            boolean exists = service.exists(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/triggered-value/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonTriggeredValue> get(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            TriggeredValue triggeredValue = service.get(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonTriggeredValue.of(triggeredValue)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonTriggeredValue.class, e, sem));
        }
    }

    @PostMapping("/triggered-value")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<JSFixedFastJsonLongIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputTriggeredValue triggeredValue, BindingResult bindingResult) {
        try {
            LongIdKey insert = service.insert(WebInputTriggeredValue.toStackBean(triggeredValue));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonLongIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonLongIdKey.class, e, sem));
        }
    }

    @PatchMapping("/triggered-value")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputTriggeredValue triggeredValue, BindingResult bindingResult) {
        try {
            service.update(WebInputTriggeredValue.toStackBean(triggeredValue));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/triggered-value/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            service.delete(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/triggered-value/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggeredValue>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggeredValue> all = service.all(new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggeredValue> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/triggered-value/all/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggeredValue>> allBetween(
            HttpServletRequest request,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggeredValue> all = service.allBetween(new Date(startDate), new Date(endDate),
                    new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggeredValue> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/triggered-value")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggeredValue>> childForPoint(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggeredValue> childForPoint =
                    service.childForPoint(new LongIdKey(pointId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggeredValue> transform = PagingUtil.transform(childForPoint, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/triggered-value/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggeredValue>> childForPointBetween(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggeredValue> childForPoint = service.childForPointBetween(new LongIdKey(pointId),
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggeredValue> transform = PagingUtil.transform(childForPoint, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/trigger/{triggerId}/triggered-value")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggeredValue>> childForTrigger(
            HttpServletRequest request,
            @PathVariable("triggerId") long triggerId,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggeredValue> childForTrigger =
                    service.childForTrigger(new LongIdKey(triggerId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggeredValue> transform = PagingUtil.transform(childForTrigger, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/trigger/{triggerId}/triggered-value/between")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggeredValue>> childForTriggerBetween(
            HttpServletRequest request,
            @PathVariable("triggerId") long triggerId,
            @RequestParam("start-date") long startDate, @RequestParam("end-date") long endDate,
            @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggeredValue> childForTrigger = service.childForTriggerBetween(new LongIdKey(triggerId),
                    new Date(startDate), new Date(endDate), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggeredValue> transform = PagingUtil.transform(childForTrigger, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
