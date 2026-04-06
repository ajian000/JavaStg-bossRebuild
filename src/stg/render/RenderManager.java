package stg.render;

import stg.core.GameWorld;
import stg.entity.base.Obj;
import stg.entity.bullet.Bullet;
import stg.entity.enemy.Enemy;
import stg.entity.item.Item;
import stg.entity.player.Player;
import stg.util.CoordinateSystem;

/**
 * 渲染管理器类
 * 封装渲染相关的功能，提供统一的渲染接口
 * @since 2026-04-02
 * @author JavaSTG Team
 */
public class RenderManager {
    /** 渲染器 */
    private IRenderer renderer;
    /** 游戏世界 */
    private GameWorld gameWorld;
    /** 玩家实例 */
    private Player player;
    /** 背景颜色 */
    private float[] backgroundColor = {0.05f, 0.05f, 0.1f, 1.0f};
    /** 窗口宽度 */
    private int windowWidth;
    /** 窗口高度 */
    private int windowHeight;
    
    /**
     * 构造函数
     * @param renderer 渲染器实例
     * @param windowWidth 窗口宽度
     * @param windowHeight 窗口高度
     */
    public RenderManager(IRenderer renderer, int windowWidth, int windowHeight) {
        this.renderer = renderer;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    
    /**
     * 设置游戏世界
     * @param gameWorld 游戏世界实例
     */
    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }
    
    /**
     * 设置玩家
     * @param player 玩家实例
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    /**
     * 设置背景颜色
     * @param r 红色分量 (0.0-1.0)
     * @param g 绿色分量 (0.0-1.0)
     * @param b 蓝色分量 (0.0-1.0)
     * @param a 透明度分量 (0.0-1.0)
     */
    public void setBackgroundColor(float r, float g, float b, float a) {
        this.backgroundColor[0] = r;
        this.backgroundColor[1] = g;
        this.backgroundColor[2] = b;
        this.backgroundColor[3] = a;
    }
    
    /**
     * 渲染场景
     */
    public void renderScene() {
        // 清除屏幕
        renderer.beginFrame();
        
        // 渲染背景
        renderBackground();
        
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
        
        renderer.endFrame();
    }
    
    /**
     * 渲染背景
     */
    private void renderBackground() {
        renderer.drawRect(0, 0, windowWidth, windowHeight, 
                backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
    }
    
    /**
     * 更新游戏状态
     * @param deltaTime 帧间隔时间（秒）
     */
    public void updateGame(float deltaTime) {
        // 更新游戏世界
        if (gameWorld != null) {
            gameWorld.update(windowWidth, windowHeight);
        }
        
        // 更新玩家
        if (player != null && player.isActive()) {
            player.update();
        }
    }
    
    /**
     * 获取渲染器
     * @return 渲染器实例
     */
    public IRenderer getRenderer() {
        return renderer;
    }
    
    /**
     * 获取窗口宽度
     * @return 窗口宽度
     */
    public int getWindowWidth() {
        return windowWidth;
    }
    
    /**
     * 获取窗口高度
     * @return 窗口高度
     */
    public int getWindowHeight() {
        return windowHeight;
    }
    
    /**
     * 清理资源
     */
    public void cleanup() {
        if (renderer != null) {
            renderer.cleanup();
        }
    }
}