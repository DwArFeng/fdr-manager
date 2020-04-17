package com.dwarfeng.fdrmanager.node.controller.v1;

import com.dwarfeng.fdr.sdk.bean.entity.JSFixedFastJsonTriggerInfo;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputTriggerInfo;
import com.dwarfeng.fdr.stack.bean.entity.TriggerInfo;
import com.dwarfeng.fdrmanager.stack.service.TriggerInfoResponseService;
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

/**
 * 过滤器信息控制器。
 *
 * @author DwArFeng
 * @since alpha-0.0.1
 */
@RestController
@RequestMapping("/api/v1")
public class TriggerInfoController {

    @Autowired
    private TriggerInfoResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<TriggerInfo, JSFixedFastJsonTriggerInfo> beanTransformer;

    @GetMapping("/trigger-info/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            boolean exists = service.exists(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/trigger-info/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonTriggerInfo> get(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            TriggerInfo triggerInfo = service.get(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonTriggerInfo.of(triggerInfo)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonTriggerInfo.class, e, sem));
        }
    }

    @PostMapping("/trigger-info")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<JSFixedFastJsonLongIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputTriggerInfo triggerInfo, BindingResult bindingResult) {
        try {
            LongIdKey insert = service.insert(WebInputTriggerInfo.toStackBean(triggerInfo));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonLongIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonLongIdKey.class, e, sem));
        }
    }

    @PatchMapping("/trigger-info")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputTriggerInfo triggerInfo, BindingResult bindingResult) {
        try {
            service.update(WebInputTriggerInfo.toStackBean(triggerInfo));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/trigger-info/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            service.delete(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/trigger-info/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggerInfo>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggerInfo> all = service.all(new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggerInfo> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/trigger-infos")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonTriggerInfo>> childForPoint(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggerInfo> all = service.childForPoint(new LongIdKey(pointId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonTriggerInfo> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
