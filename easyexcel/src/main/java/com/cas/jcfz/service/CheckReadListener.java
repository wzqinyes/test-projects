package com.cas.jcfz.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.cas.jcfz.domain.TemplateColumnConfig;
import com.cas.jcfz.domain.TemplateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导入数据校验与分析处理器
 *
 * @author wuzeqin
 * @date 2020/8/1
 **/
public class CheckReadListener extends AnalysisEventListener<Map<Integer, Object>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckReadListener.class);

    private final TemplateConfig templateConfig;

    private Map<String/*storeField*/, String/*title*/> titleMap;
    private Map<Integer, TemplateColumnConfig> columnConfigMap;  //列与配置的映射
    private Map<String/*storeField*/, TemplateColumnConfig> fieldConfigMap;  //存储名与配置的映射
    private List<TemplateColumnConfig> systemColumnConfigs;      //系统列配置

    public CheckReadListener(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        String sheetName = context.readSheetHolder().getSheetName();
        List<TemplateColumnConfig> columnConfigs = new ArrayList<>(templateConfig.getColumnConfigs().size());
        this.systemColumnConfigs = new ArrayList<>(10);
        this.columnConfigMap = new HashMap<>(templateConfig.getColumnConfigs().size());
        this.fieldConfigMap = new HashMap<>(templateConfig.getColumnConfigs().size());
        this.titleMap = new HashMap<>(headMap.size());
        templateConfig.getColumnConfigs().forEach(config -> {
            if (StringUtils.hasText(config.getHeader())) {
                columnConfigs.add(config);
            } else {
                systemColumnConfigs.add(config);
            }
            fieldConfigMap.put(config.getStoreField(), config);
        });
        TemplateColumnConfig[] configs = columnConfigs.toArray(new TemplateColumnConfig[0]);
        int remainsLength = configs.length;   //未找到匹配表头的配置的剩余长度
        Map<Integer, String> noneConfigHeadMap = new HashMap<>();   //未找到表头配置的列

        //找出表头列序号与列配置的映射关系。采用倒序遍历的方式，提高效率。
        outer:
        for (int k = headMap.size() - 1; k >= 0; k--) {
            String title = headMap.get(k);
            if (!StringUtils.hasText(title)) {
                continue;
            }
            for (int i = remainsLength - 1; i >= 0; i--) {
                TemplateColumnConfig tmpConfig = configs[i];
                if (isHeaderConfig(title, tmpConfig)) {  //找到表头配置
                    this.columnConfigMap.put(k, tmpConfig);
                    this.titleMap.put(tmpConfig.getStoreField(), title);
                    remainsLength--;
                    //把后面的往前移动
                    System.arraycopy(configs, i + 1, configs, i, remainsLength - i);
                    continue outer;
                }
            }
            //找不到配置
            noneConfigHeadMap.put(k, title);
        }

        //检查必须存在的项
        for (int i = 0; i < remainsLength; i++) {
            if (configs[i].isNeedHeader()) {
                throw new RuntimeException(String.format("工作表[%s]缺少一个表头“%s”，请检查模板是否匹配！", sheetName, configs[i].getHeader()));
            }
        }

        //检查未找到配置的表头，有些可能是合法重复表头，如果非法则抛异常
        noneConfigHeadMap.forEach((index, title) -> {
            for (TemplateColumnConfig columnConfig : this.columnConfigMap.values()) {
                if (isHeaderConfig(title, columnConfig)) {
                    //合法
                    return;
                }
            }
            throw new RuntimeException(String.format("工作表[%s]表头“%s”有误，请检查模板是否匹配！", sheetName, title));
        });
    }


    //判断列配置是否与title匹配
    private boolean isHeaderConfig(@NonNull String title, TemplateColumnConfig columnConfig) {
        String header = columnConfig.getHeader();
        if (title.equals(header)) {
            return true;
        }
        //title例子: 项目建设所在地地址[2018年2月]
        String headerRegex = columnConfig.getHeaderRegex();
        return StringUtils.hasText(headerRegex) && Pattern.compile(headerRegex).matcher(title).find();
    }

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        for (Map.Entry<Integer, Object> entry : data.entrySet()) {
            //System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        systemColumnConfigs.forEach(config -> {

        });
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        ReadSheetHolder sheetHolder = context.readSheetHolder();
        String sheetName = sheetHolder.getSheetName();
        Integer total = sheetHolder.getApproximateTotalRowNumber();
        System.out.printf("工作表[%s] import data total = %s \n", sheetName, total);
        if (columnConfigMap != null) {
            columnConfigMap.forEach((index, columnConfig) -> System.out.printf("%s -> %s \n", index + 1, columnConfig.getFieldName()));
        }
        this.columnConfigMap = null;
        this.systemColumnConfigs = null;
        System.out.println();
    }


    public static void main(String[] args) {
        //[: ]? 表示可以出现一次“：”，或者没有
        //(?:\[.+])? 表示可以出现一次“[xxx]”，或者没有；?:声明该()是非捕获的。
        Pattern pattern = Pattern.compile("^单位详细名称[：]?(?:\\[.+])?$");
        Matcher matcher;

        matcher = pattern.matcher("单位详细名称");
        System.out.println(matcher.find());  //true

        matcher = pattern.matcher("单位详细名称：");
        System.out.println(matcher.find());  //true

        matcher = pattern.matcher("单位详细名称[2020]");
        System.out.println(matcher.find());  //true

        matcher = pattern.matcher("单位详细名称：[2020]");
        System.out.println(matcher.find());  //true

        matcher = pattern.matcher("单位详细名称：[2020");
        System.out.println(matcher.find());  //false

        matcher = pattern.matcher("单位详细名称：2020");
        System.out.println(matcher.find());  //false
    }
}
