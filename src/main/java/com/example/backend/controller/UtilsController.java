package com.example.backend.controller;

import com.example.backend.Pojo.Result;
import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.PodMetricsList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping()
public class UtilsController {
    @GetMapping("/metric")
    public Result metric() throws Exception {
        Metrics metrics = new Metrics();
        PodMetricsList podMetrics = metrics.getPodMetrics("default");
        return Result.success(200,podMetrics);
    }

}
