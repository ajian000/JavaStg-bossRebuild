package stg.render;

/**
 * 渲染器接口
 * 定义渲染器的基本功能
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public interface IRenderer {
	/**
	 * 初始化渲染器
	 * @param width 窗口宽度
	 * @param height 窗口高度
	 */
	void initialize(int width, int height);
	
	/**
	 * 开始渲染帧
	 */
	void beginFrame();
	
	/**
	 * 结束渲染帧
	 */
	void endFrame();
	
	/**
	 * 设置视口
	 * @param x 视口左下角X坐标
	 * @param y 视口左下角Y坐标
	 * @param width 视口宽度
	 * @param height 视口高度
	 */
	void setViewport(int x, int y, int width, int height);
	
	/**
	 * 清除屏幕
	 * @param r 红色分量 (0.0 - 1.0)
	 * @param g 绿色分量 (0.0 - 1.0)
	 * @param b 蓝色分量 (0.0 - 1.0)
	 * @param a 透明度 (0.0 - 1.0)
	 */
	void clear(float r, float g, float b, float a);
	
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
	void drawRect(float x, float y, float width, float height, float r, float g, float b, float a);
	
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
	void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a);
	
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
	void drawCircle(float x, float y, float radius, float r, float g, float b, float a);
	
	/**
	 * 绘制文本
	 * @param text 文本内容
	 * @param x X坐标
	 * @param y Y坐标
	 * @param font 字体
	 * @param color 颜色
	 */
	void drawText(String text, float x, float y, java.awt.Font font, java.awt.Color color);
	
	/**
	 * 绘制文本（简化版，使用指定字体大小和颜色数组）
	 * @param text 文本内容
	 * @param x X坐标
	 * @param y Y坐标
	 * @param fontSize 字体大小
	 * @param color 颜色数组 [r, g, b, a]
	 */
	void drawText(String text, float x, float y, float fontSize, float[] color);

	/**
	 * 绘制图片
	 * @param textureId 纹理ID
	 * @param x 图片左下角X坐标
	 * @param y 图片左下角Y坐标
	 * @param width 图片宽度
	 * @param height 图片高度
	 */
	void drawImage(int textureId, float x, float y, float width, float height);

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
	void drawImage(int textureId, float x, float y, float width, float height, float texX, float texY, float texWidth, float texHeight);

	/**
	 * 清理资源
	 */
	void cleanup();
}
