package cn.edu.wj.hash;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jwu
 * 实现带权重随机负载均衡算法
 */
public class RandomLoadBalance {

	SecureRandom secureRandom = new SecureRandom();
	
	public Invoker doSelect(List<Invoker> invokers){
		int length = invokers.size(); //总个数
		int totalWeight = 0; // 总权重
        boolean sameWeight = true; // 权重是否都一样
        
        for(int i=0;i<length;i++){
        	int weight = this.getWeight(invokers.get(i));
        	totalWeight += weight; //累计总权重
        	
        	if(sameWeight && i>0 && this.getWeight(invokers.get(i-1))!=weight){
        		sameWeight = false; //判断权重是否一样,默认权重相同
        	}
        }
        
        //权重不一样处理
        if(!sameWeight && totalWeight>0){
	        int offset = secureRandom.nextInt(totalWeight); //按总权重随机
	        //确定随机值落在哪个片段
	        for(int i=0;i<length;i++){
        		int weight = this.getWeight(invokers.get(i));
        		offset -= weight;
        		if(offset<0){
        			return invokers.get(i);
        		}
	        }
        }
        
        //权重相同或权重为0则均等随机
		return invokers.get(secureRandom.nextInt(length));
	}
	
	public int getWeight(Invoker invoker){
		return invoker.getWeight();
	}
	
	public static List<Invoker> invokers = new ArrayList<Invoker>();
	
	public static void main(String[] args) {
		invokers.add(new Invoker("10.181.123.11", 100));
		invokers.add(new Invoker("10.182.124.12", 100));
		invokers.add(new Invoker("10.183.125.13", 200));
		
		Map<Invoker, AtomicLong> counter = getInvokeCounter(2000,"");
		for (Invoker invoker : counter.keySet()) {
			long count = counter.get(invoker).get();
			System.out.println(invoker+", 调用次数:"+count);
        }
	}
	
	public static Map<Invoker, AtomicLong>	getInvokeCounter(int runs, String loadbalanceName) {
		RandomLoadBalance loadBalance = new RandomLoadBalance();
		Map<Invoker, AtomicLong> map = new ConcurrentHashMap<Invoker, AtomicLong>();
		
		for(Invoker invoker : invokers){ 
			map.put(invoker, new AtomicLong(0));
		}
		
		for(int i=0;i<runs;i++){
			Invoker invoker = loadBalance.doSelect(invokers);
			map.get(invoker).incrementAndGet();
		}
		return map;
	}
}
