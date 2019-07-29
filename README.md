
  
  
# Secdra 一个类似**pixiv**（下称p站）的网站的后台接口，项目实现是spring boot框架，但是使用的语言不是java而是基于kotlin的。       
项目大致利用idea初始化基于kotlin的spring boot，需求分析，设计具体风格，规划目录结构，开撸。      
项目大体实现功能是登录注册，上传图片，收藏图片，修改各种信息等。      
与数据库交互使用了jpa。     
项目前端[secdra](https://github.com/JunJieFu/secdra-web)，完全实现前后端分离。      
图片来源p站，通过[Pxer](https://github.com/FoXZilla/Pxer)采集到七牛云上。      
    
注：    
注册和修改功能已经实现，但是没有开放，暂时初始化了50个账号，账号名为1-50，密码不用填。    
上传功能也实现，但是由于图片的上传涉及审核，所以暂时不开放uploadToken。  
    
#### 快速链接 官网：[www.secdra.com](http://www.secdra.com)    
    
#### 关于项目 项目采取多项目模式，开发，控制器主要存放在web项目中，为了解耦，适当地使用了Aop编程，如@Auto，在当前的开发中redis主要是存放用户信息，在后续开发中会将一些接口实现缓存，加快加载速度。service项目主要是存放于数据库交互的文件。  
  
#### 技术栈    
- Kotlin    
 - Spring Boot    
 - JPA    
 - Mysql  
 - Redis  
    
#### 项目结构 ![](https://github.com/JunJieFu/static-image/blob/master/secdra/catalog.png)  
    
#### 数据处理 统一返回接口  
  
 class Result<T> {        var status: Int? = null    
       var message: String? = null    
       var data: T? = null    
   }  
  
  
  
前端返回  
  
 { status:200, data:null, message:"" }  

在这次后台做的一些修改中，主要是已添加接口缓存为主，这方面还需要持续优化。
还有就是添加了socket的一些支持，主要依靠WebSocketMessageBrokerConfigurer来实现。
后续调整会将统一返回作为一个注解，而不是现在这样统一返回。


#### 开源协议 [MIT](https://opensource.org/licenses/MIT)