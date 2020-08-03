package com.cas.jcfz.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 快速生成末班模板表头
 *
 * @author wuzeqin
 * @date 2020/8/1
 **/
public class GenHeaders extends AnalysisEventListener<Map<Integer, Object>> {

    private StringBuffer stringBuffer;

    private static int genType = 0;   //0生成表头，1生成匹配模式

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        stringBuffer = new StringBuffer();
        for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
            String title = entry.getValue();
            String header = getHeaderName(title);
            if (header == null) {
                continue;
            }
            if (genType == 0) {
                stringBuffer.append(header).append("\n");
            } else if (genType == 1) {
                header = header.replace("(", "\\(").replace(")", "\\)");
                stringBuffer.append("^").append(header).append("[：]?(?:\\[.+])?$").append("\n");
            }

        }
    }

    public static String getHeaderName(String header) {
        //Multimap<String, String> multimap = ArrayListMultimap.create();
        if (StringUtils.isEmpty(header)) {
            return null;
        }

        String headerName = null;
        String[] strings = header.replaceAll("\\[.*]", "").split("：");   //去掉[xxx]，以“：”为分隔符。
        for (int i = strings.length - 1; i >= 0; i--) {
            if (StringUtils.hasText(strings[i])) {
                headerName = strings[i];
                break;
            }
        }
        return headerName;
    }

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //System.out.printf("import data total = %s \n", total);
        if (stringBuffer != null) {
            System.out.println(stringBuffer.toString());
            stringBuffer = null;
        }
    }

    public static void main(String[] args) {
        genType = 1;
        ClassPathResource resource = new ClassPathResource("定报房地产X204-1表.xls");
        try (InputStream inputStream = resource.getInputStream()) {
            ExcelReader excelReader = EasyExcel.read(inputStream, new GenHeaders()).ignoreEmptyRow(Boolean.FALSE).headRowNumber(1).build();
            List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
            excelReader.read(sheetList.get(0));
            excelReader.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
