package stg.render;

import stg.core.GameWorld;
import stg.entity.player.Player;
import stg.entity.enemy.Enemy;
import stg.entity.bullet.Bullet;
import stg.entity.item.Item;

/**
 * 主游戏面板
 * 位于窗口中央，用于渲染游戏主画面
 * 宽高比为 3:4
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class GamePanel extends Panel {
	/** 游戏逻辑宽度 */
	public static final int GAME_LOGICAL_WIDTH = 360;
	/** 游戏逻辑高度 */
	public static final int GAME_LOGICAL_HEIGHT = 480;
	/** 玩家实例 */
	private Player player;
	/** 游戏世界 */
	private GameWorld gameWorld;
	/** 坐标系转换工具 */
	private PanelCoordinateSystem coordinateSystem;
	
	/**
	 * 构造函数
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 */
	public GamePanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setBackgroundColor(0.05f, 0.05f, 0.1f, 1.0f);
		coordinateSystem = new PanelCoordinateSystem(width, height, GAME_LOGICAL_WIDTH, GAME_LOGICAL_HEIGHT);
	}
	
	/**
	 * 设置玩家
	 * @param player 玩家实例
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * 设置游戏世界
	 * @param gameWorld 游戏世界实例
	 */
	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	/**
	 * 获取玩家
	 * @return 玩家实例
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * 渲染游戏面板
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		renderBackground(renderer);
		
		// 加载Reimu玩家纹理（在主线程中）
		if (player != null && player instanceof user.player.reimu.__ReimuPlayer) {
			user.player.reimu.__ReimuPlayer reimuPlayer = (user.player.reimu.__ReimuPlayer) player;
			if (reimuPlayer != null && reimuPlayer.getReimuTextureId() == -1 && renderer instanceof GLRenderer) {
				GLRenderer glRenderer = (GLRenderer) renderer;
				String reimuTexturePath = "resources/images/reimu.png";
				int reimuTextureId = glRenderer.loadTexture(reimuTexturePath);
				if (reimuTextureId != -1) {
					reimuPlayer.setReimuTextureId(reimuTextureId);
					System.out.println("为Reimu玩家设置纹理ID: " + reimuTextureId);
				} else {
					System.err.println("无法加载Reimu纹理");
				}
			}
		}
		
		// 渲染敌人
		if (gameWorld != null) {
			for (Enemy enemy : gameWorld.getEnemies()) {
				if (enemy != null && enemy.isActive()) {
					enemy.renderOnScreen(renderer);
				}
			}
			
			// 渲染敌人子弹
			for (Bullet bullet : gameWorld.getEnemyBullets()) {
				if (bullet != null && bullet.isActive()) {
					bullet.renderOnScreen(renderer);
				}
			}
			
			// 渲染玩家子弹
			for (Bullet bullet : gameWorld.getPlayerBullets()) {
				if (bullet != null && bullet.isActive()) {
					bullet.renderOnScreen(renderer);
				}
			}
			
			// 渲染物品
			for (Item item : gameWorld.getItems()) {
				if (item != null && item.isActive()) {
					item.renderOnScreen(renderer);
				}
			}
		}
		
		// 渲染玩家（最后渲染，确保玩家在最上层）
		if (player != null && player.isActive()) {
			player.renderOnScreen(renderer);
		}
	}
	
	/**
	 * 获取游戏逻辑宽度
	 * @return 游戏逻辑宽度
	 */
	public int getLogicalWidth() {
		return GAME_LOGICAL_WIDTH;
	}
	
	/**
	 * 获取游戏逻辑高度
	 * @return 游戏逻辑高度
	 */
	public int getLogicalHeight() {
		return GAME_LOGICAL_HEIGHT;
	}
}
