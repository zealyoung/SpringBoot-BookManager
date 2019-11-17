# SpringBoot-BookManager

    这篇博客记录一下自己做一个Demo项目的全过程，写博客一方面督促自己，一方面可以让自己更熟悉流程和细节，项目资料是从Nowcoder上找的，项目的前端文件是直接使用了源项目文件，后端自己写了一遍。

# 项目简介
## 工程简介
    项目利用Maven，利用SpringBoot作为项目主体框架（SpringBoot相比整合SSM少了很多配置，可以把精力放在代码开发和业务逻辑上，其本质还是SSM），项目使用Freemaker、Web、MyBatis、Aspect四个模块。
## 内容简介
    项目实现图书管理，主要包含注册、登录、向书库中添加书目、借书、还书等操作，通过MD5加密算法对用户密码进行加密，通过cookie与ticket进行登录权限验证。
# 项目流程
## 项目创建方式
* 在https://start.spring.io/ 挑选模块之后下载到本地，用IDE打开
* 在IDE中创建项目，我使用的是IDEA，选择Spring initializr，挑选模块后完成创建
## 项目文件夹
* biz 业务逻辑层，用来存放比较复杂的逻辑
* configuration 用来放Spring Boot的代码配置
* controllers 控制层，控制器都在这里，也可以认为是网页的入口都在这
* dao 持久层，跟数据库交互的包，主要是MyBatis在这里编码
* interceptor AOP的代码都在这
* model 各种数据模型，对数据的描述
* service 一般用作对dao层的封装
* utils 工具包，一般都是静态方法。
## 创建数据库
```sql
create database bookmanager
```
    分别创建三张表user、book、ticket，可以先向book表中插入几条数据
## 完成图书的CRUD
### 创建图书实体类Book.java
    描述图书的基本属性，对应数据库表，写入Getter和Setter方法
### 创建图书的DAO层BookDAO.java
    DAO层就是利用具体SQL实现CRUD，也是常说的Mapper，因为项目是Mybatis作为持久层框架的，所以可以选择写相应Xml完成映射，或直接用注解，本项目采用的是注解方法（个人感觉能用注解的时候都用注解，很清晰）
### 创建BookService.java
    这里的Service层就是对Controller层和Model层的解耦，通用的业务逻辑实现应该在Service层中，如果去掉这一层其实也可以运行，我们直接在Controller层中实现业务逻辑，并直接调用持久层(也就是DAO层)实现数据库操作，也可以完成相应内容，但代码的耦合度就变得很高。在本项目中主要是对DAO进行封装。
### 创建BookController.java
    Controller也就是MVC中的C，这时候的Controller还不完整，只是先出一个运行效果，可以访问主页，代码也只有下面几行。
```Java
@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping(path = {"/index"}, method = {RequestMethod.GET})
    public String bookList(Model model) {
        loadAllBooksView(model);
        return "book/books";
    }

    private void loadAllBooksView(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
    }
}
```
### 看到运行效果
    访问127.0.0.1:8080/index也就是主页的URL
![](第一次运行.png)
(其实和写了一个HelloWorld没什么太大区别哈哈哈哈，第一个流程也属于先让项目运行起来，熟悉一下流程)
## 完成User类和Ticket类
### 创建User.java和Ticket.java
    写对应实体类
### 创建UserDAO.java和TicketDAO.java
    完成DAO层的CRUD
### 创建UserService.java和TicketService.java
    根据需求对DAO层进行封装
## 完成工具类
    这部分跟着源项目学习了一下，下面贴一下源项目的介绍。
**MD5.java**

这个类就是用来加密的。服务器不保存用户的明文密码是一项基本常识，所以我们用MD5来加密。这里也不要专注于MD5的具体实现方法，这不是我们的主要任务，但建议你至少要知道MD5常用在什么地方，并知道这个加密是不可逆的就可以了。

**UuidUtils.java**

注意到Cookie都是一串无意义的码串，我们用JDK自带的UUID生成器可以非常方便的生成这样一串随机的字符串。

**ConcurrentUtils.java**

用来保存当前访问者的容器。我们知道，当web程序运行在web服务器中时，都是并发的
环境，拿tomcat来说，对于每一个请求tomcat都会从自己维护的线程池中选一个线程去处理这个请求。ThreadLocal这个类提供了一种线程id到一个泛型的绑定，你可以认为它是一个Map，当我们从里面取数据的时候，实际上是将当前的线程id作为map的key，取出之前这个线程存的东西。这里我们将User保存在里面，这样我们就能随时在程序的任何地方拿出User信息了。

**CookieUtils.java**

用来封装http请求中的Cookie的操作。

**TicketUtils.java**

提供了一个生产Ticket的方法。

**LoginRegisterException.java**

封装的Exception类，用来抛出异常信息。

**HostHolder.java**

HostHolder是一个重要的类，用来包装ConcurrentUtils.java的方法，并交给Spring容器去管理，使得我们可以在任何时候都能找当前的User，只要用户登录了，我们就将User信息设置到HostHolder里面，这样我们就在其他地方可以直接拿出User来用。

## 完成登录注册逻辑LoginBiz.java
    biz是business的简写，用来保存较为复杂的业务逻辑，其本身也是一个service，一般命名Service的用来对DAO层进行封装，命名Biz表示复杂业务逻辑。
```Java
@Service
public class LoginBiz {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    /**
     * 登录逻辑，LoginController调用
     * @param email
     * @param password
     * @return T票的Ticket(String)
     * @throws Exception
     */
    public String login(String email, String password) throws Exception{
        User user = userService.getUser(email);
        //验证是否可以登录
        if(user == null)
            throw new LoginRegisterException("账户不存在，请重新输入或注册账号");
        if(StringUtils.equals(MD5.next(password), user.getPassword()))
            throw new LoginRegisterException("密码不正确");
        //检查Ticket
        Ticket t = ticketService.getTicket(user.getId());

        if(t == null){
            t = TicketUtils.next(user.getId());
            ticketService.addTicket(t);
        }
        else if(t.getExpiredAt().before(new Date())){
            ticketService.deleteTicket(t.getId());
            t = TicketUtils.next(user.getId());
            ticketService.addTicket(t);
        }

        ConcurrentUtils.setHost(user);
        return t.getTicket();
    }

    /**
     * 登出逻辑
     * @param t
     */
    public void logout(String t){
        ticketService.deleteTicket(t);
    }

    /**
     * 注册逻辑
     * @param user
     * @return
     * @throws LoginRegisterException
     */
    public String register(User user) throws LoginRegisterException{

        if(userService.getUser(user.getEmail()) != null){
            throw new LoginRegisterException("用户账号已存在");
        }
        
        user.setPassword(MD5.next(user.getPassword()));
        userService.addUser(user);

        Ticket t = TicketUtils.next(user.getId());
        ticketService.addTicket(t);

        ConcurrentUtils.setHost(user);
        return t.getTicket();
    }
}
```
## 完成控制层Controller
### 完成HomeController
    这个是我自己新加的，之前index也就是主页也放在了BookController里，个人感觉不是很合适，分离出来以后BookController只注入BookService，降低耦合度。
### 完成BookController
    将借书、还书实现，简单调用BOOKService方法即可。
### 完成LoginController
    主要调用LoginBiz方法。

## 实现拦截器完成登录权限验证
    两个拦截器一个用来注入host信息，另一个用来进行权限验证。

# 项目总结
    此项目基本就是拿来熟悉一下一个SpringBoot项目从零到运行，不得不说真的比SSM项目轻松许多，SSM项目要配置web.xml，spring的xml，springMVC的xml。
    项目中也有一些bug，例如退出登录后仍显示登录名，在注册时分配的ticket无效，直到登陆重新创建(虽然没太大影响，但是会多出来一个userid为0的ticket)，自己写的时候也改了一下，在完成项目时仍有问题，不过不属于编码问题，从项目设计就有的问题，例如应该有张表来维护用户和书的借还关系，此项目里A用户借走了书1，B用户点击归还书1也会成功归还，因为是通过book表的status列维护的，本来想在代码里增加一些判断来确保只有借走书的人才可以还，但是直接改的话会增加不少耦合性，代码也会看起来很乱，从设计阶段改的话工作量也很小(还是懒得改，主要还是为了练手)
