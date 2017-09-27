package cn.itcast.dubbo.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.dubbo.pojo.ProductBean;
import cn.itcast.dubbo.pojo.User;
import cn.itcast.dubbo.service.UserService;
import cn.itcast.dubbo.util.IDUtil;

public class UserServiceImpl implements UserService {

	private AtomicLong atomic = new AtomicLong();
	
	private static String PATTERN_STR = "yyyyMMddhhmmssSSS";
	private static FastDateFormat format = FastDateFormat.getInstance(PATTERN_STR);
	private static DecimalFormat df = new DecimalFormat("0000");
	
	@Autowired
	private IDUtil idUtil;
	
    public List<User> queryAll() {
        System.out.println("dubbo-a server...");
        return null;
    }

	public int updateProductStock(ProductBean productBean) {
		System.out.println("dubbo-a server update-stock-productId: "+productBean.getId());
		return 0;
	}

	@Override
	public String generateId() {
		String serialNumber = format.format(new Date());
		long num = atomic.getAndIncrement();
		return serialNumber + df.format(num) + idUtil.getMacID();
	}

}
