package com.cas.jcfz.service;

import com.alibaba.excel.context.AnalysisContext;
import com.cas.jcfz.domain.TemplateColumnConfig;

import java.util.List;
import java.util.Map;

/**
 *
 * @author wuzeqin
 * @date 2020/8/2
 **/
public class SystemFieldResolver {

    private Map<String/*storeField*/, String/*title*/> titleMap;
    private AnalysisContext context;
    private Map<String, Object> data;
    private Map<String, TemplateColumnConfig> excelColumnConfigMap;
    private Map<String/*storeField*/, TemplateColumnConfig> fieldConfigMap;  //存储名与配置的映射
    private List<TemplateColumnConfig> systemColumnConfigs;

}
