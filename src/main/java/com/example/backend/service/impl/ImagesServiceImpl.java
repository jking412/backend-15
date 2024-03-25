package com.example.backend.service.impl;

import com.example.backend.entity.Images;
import com.example.backend.mapper.ImagesMapper;
import com.example.backend.service.IImagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kunkun
 * @since 2024-03-24
 */
@Service
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements IImagesService {

}
