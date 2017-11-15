package cn.edu.wj.hash;

public class Invoker {
	// 服务器地址
	private String url;
	// 权重
	private int weight;

	public Invoker() {
		super();
	}

	public Invoker(String url, int weight) {
		super();
		this.url = url;
		this.weight = weight;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Invoker [url=" + url + ", weight=" + weight + "]";
	}

}
