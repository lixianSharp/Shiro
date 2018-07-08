package cn.itcast.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.ssm.shiro.CustomRealm;

/**
 * 手动调用controller，清除shiro的缓存
 * @author 贤元
 *
 */
@Controller
public class ClearShiroCache {
	
	//注入realm
	@Autowired
	private CustomRealm customRealm;
	
	@RequestMapping("/clearShiroCache")
	public String clearShiroCache(){
		
		//清除缓存，将来正常开发要在service调用customRealm.clearCached()清除缓存，比如将用户权限修改之后要立即生效，这时就要清空缓存，才会立即生效。
		customRealm.clearCached();
		return "success";
	}
}
