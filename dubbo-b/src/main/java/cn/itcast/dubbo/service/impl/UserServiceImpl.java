package cn.itcast.dubbo.service.impl;

import java.util.List;

import cn.itcast.dubbo.pojo.ProductBean;
import cn.itcast.dubbo.pojo.User;
import cn.itcast.dubbo.service.UserService;

public class UserServiceImpl implements UserService {

    public List<User> queryAll() {
        System.out.println("dubbo-b server...");
        return null;
    }

	public int updateProductStock(ProductBean productBean) {
		System.out.println("dubbo-b server update-stock-productId: "+productBean.getId());
		return 0;
	}

}
