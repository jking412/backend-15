package com.example.backend.controller;

import com.example.backend.entity.Result;
import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.PodMetricsList;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class UtilsController {
    @ApiOperation("获取日志")
    @GetMapping("/metric")
    public Result metric() throws Exception {
        Metrics metrics = new Metrics();
        PodMetricsList podMetrics = metrics.getPodMetrics("default");
        return Result.success(200,podMetrics);
    }

}
