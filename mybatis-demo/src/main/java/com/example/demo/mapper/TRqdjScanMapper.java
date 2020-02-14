package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.ScanRequestDto;
import com.example.demo.entity.TRqdjScan;

import java.util.List;

//如果已经开启了扫描@MapperScan，并且这个包再扫描范围内，则这里不需要@Mapper注解
//@Mapper
public interface TRqdjScanMapper extends BaseMapper<TRqdjScan> {

    List<ScanRequestDto> selectScanRequestDto();

}
