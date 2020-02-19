package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.ScanRequestDto;
import com.example.demo.entity.TRqdjScan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//如果已经开启了扫描@MapperScan，并且这个包再扫描范围内，则这里不需要@Mapper注解
//@Mapper
public interface TRqdjScanMapper extends BaseMapper<TRqdjScan> {

    //xml文件配置方式
    List<ScanRequestDto> selectScanRequestDto();

    //直接写语句，不需要跑到xml文件配置
    @Select("select * from t_rqdj_scan where info_id=#{infoId}")
    List<ScanRequestDto> selectScanRequestDtoByInfoId(@Param("infoId") Long infoId);

    //直接写语句，不需要跑到xml文件配置
    @Select("select * from t_rqdj_scan where info_id=${infoId}")
    List<TRqdjScan> selectByInfoId(@Param("infoId") Long infoId);

}
