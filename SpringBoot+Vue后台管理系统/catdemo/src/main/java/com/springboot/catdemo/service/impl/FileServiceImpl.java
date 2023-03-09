package com.springboot.catdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.catdemo.entity.Files;
import com.springboot.catdemo.mapper.FileMapper;
import com.springboot.catdemo.service.IFileService;
import org.springframework.stereotype.Service;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 22:28 2022/3/18
 */
@Service
public class FileServiceImpl  extends ServiceImpl<FileMapper, Files> implements IFileService {
}
