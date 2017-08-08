package cn.itcast.dubbo.service;

import java.util.List;

import cn.itcast.dubbo.pojo.ProductBean;
import cn.itcast.dubbo.pojo.User;

public interface UserService {

    public List<User> queryAll();
    
    public int updateProductStock(ProductBean productBean);

}
