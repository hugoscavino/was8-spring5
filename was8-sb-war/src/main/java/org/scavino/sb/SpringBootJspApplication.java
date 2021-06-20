package org.scavino.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "org.scavino")
public class SpringBootJspApplication extends SpringBootServletInitializer {

    /**
     * To deploy under WAS or any a web container like Tomcat, Liberty etc,
     * extend SpringBootServletInitializer, binding the application's Servlet,
     * Filter, and ServletContextInitializer to the application server
     *
     * @param builder SpringApplicationBuilder used so that application can be used
     *                in a container
     * @return Our SpringApplicationBuilder for WAS
     */

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootJspApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJspApplication.class, args);
    }
}
