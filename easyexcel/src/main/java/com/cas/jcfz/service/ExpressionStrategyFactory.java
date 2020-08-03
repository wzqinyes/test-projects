package com.cas.jcfz.service;

import com.alibaba.excel.context.AnalysisContext;
import org.springframework.lang.Nullable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wuzeqin
 * @date 2020/8/2
 **/
public class ExpressionStrategyFactory {

    private static Map<String, ExpressionStrategy> strategyMap = new HashMap<>();
    private static final ScriptEngineManager ENGINE_MANAGER = new ScriptEngineManager();

    static {
        strategyMap.put("", new ExpressionStrategy() {
            @Override
            public Object parse(String expression, AnalysisContext context, Map<String, Object> data) {
                ScriptEngine engine = ENGINE_MANAGER.getEngineByName("javascript");
                engine.put("sheet", context.readSheetHolder());
                engine.put("data", data);

                return expression;
            }
        });

    }

    public static ExpressionStrategy getExpressionStrategy(@Nullable String strategyKey) {
        if (strategyKey == null) {
            strategyKey = "";
        }
        return strategyMap.get(strategyKey);
    }



}
