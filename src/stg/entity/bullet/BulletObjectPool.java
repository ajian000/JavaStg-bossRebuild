package stg.entity.bullet;

import stg.util.objectpool.ObjectPoolManager;
import stg.util.objectpool.Resettable;

/**
 * 子弹对象池
 * 用于管理子弹对象的创建和回收，减少 GC 压力
 * @since 2026-03-17
 */
public class BulletObjectPool {
    /**
     * 获取子弹对象
     * @param <T> 子弹类型
     * @param clazz 子弹类型的 Class
     * @param args 构造函数参数
     * @return 子弹对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends IBullet> T obtain(Class<T> clazz, Object... args) {
        try {
            // 尝试从对象池获取或创建对象
            try {
                // 获取或创建对象池
                ObjectPoolManager.getInstance().getOrCreatePool(clazz);
                
                // 尝试从对象池获取
                T bullet = ObjectPoolManager.getInstance().acquire(clazz);
                if (bullet != null) {
                    // 重置子弹状态
                    if (bullet instanceof Resettable) {
                        ((Resettable) bullet).resetState();
                    }
                    
                    // 设置子弹的属性
                    // 由于 IBullet 接口没有定义 setX() 和 setY() 方法，这里暂时不设置位置
                    // 实际应用中，可能需要通过其他方式设置子弹位置
                    return bullet;
                }
            } catch (Exception poolEx) {
                // 对象池获取失败，记录异常
                System.err.println("Object pool acquire failed: " + poolEx.getMessage());
            }
            
            // 直接创建对象
            try {
                // 查找匹配的构造函数
                for (java.lang.reflect.Constructor<?> constructor : clazz.getConstructors()) {
                    if (constructor.getParameterCount() == args.length) {
                        T bullet = (T) constructor.newInstance(args);
                        
                        // 将新创建的对象添加到对象池中（如果对象池存在）
                        try {
                            ObjectPoolManager.getInstance().release(bullet);
                            // 重新从对象池获取，这样可以确保对象被正确跟踪
                            return ObjectPoolManager.getInstance().acquire(clazz);
                        } catch (Exception e) {
                            // 忽略异常，直接返回创建的对象
                        }
                        
                        return bullet;
                    }
                }
                throw new IllegalArgumentException("No suitable constructor found for " + clazz.getName());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create bullet: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to obtain bullet: " + e.getMessage(), e);
        }
    }
    
    /**
     * 回收子弹对象到对象池
     * @param <T> 子弹类型
     * @param bullet 要回收的子弹对象
     */
    public static <T extends IBullet> void free(T bullet) {
        if (bullet == null) {
            return;
        }
        
        try {
            ObjectPoolManager.getInstance().release(bullet);
        } catch (Exception e) {
            // 如果对象池未初始化或失败，忽略异常
            // 对象会被 GC 回收
        }
    }
}