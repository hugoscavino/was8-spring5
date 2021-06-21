# WAS 8.5.X and Spring Boot 2.x 

## Configuring WAS 8.X with Spring Boot 2.x and PARENT LAST on WebSphere

## Running the Application Locally
You will need to run the application using the **spring-boot:run** maven plugin so that all the dependant JAR
will be loaded. If you attempt to run the project using just the Application class you will get an *404* error
with no explanation or errors in the logs. I believe there must be a way to load the correct JARs and not use the
plugin. I gave up. If you find a way please fork this [repo](https://github.com/hugoscavino/was8-spring5) 
and make the changes.

### Class Loader Changes
Please review the comments in the WAS8 [Spring 5.x README.md](../README.md) file with those details. This section 
covers just the changes required for Spring Boot 2.x

### Pom Changes
Again review the [Spring 5.x README.md](../README.md) file to see the changes for Validation. For Spring Boot 2.x
there is one

### Solution - Hibernate Validation 2.x for Spring Boot 2.x
In addition to the Spring 5.x changes, you will need to upgrade your hibernate validation to at least
version 6.x and also add an implementation javax.el. Why? Well the Validation starter no longer included in web 
starters for the latest Spring Boot. No big whoop.

* [Spring-Boot-2.3-Release-Notes#validation-starter-no-longer-included-in-web-starters](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#validation-starter-no-longer-included-in-web-starters)

```xml
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>6.1.7.Final</version>
    </dependency>
```

In addition, you will need an implementation that is compatible with your version of Hibernate. You will know this when
you see the below error:

*Caused by: java.lang.NoClassDefFoundError: javax/el/ELManager*

```xml
        <dependency>
            <groupId>org.glassfish</groupId>
            <artifactId>javax.el</artifactId>
            <version>3.0.1-b08</version>
        </dependency>
```

### Java Config Changes
The subtle and important change is to extend the *SpringBootServletInitializer* class. Without this change
the application will start but the JSP pages will not load and you will be haunted by an *404* error.

```java
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
```

Also do not forget to setup your JSP configuration using application.properties or application.yml. PLEASE, PLEASE note
the directory here is using the maven default of */WEB-INF/jsp/*

```properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
logging.level.org.springframework.boot.autoconfigure=ERROR
```
