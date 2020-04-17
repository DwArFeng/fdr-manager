package com.dwarfeng.fdrmanager.node.controller.v1;

import com.dwarfeng.fdr.sdk.bean.entity.FastJsonTriggerSupport;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputTriggerSupport;
import com.dwarfeng.fdr.stack.bean.entity.TriggerSupport;
import com.dwarfeng.fdrmanager.stack.service.TriggerSupportResponseService;
import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonResponseData;
import com.dwarfeng.subgrade.sdk.bean.dto.JSFixedFastJsonPagedData;
import com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil;
import com.dwarfeng.subgrade.sdk.bean.dto.ResponseDataUtil;
import com.dwarfeng.subgrade.sdk.bean.key.FastJsonStringIdKey;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.sdk.interceptor.http.BindingCheck;
import com.dwarfeng.subgrade.sdk.validation.group.Insert;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 过滤器支持控制器。
 *
 * @author DwArFeng
 * @since alpha-0.0.1
 */
@RestController
@RequestMapping("/api/v1")
public class TriggerSupportController {

    @Autowired
    private TriggerSupportResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<TriggerSupport, FastJsonTriggerSupport> beanTransformer;

    @GetMapping("/trigger-support/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            boolean exists = service.exists(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/trigger-support/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<FastJsonTriggerSupport> get(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            TriggerSupport triggerSupport = service.get(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(FastJsonTriggerSupport.of(triggerSupport)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(FastJsonTriggerSupport.class, e, sem));
        }
    }

    @PostMapping("/trigger-support")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<FastJsonStringIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputTriggerSupport triggerSupport, BindingResult bindingResult) {
        try {
            StringIdKey insert = service.insert(WebInputTriggerSupport.toStackBean(triggerSupport));
            return FastJsonResponseData.of(ResponseDataUtil.good(FastJsonStringIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(FastJsonStringIdKey.class, e, sem));
        }
    }

    @PatchMapping("/trigger-support")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputTriggerSupport triggerSupport, BindingResult bindingResult) {
        try {
            service.update(WebInputTriggerSupport.toStackBean(triggerSupport));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/trigger-support/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            service.delete(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/trigger-support/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonTriggerSupport>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggerSupport> all = service.all(new PagingInfo(page, rows));
            PagedData<FastJsonTriggerSupport> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/trigger-support/id-like")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonTriggerSupport>> idLike(
            HttpServletRequest request,
            @RequestParam("pattern") String pattern, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggerSupport> all = service.idLike(pattern, new PagingInfo(page, rows));
            PagedData<FastJsonTriggerSupport> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/trigger-support/label-like")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonTriggerSupport>> labelLike(
            HttpServletRequest request,
            @RequestParam("pattern") String pattern, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<TriggerSupport> all = service.labelLike(pattern, new PagingInfo(page, rows));
            PagedData<FastJsonTriggerSupport> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
