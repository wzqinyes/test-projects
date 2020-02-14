package com.example.demo.service;


import com.example.demo.bean.ScanRequestDto;
import com.example.demo.entity.TRqdjScan;

import java.util.List;

public interface TRqdjScanService {

    TRqdjScan insert(TRqdjScan rqdjScan);

    List<ScanRequestDto> getScanRequestDto();

}
