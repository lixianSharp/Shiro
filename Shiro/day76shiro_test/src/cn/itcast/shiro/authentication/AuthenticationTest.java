package cn.itcast.shiro.authentication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * 认证测试
 * @author 贤元
 * 
 */
public class AuthenticationTest {
	
	//用户登录和退出
	@Test
	public void testLoginAndLogout(){
		//创建securityManager工厂,通过ini配置文件创建SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-first.ini");
		
		//创建SecurityManager
		SecurityManager securityManager = factory.getInstance();
		
		//将securityManager设置到当前的运行环境中
		SecurityUtils.setSecurityManager(securityManager);
		
		//从SecurityManager里边创建一个subject
		Subject subject = SecurityUtils.getSubject();
		
		//在认证提交前需要准备token(令牌)
		//这里的账号和密码将来是由用户输入进去的
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "111111");
		
		try {
			//执行认证提交
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
		//是否认证通过
		boolean isAuthenticated = subject.isAuthenticated();
		
		System.out.println("是否认证通过:"+isAuthenticated);//true
		
		//退出操作
		subject.logout();
		
		isAuthenticated = subject.isAuthenticated();
		
		System.out.println("是否认证通过:"+isAuthenticated);//false
	}
	
	//测试自定义realm
	@Test
	public void testCustomRealm(){
		//创建securityManager工厂,通过ini配置文件创建SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
		//创建SecurityManager
		SecurityManager securityManager = factory.getInstance();
		//将securityManager设置到当前的运行环境中
		SecurityUtils.setSecurityManager(securityManager);
		//从SecurityManager里边创建一个subject
		Subject subject = SecurityUtils.getSubject();
		
		//在认证提交前需要准备token(令牌)
		//这里的账号和密码将来是由用户输入进去的
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "111111");
		
		try {
			//执行认证提交
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		//是否认证通过
		boolean isAuthenticated = subject.isAuthenticated();
		System.out.println("是否认证通过:"+isAuthenticated);//true
	}
	
	
	//MD5散列测试程序
	@Test
	public void testMd5(){
		//原始密码
		String source = "111111";
		//盐
		String salt = "qwerty";
		//散列次数
		int hashIterations = 2;
		//上边散列一次(也就是 111111qwerty散列一次之后的值)：f3694f162729b7d0254c6e40260bf15c
		//上边散列一次：36f2dfa24d0a9fa97276abbe13e596fc
		
		//构造方法中：
		//第一个参数：明文，原始密码
		//第二个参数：盐，通过使用随机数
		//第三个参数：散列的次数，比如散列两次，相当于md5(md5(''))
		Md5Hash md5Hash = new Md5Hash(source, salt, hashIterations);
		
		String password_md5 = md5Hash.toString();
		System.out.println(password_md5);//36f2dfa24d0a9fa97276abbe13e596fc
		
		//第一个参数：散列算法   下面一行的效果和上面的效果等价
		SimpleHash simpleHash = new SimpleHash("md5", source, salt, hashIterations);
		System.out.println(simpleHash.toString());//36f2dfa24d0a9fa97276abbe13e596fc
	}
	
	
	//自定义realm实现散列值匹配
	@Test
	public void testCustomRealmMD5(){
		//创建SecurityManager工厂，通过ini配置文件创建securityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm-md5.ini");
		//创建SecurityManager,通过SecurityManager工厂创建
		SecurityManager securityManager = factory.getInstance();
		//将securityManager设置到当前的运行环境中
		SecurityUtils.setSecurityManager(securityManager);
		
		//从SecurityUtils里边创建一个subject
		Subject subject = SecurityUtils.getSubject();
		
		//在认证提交前准备token(令牌)
		//这里的账号和密码  将来是由用户输入进去
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","111111");
		try {
			//执行认证提交
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
		//是否认证通过
		boolean isAuthenticated = subject.isAuthenticated();
		System.out.println("是否认证通过:"+isAuthenticated);//true
	}
	
}