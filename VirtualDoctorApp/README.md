# VirtualDoctor

## Description
This is a virtual doctor application that helps patients to get medical advice from AI.

## Features
- User can ask for medical advice based on symptoms from AI
- User can get a list of doctors based on the diagnosis
- User can get a list of hospitals based on the diagnosis


## Technologies
- Spring Boot for Backend
- Python [libs] for classification model
- MongoDB for Database

## MongoDB docker container


Firtly, you need to have docker installed on your machine. Then you can run the `docker-compose.yml` file to 
create a MongoDB container.

### How to connect to local MongoDB docker container

Use the following data source

```
#DataSourceSettings#
#LocalDataSource: @localhost
#BEGIN#
<data-source source="LOCAL" name="@localhost" uuid="d350722f-dc95-4d99-98ff-d66eff00ac30"><database-info product="Mongo DB" version="7.0.8" jdbc-version="4.2" driver-name="MongoDB JDBC Driver" driver-version="1.18" dbms="MONGO" exact-version="7.0.8" exact-driver-version="1.18"/><case-sensitivity plain-identifiers="mixed" quoted-identifiers="mixed"/><driver-ref>mongo</driver-ref><synchronize>true</synchronize><jdbc-driver>com.dbschema.MongoJdbcDriver</jdbc-driver><jdbc-url>mongodb://localhost:27017</jdbc-url><jdbc-additional-properties><property name="com.intellij.clouds.kubernetes.db.host.port"/><property name="com.intellij.clouds.kubernetes.db.enabled" value="false"/><property name="com.intellij.clouds.kubernetes.db.container.port"/></jdbc-additional-properties><secret-storage>master_key</secret-storage><patient-name>rootuser</patient-name><schema-mapping><introspection-scope><node kind="schema" negative="1"/></introspection-scope></schema-mapping><working-dir>$ProjectFileDir$</working-dir></data-source>
#END#
```
## Login Management

To add a login mechanism to your Spring Boot application, you can use Spring Security, which is a powerful and highly customizable authentication and access-control framework. Here's a step-by-step guide:

1. Add Spring Security dependencies to your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

2. Create a `UserDetails` service. This service retrieves patient-related data. It is used by Spring Security to handle patient information.

```java
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository patientRepository;

    public UserDetailsServiceImpl(UserRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with name: " + username));

        return UserDetailsImpl.build(patient);
    }
}
```

3. Create a `UserDetailsImpl` class. This class will implement the `UserDetails` interface.

```java
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;

    public UserDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static UserDetailsImpl build(User patient) {
        return new UserDetailsImpl(
                patient.getUsername(),
                patient.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

4. Configure Spring Security. Create a `SecurityConfig` class:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/patient/login").permitAll()
                .anyRequest().authenticated();
    }
}
```

5. Update your `UserController` to handle login requests:

```java
@PostMapping("/login")
public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername()));
}
```

Please note that this is a basic example and might need to be adjusted to fit your specific needs. For example, you might want to handle roles, add a registration endpoint, or use JSON Web Tokens (JWT) for stateless authentication.
