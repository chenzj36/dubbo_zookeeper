# Dubbo、ZooKeeper学习笔记

###### 原文链接：https://www.cnblogs.com/hellokuangshen/p/11330606.html  
#### 1. 安装zookeeper及dubbo-admin
- zookeeper下载：http://mirror.bit.edu.cn/apache/zookeeper/zookeeper-3.4.14/
> 将conf文件夹下面的zoo_sample.cfg复制一份改名为zoo.cfg，双击/bin/zkServer.cmd即可开启服务
- dubbo-admin下载：https://github.com/apache/dubbo-admin/tree/master
> \application.properties 指定zookeeper地址127.0.0.1:2181(默认)
> 项目目录下打开cmd打包dubbo-admin：mvn clean package -Dmaven.test.skip=true 
> 打包操作也可在IDEA中完成
> 打包完成后：java -jar jar包名  ==>启动dubbo
> dubbo提供的管理界面：localhost:7001
#### 2. 创建项目
##### 2.1 创建空项目
##### 2.2 新建Module：provider-service
- 新建并服务
```
package com.chenzj36.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service //dubbo的service
@Component
public class ProviderServiceImpl implements ProviderService {
    @Override
    public String getTicket() {
        return "票务服务：调用成功~";
    }
}
```
- 添加依赖
```
  <!-- https://mvnrepository.com/artifact/org.apache.dubbo/dubbo-spring-boot-starter -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>2.7.5</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.github.sgroschupf/zkclient -->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
        </dependency>
        <!-- 引入zookeeper -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
            <!--排除这个slf4j-log4j12-->
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

```
- 配置相关属性
```
server.port=8081

#当前应用名字
dubbo.application.name=provider-service
#注册中心地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
#扫描指定包下服务
dubbo.scan.base-packages=com.chenzj36.service
```
##### 2.3 新建Module：consumer-service
- 添加依赖
`与provider相同`
- 配置相关属性
```
server.port=8082

#当前应用名字
dubbo.application.name=consumer-service
#注册中心地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
```
- 配置服务类
```
package com.chenzj36.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service // springframework下的service
public class UserService {
    @Reference // 定义路径相同的接口名
    ProviderService tickerService;
    public void buyTicket(){
        String ticket = tickerService.getTicket();
        System.out.println(ticket);
    }
}
```
- 为了@Reference不出错，导入所引用的类的完整路径和接口，项目结构如下    
![enter description here](https://aliyunosschenzj.oss-cn-beijing.aliyuncs.com/aliyunoss/1581334814618.png)  
##### 2.4 编写测试类(开启测试前打开zookeeper服务)
```
package com.chenzj36;

import com.chenzj36.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsumerServiceApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        userService.buyTicket();
    }
}
```
