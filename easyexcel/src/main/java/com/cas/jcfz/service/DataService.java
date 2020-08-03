package com.cas.jcfz.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.cas.jcfz.domain.TemplateColumnConfig;
import com.cas.jcfz.domain.TemplateConfig;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author wuzeqin
 * @date 2020/8/1
 **/
public class DataService {

    public static void main(String[] args) {
        TemplateConfig templateConfig = getConfig();
        ClassPathResource resource = new ClassPathResource("定报房地产X204-1表.xls");
        try (InputStream inputStream = resource.getInputStream()) {
            ExcelReader excelReader = EasyExcel.read(inputStream, new CheckReadListener(templateConfig))
                .ignoreEmptyRow(Boolean.FALSE).headRowNumber(templateConfig.getHeaderRow()).build();
            List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
            excelReader.read(sheetList);
            //sheetList.forEach(excelReader::read);
            excelReader.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static TemplateConfig getConfig() {
        TemplateConfig config = new TemplateConfig();
        final int templateId = 1001;
        config.setId(templateId);
        config.setName("定报房地产X204-1");

        ClassPathResource resource = new ClassPathResource("template.xls");

        try (InputStream inputStream = resource.getInputStream()) {
            EasyExcel.read(inputStream, new AnalysisEventListener<Map<Integer, Object>>() {
                @Override
                public void invoke(Map<Integer, Object> data, AnalysisContext context) {
                    Object tmpId = data.get(0);
                    if (tmpId == null || Integer.valueOf(tmpId.toString()) != templateId) {
                        return;
                    }
                    TemplateColumnConfig columnConfig = new TemplateColumnConfig();
                    columnConfig.setTemplateId(templateId);
                    columnConfig.setNum(getValue(data.get(1), Integer.class));
                    columnConfig.setHeader(getValue(data.get(2), null));
                    columnConfig.setFieldName(getValue(data.get(3), null));
                    columnConfig.setStoreField(getValue(data.get(4), null));
                    columnConfig.setFieldType(getValue(data.get(5), null));
                    columnConfig.setShow(getValue(data.get(6), false));
                    columnConfig.setSearchable(getValue(data.get(7), false));
                    columnConfig.setEditable(getValue(data.get(8), true));
                    columnConfig.setRequired(getValue(data.get(9), false));
                    columnConfig.setNeedHeader(getValue(data.get(10), false));
                    columnConfig.setDefaultExpression(getValue(data.get(11), null));
                    //columnConfig.setSameHeaderIndex(getValue(data.get(12), Integer.class));
                    columnConfig.setHeaderRegex(getValue(data.get(13), null));
                    config.addColumnConfigs(columnConfig);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).headRowNumber(1).sheet().doRead();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    @SuppressWarnings("unchecked")
    static <T> T getValue(Object o, Class<T> clazz) {
        if (o == null || clazz == null) {
            return (T) o;
        }

        if (clazz.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(o.toString());
        }

        if (clazz.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(o.toString());
        }

        if (clazz.isAssignableFrom(Boolean.class)) {
            String b = o.toString();
            return clazz.cast(b.equals("y") || b.equals("yes") || b.equals("1") || b.equalsIgnoreCase("true"));
        }

        return (T) o;
    }

    @SuppressWarnings("unchecked")
    static <T> T getValue(Object o, T def) {
        T result = getValue(o, (Class<T>) def.getClass());
        return result == null ? def : result;
    }
}
