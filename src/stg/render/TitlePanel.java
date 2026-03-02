package stg.render;

import java.awt.Color;
import java.awt.Font;

import stg.base.KeyStateProvider;

/**
 * 标题页面面板
 * 显示游戏标题和主菜单
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class TitlePanel extends Panel {
	private static final Color BG_COLOR = new Color(10, 10, 20);
	private static final Color SELECTED_COLOR = new Color(255, 200, 100);
	private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

	private enum MenuState {
		MAIN_MENU
	}

	private static final String[] MAIN_MENU_ITEMS = {
		"start game",
		"replay",
		"setting",
		"exit game"
	};

	private int selectedIndex = 0;
	private MenuState currentState = MenuState.MAIN_MENU;
	private int animationFrame = 0;
	private int backgroundTextureId = -1;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean zPressed = false;
	private boolean xPressed = false;
	private boolean isTransitioning = false;
	private boolean isNewPageAnimating = false;
	private float transitionOffset = 0.0f;
	private float newPageOffset = 0.0f;
	private static final float TRANSITION_SPEED = 10.0f;
	private static final float TRANSITION_DURATION = 0.1f * 60; // 0.1秒，按60fps计算
	private static final float NEW_PAGE_DURATION = 0.1f * 60; // 0.1秒，按60fps计算

	public interface TitleCallback {
		void onGameStart();
		void onExit();
	}

	/**
	 * 重置按键状态
	 */
	public void resetKeyStates() {
		upPressed = false;
		downPressed = false;
		zPressed = false;
		xPressed = false;
	}

	private TitleCallback callback;
	private KeyStateProvider keyStateProvider;

	/**
	 * 构造函数
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 * @param callback 标题回调
	 */
	public TitlePanel(int x, int y, int width, int height, TitleCallback callback) {
		super(x, y, width, height);
		setBackgroundColor(0.04f, 0.04f, 0.08f, 1.0f);
		this.callback = callback;
	}
	
	/**
	 * 加载背景纹理
	 * @param renderer 渲染器实例
	 */
	public void loadBackgroundTexture(IRenderer renderer) {
		// 使用相对路径加载背景图片
		String backgroundPath = "resources/images/menu_bg.png";
		// 也可以尝试使用相对路径
		String relativePath = "resources/images/menu_bg.png";
		
		// 尝试使用相对路径加载
		if (renderer instanceof GLRenderer) {
			GLRenderer glRenderer = (GLRenderer) renderer;
			backgroundTextureId = glRenderer.loadTexture(backgroundPath);
			
			// 如果相对路径加载失败，尝试使用备用路径
			if (backgroundTextureId == -1) {
				System.out.println("使用相对路径加载背景纹理失败，尝试使用备用路径");
				backgroundTextureId = glRenderer.loadTexture(relativePath);
			}
			
			if (backgroundTextureId != -1) {
				System.out.println("背景纹理加载成功，纹理ID: " + backgroundTextureId);
			} else {
				System.err.println("背景纹理加载失败");
			}
		} else {
			System.err.println("无效的渲染器实例");
		}
	}

	/**
	 * 设置按键状态提供者
	 * @param keyStateProvider 按键状态提供者
	 */
	public void setKeyStateProvider(KeyStateProvider keyStateProvider) {
		this.keyStateProvider = keyStateProvider;
	}

	/**
	 * 处理键盘输入
	 */
	public void handleInput() {
		if (keyStateProvider == null) return;

		// 处理上下选择
		boolean currentUpPressed = keyStateProvider.isUpPressed();
		if (currentUpPressed && !upPressed) {
			selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
			upPressed = true;
		} else if (!currentUpPressed) {
			upPressed = false;
		}

		boolean currentDownPressed = keyStateProvider.isDownPressed();
		if (currentDownPressed && !downPressed) {
			selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
			downPressed = true;
		} else if (!currentDownPressed) {
			downPressed = false;
		}

		// 处理确认
		boolean currentZPressed = keyStateProvider.isZPressed();
		if (currentZPressed && !zPressed) {
			handleSelection();
			zPressed = true;
		} else if (!currentZPressed) {
			zPressed = false;
		}

		// 处理退出
		boolean currentXPressed = keyStateProvider.isXPressed();
		if (currentXPressed && !xPressed) {
			if (callback != null) {
				callback.onExit();
			}
			xPressed = true;
		} else if (!currentXPressed) {
			xPressed = false;
		}
	}

	/**
	 * 处理菜单选择
	 */
	private void handleSelection() {
		switch (selectedIndex) {
		case 0:
			// 开始游戏
			startTransition(() -> {
				if (callback != null) {
					callback.onGameStart();
				}
			});
			break;
		case 1:
			// 重玩
			startTransition(() -> {
				if (callback != null) {
					callback.onGameStart();
				}
			});
			break;
		case 2:
			// 设置
			// 暂时不实现，留作后续扩展
			break;
		case 3:
			// 退出游戏
			startTransition(() -> {
				if (callback != null) {
					callback.onExit();
				}
			});
			break;
		}
	}

	/**
	 * 开始过渡动画（进入下一个页面，旧页面向左移动）
	 * @param callback 动画结束后执行的回调
	 */
	private void startTransition(Runnable callback) {
		isTransitioning = true;
		transitionOffset = 0.0f;
		
		// 创建动画线程
		new Thread(() -> {
			try {
				// 第一阶段：当前页面向左消失
				for (int i = 0; i < TRANSITION_DURATION; i++) {
					transitionOffset += TRANSITION_SPEED;
					Thread.sleep(16); // 约60fps
				}
				
				// 执行页面切换
				if (callback != null) {
					callback.run();
				}
				
				// 重置过渡状态
				isTransitioning = false;
				transitionOffset = 0.0f;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}
	
	/**
	 * 开始新页面动画（从右侧进入）
	 */
	public void startNewPageAnimation() {
		isNewPageAnimating = true;
		newPageOffset = getWidth() * 0.2f; // 新页面从右侧屏幕的20%位置开始，离中心更近
		
		// 创建动画线程
		new Thread(() -> {
			try {
				// 新页面从右侧平移到中间
				for (int i = 0; i < NEW_PAGE_DURATION; i++) {
					newPageOffset -= TRANSITION_SPEED;
					Thread.sleep(16); // 约60fps
				}
				
				// 重置动画状态
				isNewPageAnimating = false;
				newPageOffset = 0.0f;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}
	
	/**
	 * 开始返回动画（从左侧进入）
	 */
	public void startBackAnimation() {
		isNewPageAnimating = true;
		newPageOffset = -getWidth() * 0.2f; // 新页面从左侧屏幕的20%位置开始，离中心更近
		
		// 创建动画线程
		new Thread(() -> {
			try {
				// 新页面从左侧平移到中间
				for (int i = 0; i < NEW_PAGE_DURATION; i++) {
					newPageOffset += TRANSITION_SPEED;
					Thread.sleep(16); // 约60fps
				}
				
				// 重置动画状态
				isNewPageAnimating = false;
				newPageOffset = 0.0f;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}
	
	/**
	 * 开始返回时的旧页面动画（向右移动）
	 */
	public void startOldPageBackAnimation(Runnable callback) {
		isTransitioning = true;
		transitionOffset = 0.0f;
		
		// 创建动画线程
		new Thread(() -> {
			try {
				// 旧页面向右消失
				for (int i = 0; i < TRANSITION_DURATION; i++) {
					transitionOffset += TRANSITION_SPEED;
					Thread.sleep(16); // 约60fps
				}
				
				// 执行页面切换
				if (callback != null) {
					callback.run();
				}
				
				// 重置过渡状态
				isTransitioning = false;
				transitionOffset = 0.0f;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	/**
	 * 渲染标题页面
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		// 首先绘制背景图片
		drawBackgroundImage(renderer);

		// 然后绘制标题和菜单，应用过渡动画偏移
		float currentOffset = 0;
		if (isTransitioning) {
			// 过渡动画：旧页面向右移动
			currentOffset = transitionOffset;
		} else if (isNewPageAnimating) {
			// 新页面动画：从右侧或左侧进入
			currentOffset = newPageOffset;
		}
		drawTitle(renderer, currentOffset);
		drawMenu(renderer, currentOffset);

		// 更新动画帧
		animationFrame++;
	}
	
	/**
	 * 绘制背景图片
	 * @param renderer 渲染器
	 */
	public void drawBackgroundImage(IRenderer renderer) {
		if (backgroundTextureId != -1) {
			// 绘制背景图片，覆盖整个面板
			renderer.drawImage(backgroundTextureId, 0, 0, getWidth(), getHeight());
		}
	}

	/**
	 * 绘制标题
	 * @param renderer 渲染器
	 */
	private void drawTitle(IRenderer renderer, float offsetX) {
		// 获取字体管理器实例
		FontManager fontManager = FontManager.getInstance();
		
		// 绘制主标题
		Font titleFont = fontManager.getTitleFont();
		String title = "JavaSTG";
		float titleX = getWidth() / 2 - 100 + offsetX;
		float titleY = getHeight() - getHeight() / 3;

		// 绘制黑色边框
		renderer.drawText(title, titleX - 2, titleY - 2, titleFont, Color.BLACK);
		renderer.drawText(title, titleX + 2, titleY - 2, titleFont, Color.BLACK);
		renderer.drawText(title, titleX - 2, titleY + 2, titleFont, Color.BLACK);
		renderer.drawText(title, titleX + 2, titleY + 2, titleFont, Color.BLACK);
		// 绘制白色文字
		renderer.drawText(title, titleX, titleY, titleFont, Color.WHITE);
	}

	/**
	 * 绘制菜单
	 * @param renderer 渲染器
	 */
	private void drawMenu(IRenderer renderer, float offsetX) {
		// 获取字体管理器实例
		FontManager fontManager = FontManager.getInstance();
		// 获取更大加粗的菜单字体
		Font menuFont = fontManager.getFont(32f, Font.BOLD);

		for (int i = 0; i < MAIN_MENU_ITEMS.length; i++) {
			String item = MAIN_MENU_ITEMS[i];
			float x = getWidth() / 2 - 120 + offsetX;
			float y = getHeight() - getHeight() / 2 - 20 - i * 60;

			if (i == selectedIndex) {
				// 绘制选中效果
				// 绘制黑色边框
				renderer.drawText(">", x - 50 - 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(">", x - 50 + 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(">", x - 50 - 1, y + 1, menuFont, Color.BLACK);
				renderer.drawText(">", x - 50 + 1, y + 1, menuFont, Color.BLACK);
				// 绘制选中颜色
				renderer.drawText(">", x - 50, y, menuFont, SELECTED_COLOR);
				
				// 绘制黑色边框
				renderer.drawText(item, x - 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x - 1, y + 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y + 1, menuFont, Color.BLACK);
				// 绘制选中颜色
				renderer.drawText(item, x, y, menuFont, SELECTED_COLOR);
			} else {
				// 绘制黑色边框
				renderer.drawText(item, x - 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x - 1, y + 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y + 1, menuFont, Color.BLACK);
				// 绘制未选中颜色
				renderer.drawText(item, x, y, menuFont, UNSELECTED_COLOR);
			}
		}
	}

	/**
	 * 绘制操作提示
	 * @param renderer 渲染器
	 */
	private void drawHints(IRenderer renderer) {
		// 移除操作提示，按照要求不要其他任何内容
	}

	/**
	 * 更新标题页面
	 */
	public void update() {
		handleInput();
	}
}