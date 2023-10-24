# Registration and Login System with JWT

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/gustavoh430/loginproject/blob/main/LICENCE) 



![Header](https://github.com/gustavoh430/loginproject/blob/main/Technologies%20Used.PNG)




## What's in the box?

This project has been built up based on Logical layers of a Web Service as follows:

![Header](https://github.com/gustavoh430/loginproject/blob/main/ServiceLayer.drawio.png)

This is a registration and login system using JWT to authenticate. It has got one Controller layer that includes all operations available, that are: Sign in, Sign up, User Information retrieving and User Role Setting up.
There will be a service layer which will orchestrate all service callings between the controllers and the database repository. Of course, I could just call the repository directly through the Controller, but I thought it would be the most suitable, for this case, to create an intermediate class. This class is responsible for containing all business rules.


# User Resource (Controllers)

## Sign Up (HTTP POST)


### Endpoint
```text
/login/signup
```

### Payload
```json
{
    "name": "Gustavo",
    "age": "34",
    "email": "gustavoh430@gmail.com",
    "password": "teste123"

}
```
### Response
```json

"User registered successfully"
```

## Sign In (HTTP POST)

### Endpoint
```text
/login/signin
```

### Payload
```json
{
    "email": "gustavoh430@gmail.com",
    "password": "teste123"
}
```

### Response
```json
{
    "accessToken": "<ACCESS_TOKEN>",
    "tokenType": "Bearer "
}
```

## User Information Retrieving (HTTP GET)

You must be logged in with a valid JwT token*

### Endpoint
```text
/login/{email}
```

### Response
```json
{
    "id": 1,
    "name": "Gustavo",
    "age": "34",
    "email": "gustavoh430@gmail.com",
    "password": "$2a$10$G8u.55if26heBdmte13fw.anY0PjoP//H/m1SPx.aOl1/gXA2PwR.",
    "passwordConfirm": null,
    "roles": [
        {
            "id": 2,
            "name": "USER",
            "authority": "USER"
        }
    ],
    "enabled": true,
    "username": "Gustavo",
    "authorities": [
        {
            "id": 2,
            "name": "USER",
            "authority": "USER"
        }
    ],
    "credentialsNonExpired": true,
    "accountNonExpired": true,
    "accountNonLocked": true
}
```


## User Role Setting up (HTTP POST)

In this case, you do not need to be logged in with a valid JwT token, but this is customizable (Of course, that in a real project this functionality would only be accessible to the highest authority).

### Endpoint
```text
/login/authorities
```

### Payload
```json
{
    "email": "gustavoh430@gmail.com",
    "role": "ADMIN"
}
```

### Response
```text

"Authorithy has been changed successfully"

```



# User Service Layer


As mentioned before, this class is responsible for holding all business rules related to the project. This layer must also treat the exceptions that can ocurr during service callings.


```java
@Service
public class UsersService {

	@Autowired
	UsersRepository userRepository;
	@Autowired
	RolesRepository rolesRepository;

	public Users findByEmail(String email) {
		Optional<Users> obj = Optional.of(userRepository.findByEmail(email));
		return obj.orElseThrow(() -> new ResourceNotFoundException(email));
	}

	public Users findByPassword(String password) {
		Optional<Users> obj = Optional.of(userRepository.findByPassword(password));
		return obj.orElseThrow(() -> new ResourceNotFoundException(password));
	}

	public boolean nonNullFields(String users) {
		Boolean obj = users.isBlank();
		return obj;

	}

	public Boolean existsByEmail(String username) {
		return userRepository.existsByEmail(username);

	}

	public Users save(Users user) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role roles = rolesRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));
		return userRepository.save(user);

	}

	public void SetAuthoritiesService(SetAuthoritiesDTO authorities) {

		List<Role> listRoles = new ArrayList<>();

		try {
			Role roles = rolesRepository.findByName(authorities.getRole()).get();
			Users user = Optional.of(userRepository.findByEmail(authorities.getEmail())).get();

			listRoles.add(roles);
			user.setRoles(listRoles);
			userRepository.save(user);

		} catch (Exception e) {

			throw new ResourceNotFoundException("Email or Role does not exist");

		}

	}

	public Role saveRole(Role roles) {
		return rolesRepository.save(roles);

	}



}

```

# Security Layer

It has been used the SpringSecurity framework to make the access secure. Below, there is an schema of how this framework works.

First of all, the income HTTP requests will be filtered. There are a bunch of filters we can implement. Clearly, it will depend on which kind of operation we want to do.
But as example of this, we can decide which endpoints will be accessible to each user role. We can also decide if people must be logged in to have certain access.

After hiting all of the filters applied, the Authentication Manager will be called. Its responsibility is to go through all of the providers trying to authenticate the users. Hence, the providers are supposed to fetch users by communicating with User Details Service and return success state if the user exist with given credential. These users will be fetched from the MySQL database, in this case.

![Header](https://github.com/gustavoh430/loginproject/blob/main/Spring%20Security%20Schema.png)
(Source: https://backendstory.com/spring-security-authentication-architecture-explained-in-depth/)




## JWT
Jason Web Token, also known as JWT, is a token digitally assigned to assure secure information transmission.
It is usually used during authentication. Once the user has already proven he can be trusted, a key will be generated and it can be used to access different resources that only will be available through the JWT token.

The general schema of how it works is showed below:

![image](https://user-images.githubusercontent.com/41215245/214250247-19335bda-0345-4e98-8de7-db7bf9652292.png)
(Source: https://metamug.com/article/security/jwt-java-tutorial-create-verify.html)



However, to understand what has been done in this project completely, we must get our head around how it is related to the Spring Security and where it belongs in the schema showed before.




## SpringSecurity + JWT

Now we can understand how JWT and Spring Security work together.As seen in the image below, we create a class representing the jwtFilter that will be part of the Spring Security filters. Then, there will also be a JwTProvider as a custom authentication provider.

These implemented steps will be enough to repond an authentication with a JwT token.


![Header](https://github.com/gustavoh430/loginproject/blob/main/JWT%20%2B%20SpringSecurity.png)
(Source: https://backendstory.com/spring-security-authentication-architecture-explained-in-depth/)





#  HTTP Response code and Exception Handling Messages

An effort has been made to treat most of the exceptions that could ocorrur. Furthermore, the http response codes have been thought to be as descriptive as possible. 


## Response codes

You will be seeing these response codes throughout the project:


```json
"HTTP Response Code 401 - Not Authorized"
"HTTP Response Code 500 - Internal Server Error"
"HTTP Response Code 406 - Not Acceptable"
"HTTP Response Code 409 - Conflict"
"HTTP Response Code 404 - Not Found"
"HTTP Response Code 200 - OK"

```

## Exception Handling Messages

Most of the errors will be presented like the HTTP Error Message below:

```json
{
    "timestamp": "<TIMESTAMP>",
    "status": "<HTTP_RESPONSE_CODE>",
    "error": "<HTTP_RESPONSE_ERROR>",
    "message": "<HTTP_RESPONSE_MESSAGE>",
    "path": "<PATH>"
}

```

A example of that is:

```json
{
    "timestamp": "2023-01-25T14:03:58.979857100Z",
    "status": 406,
    "error": "Empty Fields",
    "message": "The following field cannot be empty: Name",
    "path": "/login/signup"
}

```


# Author

Hi, I'm Gustavo!

I have been working as an SRE & Observability Analyst for one year already. As most of SREs, I work analyzing infraestructure and services in a massive Cloud based ecosystem, using traces, logs and metrics. 
I'm very skilled at complex tasks operating Monitoring tools like Dynatrace, Splunk and Grafana. 

Nowadays, I am widening my view of Software Engineering. Part of this effort is to learn java.

Contacts:
Email: gustavoh430@gmail.com

Linkedin: https://www.linkedin.com/in/gustavohgodinho/


