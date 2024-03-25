package com.example.backend.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MPUtil {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/os", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("kunkun") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/home/jking/IdeaProjects/backend/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example") // 设置父包名
                            .moduleName("backend") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/home/jking/IdeaProjects/backend/src/main/resources/xml" )); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("constants",
                            "container_ports",
                            "images",
                            "network_info",
                            "pod_info",
                            "scripts",
                            "security_group",
                            "security_group_ports",
                            "user_info",
                            "volume_info");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
