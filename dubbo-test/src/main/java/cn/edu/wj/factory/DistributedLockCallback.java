package cn.edu.wj.factory;

/**
 * 分布式锁回调接口
 * @author jwu
 * @param <T>
 */
public interface DistributedLockCallback<T>{

	 /** 
     * 必须在此方法中实现需要加分布式锁的业务逻辑 
     *  
     * @return 
     */  
    public T process();  
  
    /** 
     * 得到分布式锁名称 
     *  
     * @return 
     */  
    public String getLockName();  
	
}
