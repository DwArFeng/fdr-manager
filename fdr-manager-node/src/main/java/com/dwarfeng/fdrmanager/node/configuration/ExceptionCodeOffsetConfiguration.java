package com.dwarfeng.fdrmanager.node.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ExceptionCodeOffsetConfiguration {

    @Value("${fdr_manager.exception_code_offset.subgrade}")
    private int subgradeExceptionCodeOffset;

    @PostConstruct
    public void init() {
        com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.setExceptionCodeOffset(subgradeExceptionCodeOffset);
    }
}
