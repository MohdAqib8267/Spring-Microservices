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
</beans>
```
**Benefits of Using IoC and DI** 
- Loose Coupling: Car no longer depends on Engine directly, making the code more modular and flexible.
- Easy Testing: You can mock dependencies like Engine for testing purposes.
- Improved Maintainability: You can change dependency implementations without modifying the dependent class.
