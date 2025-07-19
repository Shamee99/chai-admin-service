package org.shamee.launcher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;

/**
 * 启动类
 *
 * @author shamee
 * @since 2024-01-01
 */
@SpringBootApplication(scanBasePackages = "org.shamee")
@MapperScan("org.shamee.**.mapper")
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {
        //关闭热部署
        System.setProperty("spring.devtools.restart.enabled","false");
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        String info = """

                ----------------------------------------------------------
                应用 '%s' 运行成功! 当前环境 '%s' !!! 端口 '[%s]' !!!
                ----------------------------------------------------------

                """;
        System.out.printf((info) + "%n",
                environment.getProperty("spring.application.name"),
                Arrays.toString(environment.getActiveProfiles()),
                environment.getProperty("server.port"));
    }
}
