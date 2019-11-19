### Spring security 安全配置

#### 准备工作
0. 实现  `UserDetails` 接口创建用户实体类
0. 实现  `UserDetailsService` 接口创建用户服务类
0. 实现  `GrantedAuthority` 接口创建角色实体类
0. 实现  `FilterInvocationSecurityMetadataSource` 接口创建菜单服务类

> `FilterInvocationSecurityMetadataSource` 与权限相关，目的是灵活配置
#### 自定义安全配置
0. 继承 `WebSecurityConfigurerAdapter` 
    ```java
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    
        // ...
    
    }
    ```

0. 配置用户服务及密码加密方式
    ```
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    ```
0. 自定义安全配置
    ```
    @Override
    protected void configure(HttpSecurity http) throws Exception{
    
       
        // ... 权限配置
        .authorizeRequests()
        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
           @Override
           public <O extends FilterSecurityInterceptor> O postProcess(O object) {
               object.setSecurityMetadataSource(menuService.set(object.getSecurityMetadataSource()));
               return object;
           }
        })
        .anyRequest().authenticated()
        // ...
    
    }
    ```
---

### JPA核心注解

注解 | 说明 
---|--- 
@Entity | 指定类是实体 
@Column | 指定持久属性
@Id | 指定主键
@ManyToMany | 多对多
@ManyToOne | 多对一
@OneToOne | 一对一
@OneToMany | 一对多
@JoinColumn | 指定关联属性
@JoinTable | 指定关联表
@Temporal | 指定日期属性
@Transient | 指定非持久属性

#### @OneToOne

- `cascade` （可选）必须级联到关联目标的操作。
- `fetch` （可选）关联是应该懒洋洋地加载还是必须急切地获取。 默认 `EAGER`
- `optional` （可选）关联是否可选。默认 `true` 
- `mappedBy` （可选）拥有关系的字段。此元素仅在关联的反（非拥有）端指定。

```java
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

public class User {
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "user")
    private Address address;
}

public class Address {
    /** ... */
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
```

#### @OneToMany

- `cascade` （可选）必须级联到关联目标的操作。
- `fetch` （可选）关联是应该懒洋洋地加载还是必须急切地获取。 默认 `LAZY`
- `mappedBy` 拥有关系的字段。必需，除非关系是单向的。

#### @ManyToOne

- `cascade` （可选）必须级联到关联目标的操作。
- `fetch` （可选）关联是应该懒洋洋地加载还是必须急切地获取。 默认 `EAGER`
- `optional` （可选）关联是否可选。默认 `true` 

```java
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import java.util.List;

public class User {
    @OneToMany(mappedBy = "user")
    private List<Address> address;
}

public class Address {
    /** ... */
    @ManyToOne
    private User user;
}
```

#### @ManyToMany

- `cascade` （可选）必须级联到关联目标的操作。
- `fetch` （可选）关联是应该懒洋洋地加载还是必须急切地获取。 默认 `EAGER`
- `mappedBy` 拥有关系的字段。必需，除非关系是单向的。

```java
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import java.util.List;

public class Menu {
    @ManyToMany(mappedBy = "Menus")
    private List<Role> Roles;
}

public class Role {
    /** ... */
    @ManyToMany(mappedBy = "roles")
    private List<Menu> Menus;
}
```

#### @JoinColumn
```java
/**
 * @see ManyToOne
 * @see OneToMany
 * @see OneToOne
 * @see JoinTable
 * @see CollectionTable
 * @see ForeignKey
 */
```

- `name` （可选）外键列的名称。在其中找到它的表取决于上下文。默认主表外键
- `referencedColumnName` （可选）此外键列引用的列的名称。默认从表主键
- `foreignKey` （可选）用于指定或控制在表生成生效时外键约束的生成。默认 `PROVIDER_DEFAULT`

---

### 问题解决

* 实体类相互引用 'toString()' 导致 'StackOverflowException'
> @ToString(exclude = {"user","role"})