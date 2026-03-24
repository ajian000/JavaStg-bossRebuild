package stg.util.objectpool;

import java.util.Map;

/**
 * 对象池管理器
 * 集中管理各种类型的对象池
 * 使用单例模式，提供全局访问点
 * 
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public class ObjectPoolManager {
    
    // 单例实例
    private static final ObjectPoolManager INSTANCE = new ObjectPoolManager();
    
    // 存储不同类型的对象池（线程安全）
    private final Map<Class<?>, ObjectPool<?>> poolMap;
    
    /**
     * 私有构造函数
     */
    private ObjectPoolManager() {
        poolMap = new java.util.concurrent.ConcurrentHashMap<>();
    }
    
    /**
     * 获取单例实例
     * 
     * @return 对象池管理器实例
     */
    public static ObjectPoolManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * 注册对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param pool 对象池实例
     */
    public <T> void registerPool(Class<T> type, ObjectPool<T> pool) {
        if (type == null || pool == null) {
            throw new IllegalArgumentException("Type and pool cannot be null");
        }
        poolMap.put(type, pool);
    }
    
    /**
     * 获取指定类型的对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @return 对象池实例
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getPool(Class<T> type) {
        return (ObjectPool<T>) poolMap.get(type);
    }
    
    /**
     * 检查是否存在指定类型的对象池
     * 
     * @param type 对象类型的Class
     * @return 是否存在
     */
    public boolean hasPool(Class<?> type) {
        if (type == null) {
            return false;
        }
        return poolMap.containsKey(type);
    }
    
    /**
     * 从对象池中获取对象
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @return 池化对象
     */
    public <T> T acquire(Class<T> type) {
        ObjectPool<T> pool = getPool(type);
        
        if (pool == null) {
            throw new IllegalArgumentException("No object pool registered for type: " + type.getName());
        }
        return pool.acquire();
    }
    
    /**
     * 获取或创建对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @return 对象池实例
     */
    public <T> ObjectPool<T> getOrCreatePool(Class<T> type) {
        ObjectPool<T> pool = getPool(type);
        if (pool == null) {
            // 创建新的对象池
            pool = new ConcurrentLinkedObjectPool<>(
                new ObjectFactory<T>() {
                    @Override
                    public T create() {
                        try {
                            // 尝试使用默认无参构造函数
                            try {
                                return type.getConstructor().newInstance();
                            } catch (NoSuchMethodException e) {
                                // 尝试使用两个float参数的构造函数
                                try {
                                    return type.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                                } catch (NoSuchMethodException e2) {
                                    // 尝试使用一个float参数的构造函数
                                    try {
                                        return type.getConstructor(float.class).newInstance(0.0f);
                                    } catch (NoSuchMethodException e3) {
                                        // 尝试使用一个int参数的构造函数
                                        try {
                                            return type.getConstructor(int.class).newInstance(0);
                                        } catch (NoSuchMethodException e4) {
                                            throw new RuntimeException("No suitable constructor found for " + type.getName());
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to create object for " + type.getName(), e);
                        }
                    }
                },
                0, // 初始容量为 0，避免初始化时创建对象
                100 // 最大容量
            );
            // 注册对象池
            registerPool(type, pool);
        }
        return pool;
    }
    
    /**
     * 获取所有已注册的对象池类型
     * 
     * @return 已注册的对象池类型集合
     */
    public java.util.Set<Class<?>> getRegisteredTypes() {
        return poolMap.keySet();
    }
    
    /**
     * 回收对象到对象池
     * 
     * @param <T> 对象类型
     * @param object 要回收的对象
     */
    @SuppressWarnings("unchecked")
    public <T> void release(T object) {
        if (object == null) {
            return;
        }
        
        Class<?> clazz = object.getClass();
        ObjectPool<T> pool = getPool((Class<T>) clazz);
        
        if (pool == null) {
            throw new IllegalArgumentException("No object pool registered for type: " + clazz.getName());
        }
        pool.release(object);
    }
    
    /**
     * 初始化所有对象池
     */
    public void initializeAllPools() {
        // 不再在初始化时创建对象，避免默认构造函数问题
        // 实际对象创建会在第一次使用时通过 Obj.create() 完成
    }
    
    /**
     * 清理所有对象池
     */
    public void clearAllPools() {
        for (ObjectPool<?> pool : poolMap.values()) {
            pool.clear();
        }
    }
    
    /**
     * 获取对象池的数量
     * 
     * @return 对象池数量
     */
    public int getPoolCount() {
        return poolMap.size();
    }
    
    /**
     * 移除指定类型的对象池
     * 
     * @param type 对象类型的Class
     */
    public void removePool(Class<?> type) {
        poolMap.remove(type);
    }
    
    /**
     * 移除所有对象池
     */
    public void removeAllPools() {
        poolMap.clear();
    }
    
    /**
     * 获取所有对象池中的对象总数
     * @return 对象总数
     */
    public int getTotalObjectCount() {
        int total = 0;
        for (ObjectPool<?> pool : poolMap.values()) {
            total += pool.size();
        }
        return total;
    }
}
