package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @since 2020-02-12 17:45:54
 */
@Data
public class TRqdjScan implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long infoId;

    private String worker;
    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String station;

    private String stationCode;

}