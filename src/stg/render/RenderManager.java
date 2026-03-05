package stg.render;

/**
 * 渲染管理器
 * 管理渲染器的生命周期和提供统一的渲染接口
 * @since 2026-03-05
 * @author JavaSTG Team
 */
public class RenderManager {
    private static RenderManager instance;
    private IRenderer renderer;
    private boolean initialized;
    
    /**
     * 私有构造函数
     */
    private RenderManager() {
    }
    
    /**
     * 获取RenderManager实例
     * @return RenderManager实例
     */
    public static synchronized RenderManager getInstance() {
        if (instance == null) {
            instance = new RenderManager();
        }
        return instance;
    }
    
    /**
     * 设置渲染器
     * @param renderer 渲染器实例
     */
    public void setRenderer(IRenderer renderer) {
        if (this.renderer != null && initialized) {
            this.renderer.cleanup();
        }
        this.renderer = renderer;
        initialized = false;
    }
    
    /**
     * 获取当前渲染器
     * @return 当前渲染器
     */
    public IRenderer getRenderer() {
        return renderer;
    }
    
    /**
     * 初始化渲染管理器
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public void initialize(int width, int height) {
        if (renderer == null) {
            // 如果没有设置渲染器，使用默认的GLRenderer
            renderer = new GLRenderer();
        }
        renderer.initialize(width, height);
        initialized = true;
    }
    
    /**
     * 开始渲染帧
     */
    public void beginFrame() {
        checkInitialized();
        renderer.beginFrame();
    }
    
    /**
     * 结束渲染帧
     */
    public void endFrame() {
        checkInitialized();
        renderer.endFrame();
    }
    
    /**
     * 设置视口
     * @param x 视口左下角X坐标
     * @param y 视口左下角Y坐标
     * @param width 视口宽度
     * @param height 视口高度
     */
    public void setViewport(int x, int y, int width, int height) {
        checkInitialized();
        renderer.setViewport(x, y, width, height);
    }
    
    /**
     * 清除屏幕
     * @param r 红色分量 (0.0 - 1.0)
     * @param g 绿色分量 (0.0 - 1.0)
     * @param b 蓝色分量 (0.0 - 1.0)
     * @param a 透明度 (0.0 - 1.0)
     */
    public void clear(float r, float g, float b, float a) {
        checkInitialized();
        renderer.clear(r, g, b, a);
    }
    
    /**
     * 绘制矩形
     * @param x 矩形左下角X坐标
     * @param y 矩形左下角Y坐标
     * @param width 矩形宽度
     * @param height 矩形高度
     * @param r 红色分量
     * @param g 绿色分量
     * @param b 蓝色分量
     * @param a 透明度
     */
    public void drawRect(float x, float y, float width, float height, float r, float g, float b, float a) {
        checkInitialized();
        renderer.drawRect(x, y, width, height, r, g, b, a);
    }
    
    /**
     * 绘制线条
     * @param x1 起点X坐标
     * @param y1 起点Y坐标
     * @param x2 终点X坐标
     * @param y2 终点Y坐标
     * @param r 红色分量
     * @param g 绿色分量
     * @param b 蓝色分量
     * @param a 透明度
     */
    public void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
        checkInitialized();
        renderer.drawLine(x1, y1, x2, y2, r, g, b, a);
    }
    
    /**
     * 绘制圆形
     * @param x 圆心X坐标
     * @param y 圆心Y坐标
     * @param radius 半径
     * @param r 红色分量
     * @param g 绿色分量
     * @param b 蓝色分量
     * @param a 透明度
     */
    public void drawCircle(float x, float y, float radius, float r, float g, float b, float a) {
        checkInitialized();
        renderer.drawCircle(x, y, radius, r, g, b, a);
    }
    
    /**
     * 绘制文本
     * @param text 文本内容
     * @param x X坐标
     * @param y Y坐标
     * @param font 字体
     * @param color 颜色
     */
    public void drawText(String text, float x, float y, java.awt.Font font, java.awt.Color color) {
        checkInitialized();
        renderer.drawText(text, x, y, font, color);
    }
    
    /**
     * 绘制文本（简化版，使用指定字体大小和颜色数组）
     * @param text 文本内容
     * @param x X坐标
     * @param y Y坐标
     * @param fontSize 字体大小
     * @param color 颜色数组 [r, g, b, a]
     */
    public void drawText(String text, float x, float y, float fontSize, float[] color) {
        checkInitialized();
        renderer.drawText(text, x, y, fontSize, color);
    }
    
    /**
     * 绘制图片
     * @param textureId 纹理ID
     * @param x 图片左下角X坐标
     * @param y 图片左下角Y坐标
     * @param width 图片宽度
     * @param height 图片高度
     */
    public void drawImage(int textureId, float x, float y, float width, float height) {
        checkInitialized();
        renderer.drawImage(textureId, x, y, width, height);
    }
    
    /**
     * 绘制图片（带纹理坐标）
     * @param textureId 纹理ID
     * @param x 图片左下角X坐标
     * @param y 图片左下角Y坐标
     * @param width 图片宽度
     * @param height 图片高度
     * @param texX 纹理起始X坐标（0-1）
     * @param texY 纹理起始Y坐标（0-1）
     * @param texWidth 纹理宽度（0-1）
     * @param texHeight 纹理高度（0-1）
     */
    public void drawImage(int textureId, float x, float y, float width, float height, float texX, float texY, float texWidth, float texHeight) {
        checkInitialized();
        renderer.drawImage(textureId, x, y, width, height, texX, texY, texWidth, texHeight);
    }
    
    /**
     * 加载纹理
     * @param path 图片文件路径
     * @return 纹理ID
     */
    public int loadTexture(String path) {
        checkInitialized();
        if (renderer instanceof GLRenderer) {
            return ((GLRenderer) renderer).loadTexture(path);
        }
        throw new UnsupportedOperationException("当前渲染器不支持纹理加载");
    }
    
    /**
     * 清理资源
     */
    public void cleanup() {
        if (initialized && renderer != null) {
            renderer.cleanup();
            initialized = false;
        }
    }
    
    /**
     * 检查渲染器是否已初始化
     */
    private void checkInitialized() {
        if (!initialized || renderer == null) {
            throw new IllegalStateException("RenderManager 未初始化");
        }
    }
    
    /**
     * 检查是否已初始化
     * @return 是否已初始化
     */
    public boolean isInitialized() {
        return initialized;
    }
}