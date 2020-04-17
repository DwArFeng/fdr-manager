package com.dwarfeng.fdrmanager.node.controller.v1;

import com.dwarfeng.fdr.sdk.bean.entity.JSFixedFastJsonFilterInfo;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputFilterInfo;
import com.dwarfeng.fdr.stack.bean.entity.FilterInfo;
import com.dwarfeng.fdrmanager.stack.service.FilterInfoResponseService;
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
public class FilterInfoController {

    @Autowired
    private FilterInfoResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<FilterInfo, JSFixedFastJsonFilterInfo> beanTransformer;

    @GetMapping("/filter-info/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            boolean exists = service.exists(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/filter-info/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonFilterInfo> get(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            FilterInfo filterInfo = service.get(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonFilterInfo.of(filterInfo)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonFilterInfo.class, e, sem));
        }
    }

    @PostMapping("/filter-info")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<JSFixedFastJsonLongIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputFilterInfo filterInfo, BindingResult bindingResult) {
        try {
            LongIdKey insert = service.insert(WebInputFilterInfo.toStackBean(filterInfo));
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonLongIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonLongIdKey.class, e, sem));
        }
    }

    @PatchMapping("/filter-info")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputFilterInfo filterInfo, BindingResult bindingResult) {
        try {
            service.update(WebInputFilterInfo.toStackBean(filterInfo));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/filter-info/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") long id) {
        try {
            service.delete(new LongIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/filter-info/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilterInfo>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilterInfo> all = service.all(new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilterInfo> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/point/{pointId}/filter-infos")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<JSFixedFastJsonFilterInfo>> childForPoint(
            HttpServletRequest request,
            @PathVariable("pointId") long pointId, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<FilterInfo> all = service.childForPoint(new LongIdKey(pointId), new PagingInfo(page, rows));
            PagedData<JSFixedFastJsonFilterInfo> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
