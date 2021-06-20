package org.scavino;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.scavino"})
public class AppConfig {


    /**
     * ViewResolver allows setting properties such as
     * prefix (/WEB-INF/views/) or suffix (*.jsp) to the view name to generate
     * the final view page URL. In our case this is how
     * we will configure JSP pages.
     *
     * @return JSP resolver with prefix set to /WEB-INF/views/
     */
    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
