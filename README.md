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

## Dependency Injection by Setter Method
We can inject the dependency by setter method also. The <property> subelement of <bean> is used for setter injection. Here we are going to inject

- primitive and String-based values
- Dependent object (contained object)
- Collection values etc

Employee.java
It is a simple class containing three fields id, name and city with its setters and getters and a method to display these informations.
```
package com.javatpoint;  
  
public class Employee {  
private int id;  
private String name;  
private String city;  
  
public int getId() {  
    return id;  
}  
public void setId(int id) {  
    this.id = id;  
}  
public String getName() {  
    return name;  
}  
public void setName(String name) {  
    this.name = name;  
}  
  
public String getCity() {  
    return city;  
}  
public void setCity(String city) {  
    this.city = city;  
}  
void display(){  
    System.out.println(id+" "+name+" "+city);  
}  
}
these setters are called by property sublement
```
**spring.xml**
We are providing the information into the bean by this file. The property element invokes the setter method . The value subelement of property will assign the specified value.
```
<?xml version="1.0" encoding="UTF-8"?>  
<beans  
    xmlns="http://www.springframework.org/schema/beans"  
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
    xmlns:p="http://www.springframework.org/schema/p"  
    xsi:schemaLocation="http://www.springframework.org/schema/beans  
                http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">  
  
<bean id="obj" class="com.javatpoint.Employee">  
<property name="id">  
<value>20</value>  
</property>  
<property name="name">  
<value>Arun</value>  
</property>  
<property name="city">  
<value>ghaziabad</value>  
</property>  
  
</bean>  
  
</beans>
```

Test.java
```
package com.javatpoint;  
  
import org.springframework.beans.factory.BeanFactory;  
import org.springframework.beans.factory.xml.XmlBeanFactory;  
import org.springframework.core.io.*;  
  
public class Test {  
    public static void main(String[] args) {  
          
       ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Employee e = (Employee) context.getBean("Employee");
	e.display(); 
          
    }  
}
```
**Ref Attribute:** Ref attribute is used to assign the refernce of another class, and now you can use methods of referenced class. To do this you need to pass **ref** parameter in property sublement and assign class name which you want to referred.

This is an example of how we can use setter injection of user defined data types.

```
// spring.xml (Inside src)
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="alien" class="SpringDemo.app.Alien">
        <property name="age" value="10"></property>   //primitive property
        <property name="laptop" ref="laptop"></property>  //referenced proprty
    </bean>
    <bean id="laptop" class="SpringDemo.app.Laptop">
    
    </bean>
</beans>

//Laptop.java
package SpringDemo.app;

public class Laptop {
	public void compiled() {
		System.out.println("Compiled..");
	}
}

//Alien.java
package SpringDemo.app;

public class Alien {
	private Laptop laptop;
	
	private int age;
	
	public Alien() {
		System.out.println("Alien object is created");
	}
	
	public Laptop getLaptop() {
		return laptop;
	}
	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
	}
	public int getAge() {
		System.out.println("Age is assigned");
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void code() {
		System.out.println("I m coding");
		laptop.compiled();
	}
}

//App.java
package SpringDemo.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App 
{
    public static void main( String[] args )
    {
    	
    	ApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        Alien obj = (Alien) factory.getBean("alien");
        obj.code();
        
        System.out.println(obj.getAge());
    }
}
```

### Constructor Injection:
We can inject the dependency by constructor. The <constructor-arg> subelement of <bean> is used for constructor injection. This means that the dependencies are injected when the object is created, and they are typically declared in the Spring configuration file or using annotations.

```
// Alien.java
public class Alien {
    private Planet planet;

    // Constructor for dependency injection
    public Alien(Planet planet) {
        this.planet = planet;
    }

    public void displayPlanet() {
        System.out.println("Alien is from " + planet.getName());
    }
}

// Planet.java
public class Planet {
    private String name;

    public Planet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```
xml config
```
<!-- Define Planet bean -->
    <bean id="planet" class="Planet">
        <constructor-arg value="Mars"/>
    </bean>

    <!-- Define Alien bean and inject Planet through constructor -->
    <bean id="alien" class="Alien">
        <constructor-arg ref="planet"/>
    </bean>
```


# Spring JDBC

SpringJdbcApplication.java
```
import com.aqib.SpringJDBC.modal.Alien;
import com.aqib.SpringJDBC.repo.AlienRepo;

@SpringBootApplication
public class SpringJdbcApplication {

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(SpringJdbcApplication.class, args);
		Alien alien1=context.getBean(Alien.class);
		
		alien1.setId(5);
		alien1.setEmail("mpohd@gmail.com");
		alien1.setName("Aqib");
		
		AlienRepo repo = context.getBean(AlienRepo.class);
		repo.save(alien1);
		
		System.out.println(repo.findAll());
		
	}

}
```
Alien.java (modal)
```
package com.aqib.SpringJDBC.modal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Alien {
	private int id;
	private String name;
	private String email;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Alien [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
}
```
AlienRepo.java(package=com.aqib.SpringJDBC.repo)
```
package com.aqib.SpringJDBC.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.aqib.SpringJDBC.modal.Alien;

@Repository
public class AlienRepo {
	
	@Autowired
	private JdbcTemplate template;
	

	public JdbcTemplate getTemplate() {
		return template;
	}
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	public void save(Alien alien) {
	    String sql = "INSERT INTO alien (id, name, email) VALUES (?, ?, ?)";
	    int rows = template.update(sql, alien.getId(), alien.getName(), alien.getEmail());
	    System.out.println("Rows affected: " + rows);
	}

	public List<Alien> findAll(){
		String sql = "select * from alien where id>1";
		
		RowMapper<Alien> mapper = new RowMapper<Alien>() {
			@Override
			public Alien mapRow(ResultSet rs, int rowNum) throws SQLException{
				Alien a = new Alien();
				a.setId(rs.getInt("id"));
				a.setName(rs.getString("name"));
				a.setEmail(rs.getString("email"));
				return a;
			}
		};
		List<Alien> aliens = template.query(sql, mapper);
		return aliens;
	}
}
```
resource>Schema.sql
```
create table alien(
	id int primary key,
	name varchar(20),
	email varchar(100)
);
```
resource>data.sql
```
insert into alien(id,name,email) values(1,'jack','jack@gmail.com');
insert into alien(id,name,email) values(2,'john','john@gmail.com');
```



# Spring Security

Spring Security is a framework that provides **authentication, authorization, and protection against common attacks.** With first class support for securing both **imperative** and **reactive applications**.

**Imperative Applications**: Servlet Applications are imperative applications.

Spring Security integrates with the Servlet Container by using a standard Servlet Filter. This means it works with any application that runs in a Servlet Container. More concretely, you do not need to use Spring in your Servlet-based application to take advantage of Spring Security.

For Eg: when we import spring security dependency in our application, and try to go localhost on browser, we got a login form which contains two feilds.
- 1. username (user by default)
  2. password (generated by default and can see on terminal of IDE)

we can change username and password in application.properties, but it would be only for a single user.

```
spring.security.user.name=Aqib
spring.security.user.password=Aqib8267
```
**But question is why this login page coming?**

So, the answer is due to Filters, spring security allows a filter chain in servlet container, this chain containe multiple filters, some filters are related to login logout filters, and these login filter send this login page if user is not logged in. below images we have a filter chain containing filters f1,f2,f3.....

when user logged in, it generate a session ID, that stores in cookies, and this session is maintains over the instance of browser.

in filters we can modify request and response.

![Screenshot 2024-12-01 192148](https://github.com/user-attachments/assets/7ba549cf-031c-49f7-9c46-e506103c319e)

we can get this session ID using HttpServletRequest object 
```
@GetMapping("/")
public String greet(HttpServletRequest request){
	return "Hello my session ID is: "+ request.getSession().getId();
}
```
> For postman we can login in Autorization and give keys (username and password)

## CSRF (Cross Site Request Forgery):
**What is a CSRF Attack?**

The best way to understand a CSRF attack is by taking a look at a concrete example.

Assume that your bank’s website provides a form that allows transferring money from the currently logged in user to another bank account. For example, the transfer form might look like:

Transfer form
```
<form method="post"
	action="/transfer">
<input type="text"
	name="amount"/>
<input type="text"
	name="routingNumber"/>
<input type="text"
	name="account"/>
<input type="submit"
	value="Transfer"/>
</form>
```
The corresponding HTTP request might look like:

```
Transfer HTTP request
POST /transfer HTTP/1.1
Host: bank.example.com
Cookie: JSESSIONID=randomid
Content-Type: application/x-www-form-urlencoded

amount=100.00&routingNumber=1234&account=9876
```
Now pretend you authenticate to your bank’s website and then, without logging out, visit an evil website. The evil website contains an HTML page with the following form:

Evil transfer form
```
<form method="post"
	action="https://bank.example.com/transfer">
<input type="hidden"
	name="amount"
	value="100.00"/>
<input type="hidden"
	name="routingNumber"
	value="evilsRoutingNumber"/>
<input type="hidden"
	name="account"
	value="evilsAccountNumber"/>
<input type="submit"
	value="Win Money!"/>
</form>
```
You like to win money, so you click on the submit button. In the process, you have unintentionally transferred $100 to a malicious user. This happens because, while the evil website cannot see your cookies, the cookies associated with your bank are still sent along with the request.(kyuki wo us session ID ki help se bank ko authentic user lgega and bank money transfer kr dega).

Worse yet, this whole process could have been automated by using JavaScript. This means you did not even need to click on the button. Furthermore, it could just as easily happen when visiting an honest site that is a victim of a XSS attack. So how do we protect our users from such attacks?


**Protecting Against CSRF Attacks**

The reason that a CSRF attack is possible is that the HTTP request from the victim’s website and the request from the attacker’s website are exactly the same. This means there is no way to reject requests coming from the evil website and allow only requests coming from the bank’s website. To protect against CSRF attacks, we need to ensure there is something in the request that the evil site is unable to provide so we can differentiate the two requests.

Spring provides two mechanisms to protect against CSRF attacks:

- 1.The Synchronizer Token Pattern (CSRF Token)
- 2.Specifying the SameSite Attribute on your session cookie (means only same site can access)

Both protections require that **Safe Methods be Read-only**.(means that requests with the HTTP GET, HEAD, OPTIONS, and TRACE methods should not change the state of the application.(inme csrf token ki zrurat nhi, q ki we want external website can read it and also help this to leakage the token))

**Synchronizer Token Pattern**
we can see a **_csrf** token (hidden type) in servlet form.

When an HTTP request is submitted, the server must look up the expected CSRF token and compare it against the actual CSRF token in the HTTP request. If the values do not match, the HTTP request should be rejected.

basically in the http request we have add token with key "X-XSRF-TOKEN", and for post,put and delete methods. send this token value.

```
public CsrfToken getCsrfToken(HttpServletRequest req){
	return (CsrfToken) req.getAttribute("_csrf");
}
```

So, server will check _csrf token and it will be present only authentic website form
```
Synchronizer Token Form
<form method="post"
	action="/transfer">
<input type="hidden"
	name="_csrf"
	value="4bfd1575-3ad1-4d21-96c7-4ef2d9f86721"/>
<input type="text"
	name="amount"/>
<input type="text"
	name="routingNumber"/>
<input type="hidden"
	name="account"/>
<input type="submit"
	value="Transfer"/>
</form>
````
The form now contains a hidden input with the value of the CSRF token. External sites cannot read the CSRF token since the same origin policy ensures the evil site cannot read the response.

The corresponding HTTP request to transfer money would look like this:

Synchronizer Token request
```
POST /transfer HTTP/1.1
Host: bank.example.com
Cookie: JSESSIONID=randomid
Content-Type: application/x-www-form-urlencoded

amount=100.00&routingNumber=1234&account=9876&_csrf=4bfd1575-3ad1-4d21-96c7-4ef2d9f86721
```

## Spring Security Configuration
Basically we have implemented security, but most of the securities are implemented by default, but we can customize these eg, login page etc.

By default spring security provide FilterChain, but i want to customize it, and we can do this by creating a **Config Class**

> create package (config) -> SecurityConfig.java

```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // this tells, this is a Configuration class
@EnableWebSecurity  // it's enable that we have use our securities

public class SecurityConfig{
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(customizer -> customizer.disable()) //disable csrf 
		     .authorizeHttpRequest(request -> request.anyRequest().authenticated()) //authenticate each request
		     .httpBasic(customizer.withDefaults()) // enable postman & (browser form only one time) for taking username and password
		     .sessionManagement(session -> session.sessionCreationPolicy(sessionCreationPolicy.STATELESS)); // its enable session stateless, will generate a new session ID for each request, that's why we have disabled csrf


//for browser we can enable form on browser
// http.formLogin(customizer.withDefaults()) // but this form will get each time because we get a new id each time

		return http.build();
	}
}
```

Note: please read below article for a solid understanding.
> https://spring.io/guides/gs/securing-web


## Spring Security User from Database
By Default user details handled by UserDetailsService, but we can customise this, so we have create this and all done

> SecurityConfig.java 

```
package com.telusko.part29springsecex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;  
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Bean
public UserDetailsService userDetailsService(){
	// This UserDetailsService is an interface, so InMemoryUserDetailsManager class implement this interface

	UserDetails user1 = User
				.withDefaultPasswordEncoder()
				.username("kiran")
				.password("k@123")
				.roles("USER")
				.build();   // basically this build return object of UserDetils type
	UserDetails user1 = User
				.withDefaultPasswordEncoder()
				.username("jack")
				.password("j@123")
				.roles("ADMIN")
				.build();
	return new InMemoryUserDetailsService(user1,user2); // in this object we have pass multiple users in the constructor
}

```
> But we also we have used hardcoded value, user can login only above details

Now we want to use with database, so when user send details, it goes to an "Un-Autheticated Object", it goes to "Authentication Provider" that will provide services and authenticated and making it "Authenticated Object", by default it's running behind the scene, but we want to use our **custome Authentication Provider** and this **Authentication provider** used to connect with **Database**

<img width="523" alt="image" src="https://github.com/user-attachments/assets/a69682e1-be68-4318-9fc4-1f8d9b7e3a3a">


So, we have different type of **AuthenticationProvider**, but we will use **DaoAuthenticationProvider**, because it is used for Database.

> For complete code, follow below repo
> https://github.com/navinreddy20/spring6yt/tree/main/Part33-Spring%20Security%206%20Verfiy%20User%20from%20Database/src/main/java/com/telusko/part29springsecex

## JWT

**Authentication** refers to the process of verifying the identity of a user, based on provided credentials. A common example is entering a username and a password when you log in to a website. You can think of it as an answer to the question Who are you?.
  
**Authorization** refers to the process of determining if a user has proper permission to perform a particular action or read particular data, assuming that the user is successfully authenticated. You can think of it as an answer to the question Can a user do/read this?.

**Principle** refers to the currently authenticated user.

**Granted authority** refers to the permission of the authenticated user.

**Role** refers to a group of permissions of the authenticated user

#### Spring Security Architecture

![images](https://github.com/user-attachments/assets/52c9baef-237d-4014-92a2-ada98b7e4bb6)
Now, let’s break down this diagram into components and discuss each of them separately.

**Spring Security Filters Chain**

When you add the Spring Security framework to your application, it automatically registers a filters chain that intercepts all incoming requests. This chain consists of various filters, and each of them handles a particular use case.

**AuthenticationManager**

You can think of AuthenticationManager as a coordinator where you can register multiple providers, and based on the request type, it will deliver an authentication request to the correct provider.

**AuthenticationProvider**

AuthenticationProvider processes specific types of authentication. Its interface exposes only two functions:

- **authenticate** performs authentication with the request.
- **supports** checks if this provider supports the indicated authentication type.
  
One important implementation of the interface that we are using in our sample project is DaoAuthenticationProvider, which retrieves user details from a UserDetailsService.

**UserDetailsService**

UserDetailsService is described as a core interface that loads user-specific data in the Spring documentation.

In most use cases, authentication providers extract user identity information based on credentials from a database and then perform validation. Because this use case is so common, Spring developers decided to extract it as a separate interface, which exposes the single function:

- **loadUserByUsername** accepts username as a parameter and returns the user identity object.
