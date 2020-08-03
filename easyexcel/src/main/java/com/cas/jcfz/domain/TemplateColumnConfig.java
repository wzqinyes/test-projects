package com.cas.jcfz.domain;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author wuzeqin
 * @date 2020/8/1
 **/
@Data
public class TemplateColumnConfig implements Serializable {

    private Integer templateId;
    private Integer num;
    private String header;
    private String fieldName;
    private String storeField;
    private String fieldType;
    private boolean show;
    private boolean searchable;
    private boolean editable;
    private boolean required;
    private boolean needHeader;
    private String defaultExpression;
    //private Integer sameHeaderIndex;
    private String headerRegex;

    public boolean isNeedHeader() {
        return required || needHeader;
    }
}
