package com.cas.jcfz.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wuzeqin
 * @date 2020/8/1
 **/
@Data
public class TemplateConfig implements Serializable {

    private Integer id;

    private String name;

    private int headerRow = 1;

    private List<TemplateColumnConfig> columnConfigs = new ArrayList<>();

    public void addColumnConfigs(TemplateColumnConfig... columnConfigs) {
        this.columnConfigs.addAll(Arrays.asList(columnConfigs));
    }
}
