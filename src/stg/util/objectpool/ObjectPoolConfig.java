package stg.util.objectpool;

import java.lang.reflect.Constructor;

/**
 * 对象池配置类
 * 集中管理所有游戏对象的对象池配置
 * 支持通过@Pooled注解自动注册对象池
 * 
 * @date 2026-02-22
 * @author JavaSTG Team
 */
public class ObjectPoolConfig {
    
    // 对象池初始化标志
    private static volatile boolean initialized = false;
    // 对象池初始化锁
    private static final Object initLock = new Object();
    
    /**
     * 初始化所有对象池
     * 在应用启动时调用一次即可
     */
    public static void initialize() {
        if (initialized) {
            return; // 已经初始化过
        }
        
        synchronized (initLock) {
            if (initialized) {
                return; // 双重检查锁定
            }
            
            try {
                // 注册硬编码的对象池（向后兼容）
                registerHardcodedPools();
                
                initialized = true;
                System.out.println("Object pools initialized successfully");
            } catch (Exception e) {
                // 初始化失败，记录异常但继续运行
                System.err.println("Failed to initialize object pools: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 注册硬编码的对象池
     * 用于向后兼容，确保现有的类都能正常工作
     */
    private static void registerHardcodedPools() {
        ObjectPoolManager manager = ObjectPoolManager.getInstance();
        
        // 注册基类对象池
        registerPool(manager, "stg.entity.bullet.Bullet", 100, 500);
        registerPool(manager, "stg.entity.enemy.Enemy", 20, 100);
        registerPool(manager, "stg.entity.item.Item", 50, 200);
        
        // 注册用户定义的子弹类
        registerPool(manager, "user.bullet.DefaultPlayerMainBullet", 50, 200);
        registerPool(manager, "user.bullet.SimpleDownBullet", 100, 500);
        
        // 注册用户定义的敌人类
        registerPool(manager, "user.enemy.DefaultEnemy", 10, 50);
        registerPool(manager, "user.enemy.__FairyEnemy", 15, 75);
        registerPool(manager, "user.enemy.__MidFairyEnemy", 5, 25);
        registerPool(manager, "user.boss.__MinorikoBoss", 1, 5);
        
        // 初始化所有对象池
        manager.initializeAllPools();
    }
    
    /**
     * 检查对象池是否已初始化
     * @return 是否已初始化
     */
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 注册指定类型的对象池
     * @param manager 对象池管理器
     * @param className 类名
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerPool(ObjectPoolManager manager, String className, int initialCapacity, int maxCapacity) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // 检查是否已经注册
            if (manager.hasPool(clazz)) {
                return;
            }
            
            // 创建对象池
            ConcurrentLinkedObjectPool pool = new ConcurrentLinkedObjectPool(
                createObjectFactory(clazz, className),
                0, // 初始容量为 0，避免初始化时创建对象
                maxCapacity
            );
            
            // 注册对象池
            manager.registerPool(clazz, pool);
            System.out.println("Registered pool for " + className + ", pool size: " + pool.size());
        } catch (Exception e) {
            // 注册失败，记录异常但继续
            System.err.println("Failed to register pool for " + className + ": " + e.getMessage());
        }
    }
    
    /**
     * 基于@Pooled注解注册对象池
     * 当子类被加载时，通过静态初始化块调用此方法
     * @param clazz 带有@Pooled注解的类
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void registerPooledClass(Class<?> clazz) {
        if (!initialized) {
            // 如果配置还未初始化，先初始化
            initialize();
        }
        
        Pooled annotation = clazz.getAnnotation(Pooled.class);
        if (annotation == null) {
            return; // 没有@Pooled注解，不注册
        }
        
        ObjectPoolManager manager = ObjectPoolManager.getInstance();
        
        // 检查是否已经注册
        if (manager.hasPool(clazz)) {
            return;
        }
        
        try {
            int initialCapacity = annotation.initialCapacity();
            int maxCapacity = annotation.maxCapacity();
            String poolName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();
            
            // 创建对象工厂
            ObjectFactory factory = createObjectFactoryForClass(clazz);
            
            // 创建对象池
            ConcurrentLinkedObjectPool pool = new ConcurrentLinkedObjectPool(
                factory,
                initialCapacity,
                maxCapacity
            );
            
            // 注册对象池
            manager.registerPool(clazz, pool);
            System.out.println("Registered pool for @Pooled class: " + clazz.getName() + 
                " [name=" + poolName + ", initial=" + initialCapacity + ", max=" + maxCapacity + "]");
        } catch (Exception e) {
            System.err.println("Failed to register @Pooled class " + clazz.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * 创建对象工厂
     * @param clazz 类
     * @param className 类名
     * @return 对象工厂
     */
    private static ObjectFactory createObjectFactory(Class<?> clazz, String className) {
        return new ObjectFactory() {
            @Override
            public Object create() {
                try {
                    return createObjectInstance(clazz, className);
                } catch (Exception e) {
                    System.err.println("Failed to create object for " + className + ": " + e.getMessage());
                    return null;
                }
            }
        };
    }
    
    /**
     * 为指定类创建对象工厂
     * @param clazz 类
     * @return 对象工厂
     */
    private static ObjectFactory createObjectFactoryForClass(Class<?> clazz) {
        return new ObjectFactory() {
            @Override
            public Object create() {
                try {
                    return createObjectInstanceForClass(clazz);
                } catch (Exception e) {
                    System.err.println("Failed to create object for " + clazz.getName() + ": " + e.getMessage());
                    return null;
                }
            }
        };
    }
    
    /**
     * 根据类名创建对象实例（向后兼容）
     * @param clazz 类
     * @param className 类名
     * @return 对象实例
     * @throws Exception 创建失败时抛出异常
     */
    private static Object createObjectInstance(Class<?> clazz, String className) throws Exception {
        // 对于不同类型的对象，使用不同的默认参数
        if (className.contains("FairyEnemy")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("MidFairyEnemy")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("DefaultEnemy")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("TestBoss")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("MinorikoBoss")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("DefaultPlayerMainBullet")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("SimpleDownBullet")) {
            return clazz.getConstructor(float.class, float.class, float.class, float.class, float.class, java.awt.Color.class)
                .newInstance(0.0f, 0.0f, 0.0f, 1.0f, 5.0f, java.awt.Color.RED);
        } else if (className.contains("Bullet")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("Enemy")) {
            return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
        } else if (className.contains("Item")) {
            return clazz.getConstructor(float.class, float.class, float.class, float.class).newInstance(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            // 尝试使用默认构造函数
            return clazz.getDeclaredConstructor().newInstance();
        }
    }
    
    /**
     * 为指定类创建对象实例
     * 尝试找到最合适的构造函数
     * @param clazz 类
     * @return 对象实例
     * @throws Exception 创建失败时抛出异常
     */
    private static Object createObjectInstanceForClass(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        
        for (Constructor<?> constructor : constructors) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] args = new Object[paramTypes.length];
            
            boolean canUse = true;
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> paramType = paramTypes[i];
                if (paramType == float.class || paramType == Float.class) {
                    args[i] = 0.0f;
                } else if (paramType == int.class || paramType == Integer.class) {
                    args[i] = 0;
                } else if (paramType == double.class || paramType == Double.class) {
                    args[i] = 0.0;
                } else if (paramType == boolean.class || paramType == Boolean.class) {
                    args[i] = false;
                } else if (paramType == String.class) {
                    args[i] = "";
                } else if (paramType == java.awt.Color.class) {
                    args[i] = java.awt.Color.WHITE;
                } else {
                    canUse = false;
                    break;
                }
            }
            
            if (canUse) {
                return constructor.newInstance(args);
            }
        }
        
        // 如果没有找到合适的构造函数，尝试默认构造函数
        return clazz.getDeclaredConstructor().newInstance();
    }
}
