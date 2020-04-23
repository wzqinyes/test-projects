package com.example.demo.controller;

import com.example.demo.bean.ScanRequestDto;
import com.example.demo.entity.TRqdjScan;
import com.example.demo.service.TRqdjScanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/rqdj")
public class RqdjController {

    @Resource
    private TRqdjScanService scanService;

    @PostMapping("/scan")
    public ResponseEntity<?> scan(@RequestBody ScanRequestDto requestDto) throws Exception {
        TRqdjScan scan = new TRqdjScan();
        scan.setInfoId(requestDto.getInfoId());
        scan.setStation(requestDto.getStation());
        scan.setStationCode(requestDto.getStationCode());
        scan.setWorker("张三");
        try {
            scanService.insert(scan);
        }catch (Exception e) {

        }
        System.out.println(scan.getId());
        System.out.println(scan.getCreateTime());

        return ResponseEntity.ok(scan);
    }

    @GetMapping("/scans")
    public ResponseEntity<?> scans() throws Exception {
        return ResponseEntity.ok(scanService.getScanRequestDto());
    }

    @GetMapping("/scans/{infoId}")
    public ResponseEntity<?> scans(@PathVariable("infoId") Long infoId) throws Exception {
        return ResponseEntity.ok(scanService.getScanRequestDto(infoId));
    }

    @GetMapping("/scan/{infoId}")
    public ResponseEntity<?> scan(@PathVariable("infoId") Long infoId) throws Exception {
        return ResponseEntity.ok(scanService.getTRqdjScanByInfoId(infoId));
    }

    @GetMapping("/testmp3/{id}")
    public void testMp3(@PathVariable("id") String id, HttpServletResponse response) throws Exception {

    }

}
