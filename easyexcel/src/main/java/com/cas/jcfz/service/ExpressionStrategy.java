package com.cas.jcfz.service;

import com.alibaba.excel.context.AnalysisContext;

import java.util.Map;

/**
 *
 * @author wuzeqin
 * @date 2020/8/2
 **/
public interface ExpressionStrategy {

    /**
     * 解析表达式
     * @param expression 表达式
     * @return 表达式结果
     */
    Object parse(String expression, AnalysisContext context, Map<String, Object> currentMap) throws Exception;

}
