package stg.render;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * OpenGL渲染器实现
 * 基于LWJGL的OpenGL渲染器
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class GLRenderer implements IRenderer {
	/** 窗口宽度 */
	private int width;
	/** 窗口高度 */
	private int height;
	/** 是否已初始化 */
	private boolean initialized = false;
	
	/**
	 * 构造函数
	 */
	public GLRenderer() {
	}
	
	/**
	 * 初始化渲染器
	 * @param width 窗口宽度
	 * @param height 窗口高度
	 */
	@Override
	public void initialize(int width, int height) {
		this.width = width;
		this.height = height;
		
		System.out.println("GLRenderer: 开始初始化...");
		
		GL11.glEnable(GL11.GL_BLEND);
		System.out.println("GLRenderer: 启用混合模式");
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		System.out.println("GLRenderer: 设置混合函数");
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		System.out.println("GLRenderer: 禁用深度测试");
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		System.out.println("GLRenderer: 设置投影矩阵模式");
		
		GL11.glLoadIdentity();
		System.out.println("GLRenderer: 重置投影矩阵");
		
		GL11.glOrtho(0, width, 0, height, -1, 1);
		System.out.println("GLRenderer: 设置正交投影");
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		System.out.println("GLRenderer: 设置模型视图矩阵模式");
		
		GL11.glLoadIdentity();
		System.out.println("GLRenderer: 重置模型视图矩阵");
		
		initialized = true;
		System.out.println("GLRenderer 初始化完成: " + width + "x" + height);
	}
	
	/**
	 * 开始渲染帧
	 */
	@Override
	public void beginFrame() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * 结束渲染帧
	 */
	@Override
	public void endFrame() {
		GL11.glFlush();
	}
	
	/**
	 * 设置视口
	 * @param x 视口左下角X坐标
	 * @param y 视口左下角Y坐标
	 * @param width 视口宽度
	 * @param height 视口高度
	 */
	@Override
	public void setViewport(int x, int y, int width, int height) {
		GL11.glViewport(x, y, width, height);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	/**
	 * 清除屏幕
	 * @param r 红色分量
	 * @param g 绿色分量
	 * @param b 蓝色分量
	 * @param a 透明度
	 */
	@Override
	public void clear(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
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
	@Override
	public void drawRect(float x, float y, float width, float height, float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
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
	@Override
	public void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glEnd();
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
	@Override
	public void drawCircle(float x, float y, float radius, float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		int segments = 32;
		GL11.glVertex2f(x, y);
		for (int i = 0; i <= segments; i++) {
			float angle = (float) (2 * Math.PI * i / segments);
			float vx = x + radius * (float) Math.cos(angle);
			float vy = y + radius * (float) Math.sin(angle);
			GL11.glVertex2f(vx, vy);
		}
		GL11.glEnd();
	}
	
	/**
	 * 绘制文本
	 * @param text 文本内容
	 * @param x X坐标
	 * @param y Y坐标
	 * @param font 字体
	 * @param color 颜色
	 */
	@Override
	public void drawText(String text, float x, float y, java.awt.Font font, java.awt.Color color) {
		try {
			// 使用STBFontRenderer渲染文本
			float fontSize = font.getSize2D();
			float[] colorArray = {
				color.getRed() / 255.0f,
				color.getGreen() / 255.0f,
				color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f
			};
			
			STBFontRenderer fontRenderer = STBFontRenderer.getInstance();
			fontRenderer.renderText(text, x, y, fontSize, colorArray);
		} catch (Exception e) {
			// 如果渲染失败，回退到绘制矩形
			fallbackDrawText(text, x, y, font, color);
		}
	}
	
	/**
	 * 绘制文本（简化版，使用指定字体大小和颜色数组）
	 * @param text 文本内容
	 * @param x X坐标
	 * @param y Y坐标
	 * @param fontSize 字体大小
	 * @param color 颜色数组 [r, g, b, a]
	 */
	@Override
	public void drawText(String text, float x, float y, float fontSize, float[] color) {
		try {
			// 使用STBFontRenderer渲染文本
			STBFontRenderer fontRenderer = STBFontRenderer.getInstance();
			fontRenderer.renderText(text, x, y, fontSize, color);
		} catch (Exception e) {
			// 如果渲染失败，回退到绘制矩形
			fallbackDrawText(text, x, y, new java.awt.Font("Arial", java.awt.Font.PLAIN, (int)fontSize), 
				new java.awt.Color(color[0], color[1], color[2], color[3]));
		}
	}
	
	/**
	 * 回退文本渲染方法（当纹理渲染失败时使用）
	 * @param text 文本内容
	 * @param x X坐标
	 * @param y Y坐标
	 * @param font 字体
	 * @param color 颜色
	 */
	private void fallbackDrawText(String text, float x, float y, java.awt.Font font, java.awt.Color color) {
		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;
		float a = color.getAlpha() / 255.0f;
		
		GL11.glColor4f(r, g, b, a);
		
		// 计算文本宽度（每个汉字20像素，每个英文字母10像素）
		float textWidth = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 0x4E00 && c <= 0x9FFF) { // 汉字范围
				textWidth += 20;
			} else {
				textWidth += 10;
			}
		}
		
		// 绘制文本背景矩形
		float charHeight = 25.0f;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y - charHeight);
		GL11.glVertex2f(x + textWidth, y - charHeight);
		GL11.glVertex2f(x + textWidth, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		
		// 打印文本内容到控制台，确保文本正确
		System.out.println("Fallback drawing text: " + text + " at (" + x + ", " + y + ")");
	}
	
	/**
	 * 加载纹理
	 * @param path 图片文件路径
	 * @return 纹理ID
	 */
	public int loadTexture(String path) {
		int textureId = -1;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			// 尝试从文件系统直接读取
			java.nio.file.Path filePath = java.nio.file.Paths.get(path);
			if (!java.nio.file.Files.exists(filePath)) {
				// 如果文件不存在，尝试从类路径读取
				System.out.println("文件系统中找不到图片文件: " + path + "，尝试从类路径读取");
				// 从类路径读取图片
				java.io.InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
				if (inputStream == null) {
					System.err.println("图片文件不存在: " + path);
					return -1;
				}
				
				// 读取输入流到字节数组
				byte[] bytes = inputStream.readAllBytes();
				inputStream.close();
				
				// 分配内存并填充数据
				java.nio.ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
				buffer.put(bytes);
				buffer.flip();
				
				// 加载图片
				java.nio.ByteBuffer image = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
				MemoryUtil.memFree(buffer);
				
				if (image == null) {
					System.err.println("加载图片失败: " + STBImage.stbi_failure_reason());
					return -1;
				}
				
				// 创建纹理
				textureId = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
				
				// 设置纹理参数
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				
				// 保存宽度和高度值
				int imgWidth = width.get();
				int imgHeight = height.get();
				
				// 上传纹理数据
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, imgWidth, imgHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
				
				// 释放图片数据
				STBImage.stbi_image_free(image);
				
				System.out.println("从类路径加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
			} else {
				// 从文件系统直接读取
				java.nio.ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4);
				
				if (image == null) {
					System.err.println("加载图片失败: " + STBImage.stbi_failure_reason());
					return -1;
				}
				
				// 创建纹理
				textureId = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
				
				// 设置纹理参数
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				
				// 保存宽度和高度值
				int imgWidth = width.get();
				int imgHeight = height.get();
				
				// 上传纹理数据
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, imgWidth, imgHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
				
				// 释放图片数据
				STBImage.stbi_image_free(image);
				
				System.out.println("从文件系统加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
			}
			
		} catch (Exception e) {
			System.err.println("加载纹理失败: " + e.getMessage());
			e.printStackTrace();
		}
		
		return textureId;
	}

	/**
	 * 绘制图片
	 * @param textureId 纹理ID
	 * @param x 图片左下角X坐标
	 * @param y 图片左下角Y坐标
	 * @param width 图片宽度
	 * @param height 图片高度
	 */
	@Override
	public void drawImage(int textureId, float x, float y, float width, float height) {
		// 调用带纹理坐标的方法，使用默认坐标（整个纹理）
		drawImage(textureId, x, y, width, height, 0.0f, 0.0f, 1.0f, 1.0f);
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
	@Override
	public void drawImage(int textureId, float x, float y, float width, float height, float texX, float texY, float texWidth, float texHeight) {
		if (textureId == -1) {
			System.err.println("无效的纹理ID");
			return;
		}
		
		// 启用纹理和混合
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		
		// 设置颜色为白色，这样纹理的颜色会正常显示
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		// 计算纹理坐标，调整上下颠倒的问题
		float texYTop = texY + texHeight;
		float texYBottom = texY;
		
		// 绘制四边形
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(texX, texYTop); // 左上角
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(texX + texWidth, texYTop); // 右上角
		GL11.glVertex2f(x + width, y);
		GL11.glTexCoord2f(texX + texWidth, texYBottom); // 右下角
		GL11.glVertex2f(x + width, y + height);
		GL11.glTexCoord2f(texX, texYBottom); // 左下角
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
		
		// 禁用纹理和混合
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * 清理资源
	 */
	@Override
	public void cleanup() {
		// 清理STB字体渲染器资源
		STBFontRenderer.getInstance().cleanup();
		
		initialized = false;
		System.out.println("GLRenderer 资源清理完成");
	}
	
	/**
	 * 检查是否已初始化
	 * @return 是否已初始化
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * 获取窗口宽度
	 * @return 窗口宽度
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 获取窗口高度
	 * @return 窗口高度
	 */
	public int getHeight() {
		return height;
	}
}
