package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bean.ScanRequestDto;
import com.example.demo.entity.TRqdjScan;
import com.example.demo.mapper.TRqdjScanMapper;
import com.example.demo.service.TRqdjScanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("rqdjScanService")
public class TRqdjScanServiceImpl extends ServiceImpl<TRqdjScanMapper, TRqdjScan> implements TRqdjScanService {

    private final TRqdjScanMapper scanMapper;

    public TRqdjScanServiceImpl(TRqdjScanMapper scanMapper) {
        this.scanMapper = scanMapper;
    }

    @Override
    @Transactional
    public TRqdjScan insert(TRqdjScan rqdjScan) {
        scanMapper.insert(rqdjScan);
        System.out.println(rqdjScan.getId());
        System.out.println(rqdjScan.getCreateTime());
        throw new RuntimeException("事务测试！回滚。。。");
    }

    @Override
    public List<ScanRequestDto> getScanRequestDto() {
        return scanMapper.selectScanRequestDto();
    }

}
