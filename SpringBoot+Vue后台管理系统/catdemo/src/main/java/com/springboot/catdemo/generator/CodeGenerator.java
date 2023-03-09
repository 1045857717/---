package com.springboot.catdemo.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import net.bytebuddy.NamingStrategy;

import java.util.Collections;

/**
 * 代码生产器
 * @Author: can
 * @Description:
 * @Date: Create in 23:29 2022/3/5
 */
public class CodeGenerator {

    private static String MYSQL_URL = "jdbc:mysql://localhost:3306/catdemo?serverTimezone=GMT%2b8";
    private static String USERNAME = "root";
    private static String PASSWORD = "root";
    private static String AUTHOR = "CAN";   // 作者

    public static void main(String[] args) {
        generate();
    }

    private static void generate() {

        FastAutoGenerator.create(MYSQL_URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件(3.5.2此方法会被删除)
                            .outputDir("F:\\个人项目\\SpringBoot+Vue后台管理系统\\catdemo\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.springboot.catdemo") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "F:\\个人项目\\SpringBoot+Vue后台管理系统\\catdemo\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                                .enableChainModel() // 开启链式模型
                                .enableLombok()   // 开启lombok模型
                                .enableTableFieldAnnotation()   //开启生成实体时生成字段注解
                            .controllerBuilder()
                                .enableHyphenStyle()    // 开启驼峰转连字符
                                .enableRestStyle()  // 开启生成@RestController 控制器
                            .mapperBuilder()
                                .enableBaseColumnList()
                                .enableBaseResultMap();
                    builder.addInclude("cat_of_files") // 设置需要生成的表名
                    .addTablePrefix("sys_","cat_","wx_"); // 设置需要过滤的表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new VelocityTemplateEngine()) // 默认的是Velocity引擎模板
                .execute();

    }
}
