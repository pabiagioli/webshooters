# PampaNet Web Shooters [![Build Status](https://travis-ci.org/pabiagioli/webshooters.svg?branch=master)](https://travis-ci.org/pabiagioli/webshooters) [![Coverage Status](https://coveralls.io/repos/pabiagioli/webshooters/badge.svg)](https://coveralls.io/r/pabiagioli/webshooters) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pampanet/webshooters/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.pampanet/webshooters/)

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
  <version>0.1.0</version>
</dependency>
```
- In your <code>web.xml</code> or Jetty configuration put the following classes in this strict order (any other filters would be added between the ShiroFilter and RestEasyFilter)
 - Listener SubType of [GenericGuiceRestEasyContextListener.java](https://github.com/pabiagioli/webshooters/blob/master/src/main/java/com/pampanet/webshooters/config/GenericGuiceRestEasyContextListener.java)
 - Filter SubType of [GuiceRestEasyShiroFilter.java](https://github.com/pabiagioli/webshooters/blob/master/src/main/java/com/pampanet/webshooters/servlet/filter/GuiceRestEasyShiroFilter.java)
 - Filter SubType of [GuiceRestEasyFilterDispatcher.java](https://github.com/pabiagioli/webshooters/blob/master/src/main/java/com/pampanet/webshooters/servlet/filter/GuiceRestEasyFilterDispatcher.java)

##License
- Apache 2.0 License

##Sample Server
- See [AppTest.java](https://github.com/pabiagioli/webshooters/blob/master/src/test/java/com/pampanet/webshooters/AppTest.java) Integration Test for Jetty Server Reference

##Next Steps
- More documentation
- Customizable Default Values
- More stuff...
