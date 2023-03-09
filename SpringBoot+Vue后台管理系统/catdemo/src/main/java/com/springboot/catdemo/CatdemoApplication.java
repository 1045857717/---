package com.springboot.catdemo;

import javafx.application.Application;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.poi.util.StringUtil;
import org.apache.tomcat.websocket.server.WsSci;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi  // Swagger
@MapperScan("com.springboot.catdemo.mapper")
@EnableAspectJAutoProxy // 启用@AspectJ支持
//@EnableCaching // 开启springboot缓存
public class CatdemoApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(CatdemoApplication.class, args);
    }

    /**
     * 重启项目
     */
    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            context = SpringApplication.run(CatdemoApplication.class, args.getSourceArgs());
        });
        thread.setDaemon(false);
        thread.start();
    }


    @Bean
    public TomcatServletWebServerFactory  tomcatServletWebServerFactory () {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
        };
        tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        return tomcat;
    }

    /**
     * 让我们的应用支持HTTP是个好想法，但是需要重定向到HTTPS，
     * 但是不能同时在application.properties中同时配置两个connector，
     * 所以要以编程的方式配置HTTP connector，然后重定向到HTTPS connector
     * @return Connector
     */
    private Connector createHTTPConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        // Connector监听的http的端口号
        connector.setPort(81);
        // 监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(443);
        return connector;
    }

    /**
     * 创建wss协议接口
     *
     * @return
     */
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        System.out.println("websocket init");
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                System.out.println("init   customize");
                context.addServletContainerInitializer(new WsSci(), null);
            }
        };
    }
}
