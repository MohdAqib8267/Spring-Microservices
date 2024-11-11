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
