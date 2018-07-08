package cn.itcast.shiro.authorization;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * 授权测试
 * @author 贤元
 *
 */
public class AuthorizationTest {
	
	//角色授权、资源授权测试
	@Test
	public void testAuthorization(){
		//创建SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-permission.ini");
		
		//创建SecurityManager
		SecurityManager securityManager = factory.getInstance();
		
		//将SecurityManager设置到系统运行环境，和spring后将SecurityManager配置spring容器中，一般单例管理
		SecurityUtils.setSecurityManager(securityManager);
		
		//创建subject
		Subject subject = SecurityUtils.getSubject();
		
		//创建token令牌 
		//这里的账号和密码将来是由用户输入进去的
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "123");
		
		try {
			//执行认证
			subject.login(token);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("认证状态："+subject.isAuthenticated());//true
		
		
		//认证通过后执行授权
		
		//基于角色的授权
		//hashRole传入角色标识
		boolean ishasRole = subject.hasRole("role1");
		System.out.println("单个角色判断:"+ishasRole);//true
		
		//hasAllRoles是否拥有多个角色
		boolean hasAllRoles = subject.hasAllRoles(Arrays.asList("role1","role2", "role3"));
		System.out.println("多个角色判断："+hasAllRoles);//false
		
		//使用check方法进行授权，如果授权不通过会抛出异常
		//subject.checkRole("role3");//抛异常，授权不通过
		//org.apache.shiro.authz.UnauthorizedException: Subject does not have role [role3]
		
		
		//基于资源的授权
		//isPermitted传入权限标识符
		boolean isPermitted = subject.isPermitted("user:create:1");
		System.out.println("单权限判断："+isPermitted);//true
		
		boolean isPermittedAll = subject.isPermittedAll("user:create:1","user:delete");
		System.out.println("多个权限判断:"+isPermittedAll);//true
		
		//使用chekc方法进行授权,如果授权不通过会抛出异常
		subject.checkPermission("items:create:1");//抛异常，授权不通过
		//org.apache.shiro.authz.UnauthorizedException: Subject does not have permission [items:create:1]

	}
	
	//自定义realm进行资源授权测试
	@Test
	public void testAuthorizationCustomRealm(){
		// 创建SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");

		// 创建SecurityManager
		SecurityManager securityManager = factory.getInstance();

		// 将SecurityManager设置到系统运行环境，和spring后将SecurityManager配置spring容器中，一般单例管理
		SecurityUtils.setSecurityManager(securityManager);

		// 创建subject
		Subject subject = SecurityUtils.getSubject();

		// 创建token令牌  用户输入
		//这里的账号和密码将来是由用户输入进去的
		UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","111111");

		// 执行认证
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
		System.out.println("认证状态："+subject.isAuthenticated());//true
		//认证通过后执行授权
		//基于资源的授权，调用isPermitted方法会调用CustomRealm从数据库查询正确权限数据
		//isPermitted传入权限标识符，判断user:create:1是否在CustomRealm查询到权限数据之内
		boolean isPermitted = subject.isPermitted("user:create:1");
		System.out.println("单个权限判断" + isPermitted);//true

		boolean isPermittedAll = subject.isPermittedAll("user:create:1","user:create");
		System.out.println("多个权限判断" + isPermittedAll);//true

		// 使用check方法进行授权，如果授权不通过会抛出异常
		subject.checkPermission("items:add:1");//授权通过，不会抛出异常
	}
	

}
