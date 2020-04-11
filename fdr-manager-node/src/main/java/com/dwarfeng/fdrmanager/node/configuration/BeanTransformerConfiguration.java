package com.dwarfeng.fdrmanager.node.configuration;

import com.dwarfeng.fdr.sdk.bean.entity.*;
import com.dwarfeng.fdr.stack.bean.entity.*;
import com.dwarfeng.subgrade.impl.bean.DozerBeanTransformer;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanTransformerConfiguration {

    @Autowired
    private Mapper mapper;

    @Bean
    public BeanTransformer<Point, JSFixedFastJsonPoint> pointJSFixedFastJsonPointBeanTransformer() {
        return new DozerBeanTransformer<>(Point.class, JSFixedFastJsonPoint.class, mapper);
    }

    @Bean
    public BeanTransformer<FilterInfo, JSFixedFastJsonFilterInfo> filterInfoJSFixedFastJsonFilterInfoBeanTransformer() {
        return new DozerBeanTransformer<>(FilterInfo.class, JSFixedFastJsonFilterInfo.class, mapper);
    }

    @Bean
    public BeanTransformer<TriggerInfo, JSFixedFastJsonTriggerInfo> triggerInfoJSFixedFastJsonTriggerInfoBeanTransformer() {
        return new DozerBeanTransformer<>(TriggerInfo.class, JSFixedFastJsonTriggerInfo.class, mapper);
    }

    @Bean
    public BeanTransformer<FilterSupport, FastJsonFilterSupport> filterSupportFastJsonFilterSupportBeanTransformer() {
        return new DozerBeanTransformer<>(FilterSupport.class, FastJsonFilterSupport.class, mapper);
    }

    @Bean
    public BeanTransformer<TriggerSupport, FastJsonTriggerSupport> triggerSupportFastJsonTriggerSupportBeanTransformer() {
        return new DozerBeanTransformer<>(TriggerSupport.class, FastJsonTriggerSupport.class, mapper);
    }

    @Bean
    public BeanTransformer<MapperSupport, FastJsonMapperSupport> mapperSupportFastJsonMapperSupportBeanTransformer() {
        return new DozerBeanTransformer<>(MapperSupport.class, FastJsonMapperSupport.class, mapper);
    }
}
