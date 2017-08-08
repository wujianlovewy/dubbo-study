package cn.itcast.dubbo.pojo;

import java.io.Serializable;

public class ProductBean implements Serializable{
	
	private static final long serialVersionUID = 1004332000583995689L;

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
