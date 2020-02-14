package com.example.demo.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充默认字段，比如创建时间、修改时间、创建人、修改人
 */
@Component
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME_FIELD_NAME = "createTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_TIME_FIELD_NAME)) {
            setInsertFieldValByName(CREATE_TIME_FIELD_NAME, LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_TIME_FIELD_NAME)) {
            setUpdateFieldValByName(CREATE_TIME_FIELD_NAME, LocalDateTime.now(), metaObject);
        }
    }
}
