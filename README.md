# Deploying Spring 5.x and Spring Boot 2.x Applications on WAS 8.5.X

## Problems

### Problem 1
### Spring 5 and Spring Boot 2.x use updated Java Validation & Hibernate Validation Jars than those found on WAS
By default, WebSphere Application Server 8.5.x ( WAS8) uses the PARENT_FIRST class loader strategy. In general this
means WAS will load its version of application server (EE) jars first and prioritize itself over what is in your
distribution.

For example, you may have one version of the validation api in your WEB-INF\lib directory but the class path will 
favor the WAS one first. Effectively shadow-banning your version in favor of the JEE version that came with WAS. This
make sense, IBM spent a lot of time certifying and testing their application server with a known set of javax.* jars
and it wants to keep it that way by default.

The rub comes when you want to use a newer version of Spring 5.x and Spring Boot 2.x and those versions are using and 
rely on a newer version of javax valtidation and hibernate validation for their core functionality. What do you do?

#### Solution 1 - Upgrade Your Server
Option 1 is to upgrade your application server. This rather straight forward with 
[Apache Tomcat](http://tomcat.apache.org/), [Open Liberty](https://openliberty.io/), [JBoss](https://www.wildfly.org/)
and many 
other open source implementations. This because a great challenge with WAS. Many critical and legacy applications
have been running WAS 8.5 with JDK 8.x for years, and the business has little desire to pay for some expensive regression 
testing. Sure Spring 4.x is end of life, but it still works, so why upgrade? The best 
answer is not that 5.x is cooler and that it helps my resume. The real answer is vulnerabilities. You are no longer
supported and that is a problem when, not if, new vulnerabilities are found in the wild. You want to get ahead of that
scenario.

#### Solution 1.1 - Upgrade to WAS 9
What not WAS 9? Well I leave that up to you. Personally if you are migrating away from WAS 8. Unless you have some
dying need for custom WAS 8 functionality the above open source implementations will do just fine. You can deploy
as many as you want in as many places as you want on any environment, cloud. container or on-prem. Your choice and
your CFO won't care.

#### Solution 2 - Change Your ClassLoader Policy
The option we are going to address here is just switching the classloader from PARENT FIRST to PARENT LAST. This is not
a long term solution. This just gets you to the supported of Spring and your business can "spring" for that changes.
You get it "spring", I am being punny. Also this is not a silver bullet. I am sure your infrastructure teams have invested
in their scripting and have well running nodes in stable clusters. Deploying a new node with PARENT LAST may affect
other application that are not migrating but are in the same CELL. What do you do there?

There are many ways to accomplish this task. We will not delve into those specifics. I will give you three (3) high 
level solutions and let you pick.

##### Solution 2.1 - Update deployment.xml in EAR
This is the riskiest and least documented. Here you update some obscure xml file in your EAR with IBM specific 
annotations and hope for the best. The upside is you do not need to alter your scripts. The downside? I have not 
been able to prove this works in production environments.  Below are some stack overflow articles on the topic. 
Buyer Beware

* [Stack Overflow - how-to-set-java-class-loader-parent-last](https://stackoverflow.com/questions/21421916/how-to-set-java-class-loader-parent-last)

##### Solution 2.2 - Update Classloader via UI (WAS Admin)
This one works and works great with developer versions of WAS or if you have access to the WAS admin console. This 
option is a great way to prove out your changes. The major problem of course is these changes are not scalable nor
can be scripted. You may like this option but unlikely your infrastructure teams will happily update your EAR after
every module deployment. THis is not a scable option.

* [Class loading and update detection settings](https://www.ibm.com/docs/en/was/8.5.5?topic=application-class-loading-update-detection-settings)
* [Enhanced EAR deployment and the isolated class loader attribute for a shared library](https://www.ibm.com/support/pages/enhanced-ear-deployment-and-isolated-class-loader-attribute-shared-library)
* [Configuring the Classloader for your WebSphere Application](http://www.craigstjean.com/2015/05/configuring-the-classloader-for-your-websphere-application-2/)
* [IBM Websphere Classloading set to PARENT_LAST](http://tugrulaslan.com/ibm-websphere-classloading-set-to-parent_last/)

##### Solution 2.3 - Update Deployment Scripts
This really is your only option for production. You will need to work hand in hand with your infrastructure teams to update
their deployment scripts to change the classloader during deployment and before starting the application. I have included
a sample developer's ( which means not highly tested) jython (py) file to get the point across to your partners.

You can see my inspiration for the jython script from here

* [modifying-war-class-loader-mode-using-wsadmin-scripting](https://www.ibm.com/docs/en/was-nd/8.5.5?topic=caus-modifying-war-class-loader-mode-using-wsadmin-scripting)

```python
print ("Get Deployments : ")
deployments = AdminConfig.getid('/Deployment:' + APP_NAME + '/')
print deployments
print ("")

print ("Deployment Object")
deploymentObject = AdminConfig.showAttribute(deployments, 'deployedObject')
print deploymentObject

myModules = AdminConfig.showAttribute(deploymentObject, 'modules')
myModules = myModules[1:len(myModules)-1].split(" ")
print myModules
for module in myModules:
     if (module.find('WebModuleDeployment')!= -1):
        AdminConfig.modify(module, [['classloaderMode', 'PARENT_LAST']])

AdminConfig.save()
print ("Set module to PARENT_LAST")
```

### Problem 2
### POM Needs to Updated to Deploy Correct Validation Jars
This change is totally on you. You will need a developer server or local server to see and validate your changes, but
you need to update your *pom.xml* with specific changes for validation and hibernate.

### Solution 3 - Update your Java Validation 2.x for Spring 5.x
After your first you deploy, and after changing the class loader you will see the below error in the SystemOut logs:

*Caused by: java.lang.ClassNotFoundException: javax.validation.ParameterNameProvider*

Spring 5.x relies on version 2.x of the *validtion-api* so you will need to explicitly upgrade the api in your pom. Again
this is only possible because you changed the class loader. Without that change you are stuck loading version 1.x from
the WAS class path. There is no work-around. There are other changes if you are using JSP pages. See the pom.xml in the 
WAS8-war project.

Special note **if you are using tiles 2.x you will need to upgrade to tiles 3.x. That will be another post.**

```xml
    <dependency>
        <groupId>javax.validation</groupId>
        <artifactId>validation-api</artifactId>
        <version>2.0.1.Final</version>
    </dependency>
```

While not WAS specific, we need to include the tomcat-embed-jasper dependency to allow
our application to compile and render JSP pages for local running. Not the *scope* is set to **provided**

```xml
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <version>${tomcat-embed-jasper.version}</version>
            <scope>provided</scope>
        </dependency>
```

### Solution 3.1 - Hibernate Validation 2.x for Spring Boot 2.x
See the Spring Boot 2.x [README.md](./was8-sb-war/README.md) for the additional JARs required.

## JSP Configuration

### Spring 5.x JSP Configuration Using Java Config
You are using Java config if you have gone this far. Forget the old style xml configuration. That was so Spring 4.x. You
are better than that now. 

*Please note the location for the JSP pages :* **/WEB-INF/views/**

```java
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
```

We are extending the [AbstractAnnotationConfigDispatcherServletInitializer](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/support/AbstractAnnotationConfigDispatcherServletInitializer.html)
to have access to the key configuration. Why? We can use the convenience classes 
provided by Spring instead of manually configuring the DispatcherServlet and/or ContextLoaderListener

* [When use AbstractAnnotationConfigDispatcherServletInitializer and WebApplicationInitializer?](https://stackoverflow.com/questions/26676782/when-use-abstractannotationconfigdispatcherservletinitializer-and-webapplication)

```java
public class SpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class <?> [] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class <?> [] getServletConfigClasses() {
        return new Class[] {
                AppConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
                "/"
        };
    }
```




