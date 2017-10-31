package cn.edu.wj.tbuffer;

public class DoubleBuffer{
	//����
	final int size;
	//��������
	final float loadFactor;
	
	volatile Entry entry;
	
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	public DoubleBuffer(int size, int modCount){
		this(size, DEFAULT_LOAD_FACTOR, modCount);
	}
	
	public DoubleBuffer(int size, float loadFactor, int modCount){
		this.size = size;
		this.loadFactor = loadFactor;
		this.entry = new Entry(modCount, null, size);
	}
	
	public synchronized Integer take(){
		if(this.entry.modCount > this.entry.maxId){
			this.entry = this.entry.next;
		}
		return this.entry.modCount++;
	}
	
	public static void main(String[] args) {
		DoubleBuffer db = new DoubleBuffer(1000, 0);
		for(int i=0;i<100;i++){
			System.out.println(db.take());
		}
	}
	
	/**
	 * @author jwu
	 * ˫bufferʵ����
	 */
	static final class Entry {
		//��ʼ�±�
		public transient volatile int modCount=0;
		
		transient volatile Entry next;
		
		//����±�
		transient final int maxId;
		
		public Entry(int modCount, Entry next, int step){
			this.modCount = modCount;
			this.next = next;
			this.maxId = step + modCount;
		}
	}
}


