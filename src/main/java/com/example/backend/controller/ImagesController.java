package com.example.backend.controller;

import com.example.backend.entity.Result;
import com.example.backend.entity.Images;
import com.example.backend.service.IImagesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kunkun
 * @since 2024-03-24
 */
@RestController
@RequestMapping("/images")
public class ImagesController {
    @Autowired
    private IImagesService imagesService;

    @ApiOperation("插入镜像")
    @PostMapping
    public Result InsertImage(@RequestBody Images images) {
        boolean res = imagesService.save(images);
        if (res == false) {
            return Result.error(400, "插入失败");
        }
        return Result.success(200,"插入成功");
    }

    @ApiOperation("获取所有镜像")
    @GetMapping
    public Result GetAllImages() {
        return Result.success(200, imagesService.list());
    }

    @ApiOperation("获取指定id的镜像")
    @GetMapping("/{id}")
    public Result GetImage(@PathVariable int id) {
        return Result.success(200, imagesService.getById(id));
    }
    @ApiOperation("删除指定id的镜像")
    @DeleteMapping("/{id}")
    public Result DeleteImage(@PathVariable int id) {
        boolean res = imagesService.removeById(id);
        if (res == false) {
            return Result.error(400, "删除失败");
        }
        return Result.success(200,"删除成功");
    }

    @ApiOperation("更新镜像")
    @PutMapping
    public Result UpdateImage(@RequestBody Images images) {
        boolean res = imagesService.updateById(images);
        if (res == false) {
            return Result.error(400, "更新失败");
        }
        return Result.success(200,"更新成功");
    }




}
