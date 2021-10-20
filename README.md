# java-dataloader-plus

用于简化java-dataloader开发的增强工具包（a powerful enhanced toolkit of java-dataloader for simplify
development）

本工具在java-dataloader基础上进行扩展，简化了java-dataloader的接入成本，并且减少了java-dataloader对业务代码的侵入。

[java-dataloader](https://github.com/graphql-java/java-dataloader)
可以作为应用程序的数据层的组成部分，在不同的后端提供一致的API，并通过批处理和缓存减少消息通信的开销，并且可以有效得解决在应用开发中经常碰到"n+1 "获取问题。

## 开始使用

- 添加依赖
  ```xml
  <dependency>
    <groupId>com.github.lokic</groupId>
    <artifactId>java-dataloader-plus-spring-boot-starter</artifactId>
    <version>${lastest.version}</version>
  </dependency>
	```


- 在spring-boot的启动的Application上添加注解

  ```java
  @EnableDataLoader
  @SpringBootApplication
  public class XxxApplication {
       ...
  }
  ```


- 创建dataloader接口和批处理程序

  ```java
  @DataLoaderService
  public interface UserClient {
    
      @DataLoaderMapping(batchLoader = UserNameBatchLoader.class)
      CompletableFuture<String> getNameById(String id);
  }
  
  public class UserNameBatchLoader implements MultiKeyMappedBatchLoader<String, String> {
  
      @Override
      public Map<String, String> doLoad(Set<String> set, BatchLoaderEnvironment batchLoaderEnvironment) {
          ...
      }
  }
  ```


- 在需要执行批处理的方法上加上@DataLoadable

  ```java
  import com.github.lokic.javaplus.CompletableFutures;
  
  
  @Autowired
  private UserService userService;
  
  public void process(List<String> uidList) {
    
      // 建议使用CompletableFutures#join(CompletableFuture)来阻塞获取值，而不是CompletableFuture#join()。
      // CompletableFutures#join(CompletableFuture)会把实际异常抛出，而不是使用CompletionException封装之后抛出，这样可以减少引入CompletableFuture对业务代码的侵入。
      List<String> names = CompletableFutures.join(userService.getNameList(uidList));
      ...
  }
  
  
  @Service
  public class UserService {
      @Autowired
	    private UserClient userClient;
    
      @DataLoadable
      public CompletableFuture<List<String>> getNameList(List<String> uidList) {
        List<CompletableFuture<String>> nameFutureList = uidList.stream()
              .map(userClient::getNameById)
              .collect(Collectors.toList());
        
        // CompletableFutures#sequence可以很方便得把List<CompletableFuture>转换成CompletableFuture<List>
        return CompletableFutures.sequence(nameFutureList);
      }
  }
  ```

## 复杂场景

- 多个CompletableFuture返回之后，组装成对象返回

  ```java
  import static com.github.lokic.javaplus.CompletableFutures.Fors.For;
  import static com.github.lokic.javaplus.CompletableFutures.Fors.Yield;
  
  
  @DataLoadable
  public CompletableFuture<UserInfo> getUserInfo(String uid) {
      return userClient.getNameById(uid)
              .thenCompose(For((name) -> userClient.getAddressById(uid)))
              .thenApply(Yield((name, address) -> new UserInfo(uid, name, address)));
  }
  
  public class UserInfo {
      private final String uid;
      private final String name;
      private final String address;
  
      public UserInfo(String uid, String name, String address) {
          this.uid = uid;
          this.name = name;
          this.address = address;
     }
  }
  ```


- 如果需要支持java-dataloader中的keyContext，需要在@DataLoaderMapping注解的对应方法参数上加上@KeyContext，@KeyContext目前一个方法最多只支持一个

  ```java
  @DataLoaderService
  public interface UserClient {
    
      @DataLoaderMapping(batchLoader = UserNameBatchLoader.class)
      CompletableFuture<String> getNameById(String id, @KeyContext String context);
  }
  ```


- @DataLoadable嵌套使用时，DataLoadable支持传播，目前支持Propagation.REQUEST和Propagation.REQUIRES_NEW

  - Propagation.REQUEST：如果当前存在Registry，则复用Registry的上下文，如果没有则新建一个Registry执行
  - Propagation.REQUEST_NEW： 基于当前配置，新建一个Registry执行

## 注意

- 如果要使用CompletableFuture中的相关方法，对@DataLoaderMapping返回的数据进行处理，则必须使用 **同步** 的方法，不能是异步相关的方法，即不能使用xxxAsync结尾的方法


- 如果直接调用同一个类中使用@DataLoadable注解的方法，是不生效的。

  - 因为@DataLoadable是通过Spring AOP实现的，被调用方法和调用处的代码都处在同一个类，所以只是相当于对象本身的调用，并没有代理对象 ，所以@DataLoadable注解不生效

  - 类似原因可以参考Spring中@Transactional和@Async


- 目前仅支持java-dataloader中*batchingEnabled*、*cachingEnabled*、*cachingExceptionsEnabled*、*maxBatchSize*这4个参数的配置支持
