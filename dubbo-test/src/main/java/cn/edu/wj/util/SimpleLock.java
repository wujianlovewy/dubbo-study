package cn.edu.wj.util;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class SimpleLock extends AbstractQueuedSynchronizer{

    private static final long serialVersionUID = 6080411292750037659L;
    
    public SimpleLock(){
    }
    
    protected boolean tryAcquire(int unused) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    protected boolean tryRelease(int unused) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    public void lock() {
        acquire(1);
    }

    public boolean tryLock() {
        return tryAcquire(1);
    }

    public void unlock() {
        release(1);
    }

    public boolean isLocked() {
        return isHeldExclusively();
    }

}

