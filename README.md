# PampaNet Web Shooters 
[![Build Status](https://travis-ci.org/pabiagioli/webshooters.svg?branch=master)](https://travis-ci.org/pabiagioli/webshooters) 
[![Coverage Status](https://coveralls.io/repos/pabiagioli/webshooters/badge.svg)](https://coveralls.io/r/pabiagioli/webshooters) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pampanet/webshooters/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pampanet/webshooters/)

Web Framework for developing Secure Java WebApps

Dependencies included
---------------------
- [Servlet 3.1.0](http://java.net/projects/servlet-spec/pages/Home)
- [JBoss RestEasy 3.0.9.Final](http://resteasy.jboss.org) 
- [Guice 3.0](http://github.com/google/guice)
- [Apache Shiro 1.2.3](http://shiro.apache.org)
- [JUnit 4.10](http://junit.org/)
- [Logback + SLF4J XLogger](http://logback.qos.ch)

Requirements
------------
- Java 8
- Maven 3
- Application Server (Tomcat 9(?), Jetty 9 Recommended)

##Features
- Java 8 compliant
- Servlet 3.x Asynchronous Requests and Filters
- Google Guice Dependency Injection
- Apache Shiro Security Context
- JBoss RestEasy with JAX-RS 2.x compatibility

##Usage
- Insert this dependency in your pom.xml:
```xml
<dependency>
  <groupId>com.pampanet</groupId>
  <artifactId>webshooters</artifactId>
  <version>0.1.1</version>
</dependency>
```
- In your <code>web.xml</code> or Jetty configuration put the following classes in this strict order (any other filters would be added between the ShiroFilter and RestEasyFilter)
 - Listener SubType of [GenericGuiceRestEasyContextListener.java](https://github.com/pabiagioli/webshooters/blob/master/src/main/java/com/pampanet/webshooters/config/GenericGuiceRestEasyContextListener.java)
 - Filter SubType of [GuiceRestEasyShiroFilter.java](https://github.com/pabiagioli/webshooters/blob/master/src/main/java/com/pampanet/webshooters/servlet/filter/GuiceRestEasyShiroFilter.java)
 - Filter SubType of [GuiceRestEasyFilterDispatcher.java](https://github.com/pabiagioli/webshooters/blob/master/src/main/java/com/pampanet/webshooters/servlet/filter/GuiceRestEasyFilterDispatcher.java)
- Create at least one properties file in <code>src/main/resources</code> with values for the scanner

### Example

#### Listener
```java
public class MyContextListener extends com.pampanet.webshooters.config.DefaultServletContextListener{

	private XLogger logger = XLoggerFactory.getXLogger(MyContextListener.class);

	@Override
	protected List<? extends Module> getModules(ServletContext context) {
		logger.entry(context);
		return Arrays.asList(new BootstrapPropertiesModule("bootstrap.properties"), new RequestScopeModule(), new DefaultShiroModule(context), new ShiroAopModule(),new BootstrapRestPackagesModule("/bootstrap.properties","com.pampanet.webshooters.config.rest.pkgs"));
	}
```

#### bootstrap.properties
```properties
com.pampanet.webshooters.config.rest.pkgs=com.pampanet.sample.rest
```

#### web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app id="WebApp_ID" version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
<display-name>Secure Async Webapp Project</display-name>
  <listener>
  	<listener-class>com.pampanet.sample.config.MyContextListener</listener-class>
  </listener>
  <filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>com.pampanet.webshooters.servlet.filter.GuiceRestEasyShiroFilter</filter-class>
    <async-supported>true</async-supported>
  </filter>
  <filter-mapping>
    <filter-name>shiroFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <filter>
   <filter-name>restEasyFilter</filter-name>
   <filter-class>com.pampanet.webshooters.servlet.filter.GuiceRestEasyFilterDispatcher</filter-class>
   <async-supported>true</async-supported>
  </filter>
  <filter-mapping>
   <filter-name>restEasyFilter</filter-name>
   <url-pattern>/*</url-pattern>
  </filter-mapping>
</web-app>
```

##License
- Apache 2.0 License

##Sample Server
- See [AppTest.java](https://github.com/pabiagioli/webshooters/blob/master/src/test/java/com/pampanet/webshooters/AppTest.java) Integration Test for Jetty Server Reference

##Next Steps
- More documentation
- Customizable Default Values
- More stuff...
