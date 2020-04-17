package com.dwarfeng.fdrmanager.node.controller.v1;

import com.dwarfeng.fdr.sdk.bean.entity.FastJsonMapperSupport;
import com.dwarfeng.fdr.sdk.bean.entity.WebInputMapperSupport;
import com.dwarfeng.fdr.stack.bean.entity.MapperSupport;
import com.dwarfeng.fdrmanager.stack.service.MapperSupportResponseService;
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
public class MapperSupportController {

    @Autowired
    private MapperSupportResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @Autowired
    private BeanTransformer<MapperSupport, FastJsonMapperSupport> beanTransformer;

    @GetMapping("/mapper-support/{id}/exists")
    @BehaviorAnalyse
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            boolean exists = service.exists(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Boolean.class, e, sem));
        }
    }

    @GetMapping("/mapper-support/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<FastJsonMapperSupport> get(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            MapperSupport mapperSupport = service.get(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(FastJsonMapperSupport.of(mapperSupport)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(FastJsonMapperSupport.class, e, sem));
        }
    }

    @PostMapping("/mapper-support")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<FastJsonStringIdKey> insert(
            HttpServletRequest request,
            @RequestBody @Validated(Insert.class) WebInputMapperSupport mapperSupport, BindingResult bindingResult) {
        try {
            StringIdKey insert = service.insert(WebInputMapperSupport.toStackBean(mapperSupport));
            return FastJsonResponseData.of(ResponseDataUtil.good(FastJsonStringIdKey.of(insert)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(FastJsonStringIdKey.class, e, sem));
        }
    }

    @PatchMapping("/mapper-support")
    @BehaviorAnalyse
    @BindingCheck
    public FastJsonResponseData<Object> update(
            HttpServletRequest request,
            @RequestBody @Validated WebInputMapperSupport mapperSupport, BindingResult bindingResult) {
        try {
            service.update(WebInputMapperSupport.toStackBean(mapperSupport));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @DeleteMapping("/mapper-support/{id}")
    @BehaviorAnalyse
    public FastJsonResponseData<Object> delete(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            service.delete(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(null));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(Object.class, e, sem));
        }
    }

    @GetMapping("/mapper-support/all")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonMapperSupport>> all(
            HttpServletRequest request, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<MapperSupport> all = service.all(new PagingInfo(page, rows));
            PagedData<FastJsonMapperSupport> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/mapper-support/id-like")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonMapperSupport>> idLike(
            HttpServletRequest request,
            @RequestParam("pattern") String pattern, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<MapperSupport> all = service.idLike(pattern, new PagingInfo(page, rows));
            PagedData<FastJsonMapperSupport> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }

    @GetMapping("/mapper-support/label-like")
    @BehaviorAnalyse
    public FastJsonResponseData<JSFixedFastJsonPagedData<FastJsonMapperSupport>> labelLike(
            HttpServletRequest request,
            @RequestParam("pattern") String pattern, @RequestParam("page") int page, @RequestParam("rows") int rows) {
        try {
            PagedData<MapperSupport> all = service.labelLike(pattern, new PagingInfo(page, rows));
            PagedData<FastJsonMapperSupport> transform = PagingUtil.transform(all, beanTransformer);
            return FastJsonResponseData.of(ResponseDataUtil.good(JSFixedFastJsonPagedData.of(transform)));
        } catch (Exception e) {
            return FastJsonResponseData.of(ResponseDataUtil.bad(JSFixedFastJsonPagedData.class, e, sem));
        }
    }
}
