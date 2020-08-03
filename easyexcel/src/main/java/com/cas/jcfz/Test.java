package com.cas.jcfz;

import com.cas.jcfz.domain.TemplateColumnConfig;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author wuzeqin
 * @date 2020/8/2
 **/
public class Test {


    public static void main(String[] args) throws Exception{
        String exp = "new Date().getTime().toString()";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Object res = engine.eval(exp);
        System.out.println(res);
        TemplateColumnConfig config = new TemplateColumnConfig();
        config.setStoreField("aaaaaaa");
        engine.put("sheet", config);
        res = engine.eval("sheet.storeField");
        System.out.println(res);

    }

}
