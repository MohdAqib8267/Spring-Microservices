# Spring-Microservices

# Spring
The Spring Framework is a powerful, open-source framework for building Java applications. It provides a comprehensive infrastructure for developing Java enterprise applications,
particularly those requiring integration with various enterprise systems. Spring offers tools and design patterns to manage common tasks, such as dependency injection, 
transaction management, data access, and more.

# Core Spring
# IoC (Inversion of Control): 
Inversion of Control (IoC) is a design principle where the control of object creation and dependency management is transferred from the application code to a
container or framework. This allows applications to be more modular, flexible, and testable. In the Spring Framework, IoC is mainly implemented through Dependency Injection (DI).

## Key Concepts of IoC
1. **Control Shift:** Traditionally, objects are responsible for finding or creating their dependencies. With IoC, an external container (like the Spring IoC container) manages this process, injecting dependencies into objects.
2. **Dependency Injection (DI):** Dependencies are "injected" into a class instead of the class creating them itself. This is typically done via constructor injection, setter injection, or field injection.

**Types of Dependency Injection in Spring**
1. **Constructor Injection:** Dependencies are injected through the constructor.
2. **Setter Injection:** Dependencies are injected through public setter methods.
3. **Field Injection:** Dependencies are directly injected into fields using annotations.

**Example of IoC and Dependency Injection in Spring**

Consider a simple example where we have a Car class that depends on an Engine class. Without IoC, the Car class would be responsible for creating the Engine instance, making it tightly coupled to the Engine implementation.
   **Without IoC (Traditional Approach)**
```
public class Engine {
    public void start() {
        System.out.println("Engine started");
    }
}

public class Car {
    private Engine engine;

    public Car() {
        // Car creates its own Engine instance
        engine = new Engine();
    }

    public void drive() {
        engine.start();
        System.out.println("Car is driving");
    }
}
```   
In this example, the Car class is directly responsible for creating an Engine instance. This makes it tightly coupled to the Engine class, which can be limiting and hard to test.

## With IoC (Using Spring and Dependency Injection)
In the IoC approach, the Spring container manages the dependencies and injects the Engine into the Car class.

- Define the Engine and Car classes without dependency creation.
- Configure the dependencies in the Spring container (either through annotations or XML configuration).

**Annotate the classes and let Spring automatically wire dependencies.**
DemoApplication.java
```
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		
		// SpringApplication is an interface which contain run method
		// this run method gives ConfigurableApplicationContext type obj
		// this obj provide by application and injected in DemoApplication class to instantiate engine object
		
//		Car car = new Car();  //if we use like this, means now I am reponsible to handle this object 
		Car car = context.getBean(Car.class); // now my framework will be responsible to inject this object
		
		// This bean() method belongs to an ApplicationContext Interface
		car.drive();
	}

}
```
Car.java

```
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // means now spring understand that, i am responsible to create the object of Car
public class Car{
	
	@Autowired // Spring injects the Engine instance here
	Engine engine;
	
	public void drive() {
		engine.start();
		System.out.println("Car is driving");
	}

}
```
**Here, @Autowired instructs Spring to inject an Engine bean when it creates a Car bean.**

Engine.java
```
package com.example.demo;

import org.springframework.stereotype.Component;

@Component    // means now spring understand that, i am responsible to create the object of Engine
public class Engine {
	public void start() {
		System.out.println("Engine Start");
	}
}
````

> Alternatively, you could use an XML file to define the beans and dependencies. (suppose i am using constructor injection)
```
<!-- applicationContext.xml -->
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Define the Engine bean -->
    <bean id="engine" class="Engine" />

    <!-- Define the Car bean and inject the Engine bean -->
    <bean id="car" class="Car">
        <constructor-arg ref="engine" /> //constructor inject pass id in ref
    </bean>

	<!-- Define the Car bean and inject the Engine bean using setter injection -->
       <bean id="car" class="Car">
        	<property name="engine" ref="engine" />
      </bean>
	<!-- <property name="engine" ref="engine" />: This element injects the Engine bean into the Car bean by calling the setEngine method. -->

</beans>
```
**Benefits of Using IoC and DI** 
- Loose Coupling: Car no longer depends on Engine directly, making the code more modular and flexible.
- Easy Testing: You can mock dependencies like Engine for testing purposes.
- Improved Maintainability: You can change dependency implementations without modifying the dependent class.

## BeanFactory
In Spring, BeanFactory is the core interface for the IoC (Inversion of Control) container, responsible for instantiating, configuring, and managing beans. A "bean" in Spring is essentially an object that is instantiated, assembled, and managed by the Spring container.

NoTE: BeanFactory is the root interface of the Spring IoC container hierarchy

**Common Implementations of BeanFactory**
- The main implementation of BeanFactory in Spring is the XmlBeanFactory, which was previously used for parsing XML configuration files to create beans. However, XmlBeanFactory is now deprecated, and ApplicationContext is generally preferred in most applications.

App.java
```
package SpringDemo.app;

import org.springframework.beans.factory.BeanFactory;

public class App 
{
    public static void main( String[] args )
    {
    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("spring.xml"));
        Alien obj = (Alien) factory.getBean("alien");
        obj.code();
    }
}
```
Alien.java
```
package SpringDemo.app;

public class Alien {
	public void code() {
		System.out.println("I m coding");
	}
}
```

spring.xml
```
<?xml version = "1.0" encoding = "UTF-8"?>

<beans xmlns = "http://www.springframework.org/schema/beans"
   xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation = "http://www.springframework.org/schema/beans
   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

//only below bean are important
   <bean id = "alien" class = "com.aqib.Alient">
     
   </bean>

</beans>
```
## ApplicationContext

ApplicationContext is a central interface (or sub-interface of the BeanFactory. Therefore, it offers all the functionalities of BeanFactory.) in the Spring framework, acting as a more powerful and feature-rich version of BeanFactory. It is the standard way to configure and manage beans in a Spring application and provides essential services such as dependency injection, event propagation, and resource management.

Advatages of ApplicationContext over BeanFactory

- Eager Initialization: By default, ApplicationContext eagerly initializes singleton beans(matlab ki kitne bhi objects bna lo container us class ka ek hi bnayega or ek hi jgah point kreyga) at startup, whereas BeanFactory does so lazily (only when a bean is requested).
- Additional Features: ApplicationContext provides features like event propagation, declarative mechanisms to create a bean, and integration with AOP (Aspect-Oriented Programming).
- Internationalization: ApplicationContext supports internationalization (i18n) through message resources.

 ```
   public static void main( String[] args )
    {
	//    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("Spring.xml"));
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
        obj.code();
    }
```
Note: **Alien and spring.xml files are same.**

## Spring Container or IOC Container

The IoC container is responsible to instantiate, configure and assemble the objects. The IoC container gets informations from the XML file and works accordingly. The main tasks performed by IoC container are:

- to instantiate the application class
- to configure the object
- to assemble the dependencies between the objects
- There are two types of IoC containers. They are:

1. BeanFactory
2. ApplicationContext

> App.java
```
public static void main( String[] args )
    {
	//    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("Spring.xml"));
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
        obj.code();
    }
```
> Alien.java
```
package SpringDemo.app;

public class Alien {
	int age;
	public void code() {
		System.out.println("I m coding");
	}
}
```
output: I am coding

**But if we create an constructor in Alien class and  comment out obj.code(), still my constructor will call but not methods. means when we instantiate class(bean), configuration of this stored in IOC container. and whenever object is required of this, it provide.**
```
Alien.java
public class Alien {
	int age;
	public Alient(){
	System.out.println("constructor call");
	}
	public void code() {
		System.out.println("I m coding");
	}
}

App.java
public static void main( String[] args )
    {
	//    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("Spring.xml"));
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
        //obj.code();
    }

Output: constructor call
```
**Now one thing, suppose if we create more than one object of same bean, these beans are pointing on same configuration on container, That's why called it singleton bean.**
```
Alien.java
public class Alien {
	int age;
	public Alient(){
	System.out.println("constructor call");
	}
	public void code() {
		System.out.println("I m coding");
	}
}

App.java
public static void main( String[] args )
    {
	//    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("Spring.xml"));
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
	obj.code();
	obj.age=30;
	System.out.println(obj.age);

	Alien obj2 = (Alien) factory.getBean("alien");
	obj2.code();
	System.out.println(obj2.age); // obj2 pointing to same configuration whereage is 30, because singleton bean hai. mtlab kitne b objects ho sabke liye same hogi, normal objects me alag alag objects ki alag alag hoti hai.
    }

Output:
constructor call
I am coding
30

I am coding
30
```
But if you want multiple instance, then we need to **change scope to prototype** in spring.xml file. by **default score is singleton.**
```
   <bean id = "alien" class = "com.aqib.Alient" scope="prototype">
     
   </bean>
```
now if we create 10 objects, container gives us 10 different instanse of bean. but now container will call contstructor untill you request. so now 
```
Alien.java
public class Alien {
	int age;
	public Alient(){
	System.out.println("constructor call");
	}
	public void code() {
		System.out.println("I m coding");
	}
}

App.java
public static void main( String[] args )
    {
	//    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("Spring.xml"));
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
	obj.code();
	obj.age=30;
	System.out.println(obj.age);

	Alien obj2 = (Alien) factory.getBean("alien");
	obj2.code();
	System.out.println(obj2.age); // obj2 pointing to another instanse of class
    }

Output:
constructor call
I am coding
30

constructor call
I am coding
0
```
but now container will not call contstructor untill you request. so now but suppose if i comment out all objects.
```
Alien.java
public class Alien {
	int age;
	public Alient(){
	System.out.println("constructor call");
	}
	public void code() {
		System.out.println("I m coding");
	}
}

App.java
public static void main( String[] args )
    {
	//    	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("Spring.xml"));
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
	//obj.code();
	//obj.age=30;
	//System.out.println(obj.age);

	Alien obj2 = (Alien) factory.getBean("alien");
	//obj2.code();
	//System.out.println(obj2.age); // obj2 pointing to another instanse of class
    }
Output:                (nothing will print even constructor)
```
