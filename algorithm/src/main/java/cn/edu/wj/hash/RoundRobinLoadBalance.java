package cn.edu.wj.hash;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jwu
 * 实现轮询负载均衡算法
 */
public class RoundRobinLoadBalance {

	private final ConcurrentHashMap<String, AtomicInteger> sequences = new ConcurrentHashMap<String, AtomicInteger>(); 
	
	public Invoker doSelect(List<Invoker> invokers){
		String key = "INVOKER";  //负载均衡的方法key
		int length = invokers.size(); //总个数
		int totalWeight = 0; //累计总权重
		int maxWeight = 0; //最大权重
		int minWeight = 0; //最小权重
		
		//记录invoker权重信息
		final LinkedHashMap<Invoker, IntegerWrapper> invokerToWeightMap = new LinkedHashMap<Invoker, IntegerWrapper>();
		
		for(int i=0; i<length; i++){
			int weight = this.getWeight(invokers.get(i));
			maxWeight = Math.max(maxWeight, weight);
			minWeight = Math.min(minWeight, weight);
			if(weight>0){
				totalWeight += weight;
				invokerToWeightMap.put(invokers.get(i), new IntegerWrapper(weight));
			}
		}
		
		AtomicInteger sequence = sequences.get(key);
		if(sequence == null){
			sequences.putIfAbsent(key, new AtomicInteger(0));
			sequence = sequences.get(key);
		}
		int currentSequence = sequence.getAndIncrement();
		
		//权重不相同情况
		if(maxWeight>minWeight && maxWeight>0){
			//对总权重取模轮询
			int mod = currentSequence % totalWeight;
			
			for(int i=0;i<maxWeight;i++){
				for(Invoker invoker : invokerToWeightMap.keySet()){
					IntegerWrapper value = invokerToWeightMap.get(invoker);
					if(mod==0 && value.getValue()>0){
						return invoker;
					}
					if(value.getValue()>0){
						mod--;
						value.decrement();
					}
				}
			}
		}
		
		 // 取模轮循
		return invokers.get(currentSequence%length);
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
		RoundRobinLoadBalance loadBalance = new RoundRobinLoadBalance();
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
	
	private static final class IntegerWrapper {
	    private int value;

	    public IntegerWrapper(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }

	    public void setValue(int value) {
	        this.value = value;
	    }

	    public void decrement() {
	        this.value--;
	    }
	}
	
}


