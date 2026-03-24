package stg.util.objectpool;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 基于ConcurrentLinkedQueue的对象池实现
 * 线程安全，适用于多线程环境
 * 
 * @param <T> 池化对象的类型
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public class ConcurrentLinkedObjectPool<T> implements ObjectPool<T> {
    
    private final ConcurrentLinkedQueue<T> pool;
    private final ObjectFactory<T> factory;
    private int maxCapacity;
    private int totalCreatedObjects = 0;
    private int totalAcquiredObjects = 0;
    
    /**
     * 构造函数
     * 
     * @param factory 对象工厂，用于创建新对象
     */
    public ConcurrentLinkedObjectPool(ObjectFactory<T> factory) {
        this.pool = new ConcurrentLinkedQueue<>();
        this.factory = factory;
        this.maxCapacity = -1; // 默认无限制
    }
    
    /**
     * 构造函数
     * 
     * @param factory 对象工厂，用于创建新对象
     * @param initialCapacity 初始容量
     */
    public ConcurrentLinkedObjectPool(ObjectFactory<T> factory, int initialCapacity) {
        this(factory);
        // 不再在构造函数中初始化，避免创建对象
        // initialize(initialCapacity);
    }
    
    /**
     * 构造函数
     * 
     * @param factory 对象工厂，用于创建新对象
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    public ConcurrentLinkedObjectPool(ObjectFactory<T> factory, int initialCapacity, int maxCapacity) {
        this(factory, initialCapacity);
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public T acquire() {
        // 尝试从池中获取对象
        T object = pool.poll();
        
        // 如果池为空，创建新对象
        if (object == null) {
            object = factory.create();
            if (object == null) {
                throw new RuntimeException("Object factory returned null");
            }
            // 增加创建对象计数
            totalCreatedObjects++;
        }
        
        return object;
    }
    
    @Override
    public void release(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        
        // 检查对象是否可重置
        if (object instanceof Resettable) {
            try {
                ((Resettable) object).resetState();
            } catch (Exception e) {
                // 重置失败，记录异常但继续
                System.err.println("Failed to reset object state: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 检查是否达到最大容量
        if (maxCapacity == -1 || pool.size() < maxCapacity) {
            // 将对象放回池中
            pool.offer(object);
        }
    }
    
    @Override
    public void initialize(int initialCapacity) {
        for (int i = 0; i < initialCapacity; i++) {
            T object = factory.create();
            pool.offer(object);
            totalCreatedObjects++;
        }
    }
    
    @Override
    public void clear() {
        pool.clear();
        totalCreatedObjects = 0;
    }
    
    @Override
    public int size() {
        return totalCreatedObjects;
    }
    
    /**
     * 获取池中当前可用的对象数量
     * @return 池中可用对象数量
     */
    public int getPoolSize() {
        return pool.size();
    }
    
    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    @Override
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        
        // 如果设置了新的最大容量且当前池大小超过了最大容量，清理多余的对象
        if (maxCapacity > 0) {
            while (pool.size() > maxCapacity) {
                pool.poll();
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }
}
