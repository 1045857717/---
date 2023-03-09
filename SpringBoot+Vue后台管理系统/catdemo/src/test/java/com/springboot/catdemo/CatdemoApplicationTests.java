package com.springboot.catdemo;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

// webEnvironment:创建Web应用程序上下文（基于响应或基于servlet），原因:websocket是需要依赖tomcat等容器的启动。所以在测试过程中我们要真正的启动一个tomcat作为容器
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CatdemoApplicationTests {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    void contextLoads() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        // 盐值
        standardPBEStringEncryptor.setPassword("1045857717@qq.com");
        // 加密明文
        String root = standardPBEStringEncryptor.encrypt("root");
        System.out.println(root);
        // 解密
        String decrypt = standardPBEStringEncryptor.decrypt(root);
        System.out.println(decrypt);

        //加密明文
        String username = encryptor.encrypt("root");
        System.out.println("username|root加密:" + username);
        String password = encryptor.encrypt("root");
        System.out.println("password|root加密:" + password);
        String password2 = encryptor.encrypt("zZz268450");
        System.out.println("password2|zZz268450加密:" + password2);
        //第二次test,解密第一次test的密文
        System.out.println("username|root解密:" + encryptor.decrypt(username));
        System.out.println("password|root解密:" + encryptor.decrypt(password));
        System.out.println("password2|zZz268450解密:" + encryptor.decrypt(password2));

        System.out.println("username|root解密2:" + encryptor.decrypt("8NGJQl2NZuMZVnZlhShn5vXp+ga52eH5HiKy4vs5mBvxyj2T+HI8c9SMTEEQnjB7")); // root
        System.out.println("password|root解密2:" + encryptor.decrypt("+qVLHIJn23d/c6fWt985CL9PDBVw351INLrrylpFfXHOX87RPgcSHd4pbxiZ6oy8")); // root
        System.out.println("password2|zZz268450解密2:" + encryptor.decrypt("EYqO3ry1CUIiL9Wznq+3a5akvQ0cLp6k5fLeodV53GJdTpLnvueFwdNCqH5X69cJ")); // zZz268450
    }

    @Test
    void test1(){

        //加密明文
        String username = encryptor.encrypt("root");
        System.out.println("username加密:" + username);
        String password = encryptor.encrypt("root");
        System.out.println("password加密:" + password);
        //第二次test,解密第一次test的密文
        System.out.println("username解密:" + encryptor.decrypt(username));
        System.out.println("password解密:" + encryptor.decrypt(password));
    }

}
