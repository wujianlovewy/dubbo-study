package cn.edu.wj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.dubbo.pojo.ProductBean;
import cn.itcast.dubbo.service.UserService;

@Controller
public class HelloController {

	/*@Autowired
	private UserService userService;*/
	
	@ResponseBody
	@RequestMapping("/json")
	public ProductBean jsonTest(){
		ProductBean productBean1 = new ProductBean();
		productBean1.setId(20);
        
		return productBean1;
	}
	
	/*@RequestMapping("/hello")
	public String hello(){
		ProductBean productBean1 = new ProductBean();
		productBean1.setId(20);
		ProductBean productBean2 = new ProductBean();
		productBean2.setId(111);
		ProductBean productBean3 = new ProductBean();
		productBean3.setId(11);
		
        for(int i=0; i<10; i++){
        	this.userService.updateProductStock(productBean1);
            this.userService.updateProductStock(productBean2);
            this.userService.updateProductStock(productBean3);
        }
        
		return "hello";
	}*/
	
}
