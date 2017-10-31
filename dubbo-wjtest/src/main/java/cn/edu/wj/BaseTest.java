package cn.edu.wj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.dubbo.pojo.ProductBean;
import cn.itcast.dubbo.service.UserService;

/**
* @author jwu
* @date 2017-2-28 上午11:12:03
* @Description 
**/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-dubbo.xml" })
public class BaseTest {

    @Autowired
    private UserService userService;
    
    @Test
    public void test(){
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
    }
    
}
