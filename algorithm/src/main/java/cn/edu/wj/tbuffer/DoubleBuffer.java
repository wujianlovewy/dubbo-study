package cn.edu.wj.tbuffer;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class DoubleBuffer{
	
	//ģ��ID������
	final static AtomicLong atomicLong = new AtomicLong(0);
	
	//����
	final long size;
	
	//��������
	final float loadFactor;
	
	volatile Entry entry;
	
	final ExecutorService executorService;
	
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	public DoubleBuffer(long size, long modCount){
		this(size, DEFAULT_LOAD_FACTOR, modCount);
	}
	
	public DoubleBuffer(long size, float loadFactor, long modCount){
		this.size = size;
		this.loadFactor = loadFactor;
		this.entry = new Entry(modCount, null, size);
		
		this.executorService = Executors.newFixedThreadPool(1);
	}
	
	public synchronized Long take(){
		if(this.entry.modCount > this.size * this.loadFactor){
			if(this.entry.next==null && !this.entry.loadNext){
				executorService.submit(new NextEntryInitTask(this.entry));
				this.entry.loadNext = true;
			}
		}
		
		if(this.entry.modCount >= this.entry.maxId){
			while(this.entry.loadNext && this.entry.next==null){
				//LockSupport.parkUntil(this, System.currentTimeMillis()+1);
				LockSupport.parkNanos(10);
			}
			System.out.println("��ǰ�ŶΡ�"+(this.entry.maxId-this.entry.step)+"-"+this.entry.maxId
					+"��ʹ�����л����¸��Ŷ�"+", "+System.currentTimeMillis());
			this.entry = this.entry.next;
		}
		
		return ++this.entry.modCount;
	}
	
	public void destroy(){
		this.executorService.shutdown();
	}
	
	public long getSize() {
		return size;
	}

	public static void main(String[] args) {
		testMultiThread();
	}
	
	public static void testMultiThread(){
		long size = 1000;
		final DoubleBuffer db = new DoubleBuffer(size, DoubleBuffer.atomicLong.getAndAdd(size)); 
		int count = 10000;
		long startTime = System.currentTimeMillis();
		final CountDownLatch latch = new CountDownLatch(count);
		for(int i=0;i<count;i++){
			new Thread(){
				@Override
				public void run(){
					System.out.println(db.take());
					latch.countDown();
				}
			}.start();
			
		}
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("˫Buffer��ȡ�������ܹ���ʱ: "+(System.currentTimeMillis()-startTime));
		db.destroy();
	}
	
	public static void testNormal(){
		long size = 100;
		DoubleBuffer db = new DoubleBuffer(size, DoubleBuffer.atomicLong.getAndAdd(size)); 
		long startTime = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			System.out.println(db.take());
		}
		System.out.println("˫Buffer��ȡ�������ܹ���ʱ: "+(System.currentTimeMillis()-startTime));
		db.destroy();
	}
	
	/**
	 * ����next�Ŷ�
	 * @author jwu
	 */
	class NextEntryInitTask implements Callable<Entry>{
		
		private Entry entry;
		
		NextEntryInitTask(Entry entry){
			this.entry = entry;
		}
		
		public Entry call() throws Exception {
			Thread t = Thread.currentThread();
			if(this.entry.next==null){
				Entry next = new Entry(DoubleBuffer.atomicLong.getAndAdd(this.entry.getStep()), null, this.entry.getStep());
				System.out.println(t.getId()+"==="+t.getName()+": ����next�ŶΡ�"+next.modCount+"-"+next.maxId+"��"+", "+System.currentTimeMillis());
				this.entry.next = next;
				//LockSupport.unpark(this.entry.thread);
				return next;
			}
			System.out.println(t.getId()+"-"+t.getName()+"�첽���¿�ʼ");
			return this.entry.next;
		}
	}
	
	/**
	 * @author jwu
	 * ˫bufferʵ����
	 */
	static final class Entry {
		//��ʼ�±�
		public transient volatile long modCount=0;
		
		transient volatile Entry next;
		
		volatile boolean loadNext = false;
		
		//����±�
		transient final long maxId;
		
		private final long step;
		
		volatile Thread thread;
		
		public Entry(long modCount, Entry next, long step){
			this.modCount = modCount;
			this.next = next;
			this.step = step;
			this.maxId = step + modCount;
			this.thread = Thread.currentThread();
		}

		public long getStep() {
			return step;
		}
		
	}
}


