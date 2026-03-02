package stg.render;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import stg.base.KeyStateProvider;
import stg.entity.player.Player;
import user.player.DefaultPlayer;
import user.player.reimu.__ReimuPlayer;

/**
 * 玩家选择面板
 * 显示可用的玩家角色供玩家选择
 * @since 2026-02-26
 * @author JavaSTG Team
 */
public class PlayerSelectPanel extends Panel {
    private static final Color BG_COLOR = new Color(10, 10, 20);
    private static final Color SELECTED_COLOR = new Color(255, 200, 100);
    private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

    private int selectedIndex = 0;
    private List<PlayerInfo> playerInfos;
    private KeyStateProvider keyStateProvider;
    private PlayerSelectCallback callback;
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

    public interface PlayerSelectCallback {
        void onPlayerSelected(Player player);
        void onBack();
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

    /**
     * 玩家信息类
     */
    private static class PlayerInfo {
        private String name;
        private String description;
        private Player player;

        public PlayerInfo(String name, String description, Player player) {
            this.name = name;
            this.description = description;
            this.player = player;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Player getPlayer() {
            return player;
        }
    }

    /**
     * 构造函数
     * @param x 面板X坐标
     * @param y 面板Y坐标
     * @param width 面板宽度
     * @param height 面板高度
     * @param callback 回调接口
     */
    public PlayerSelectPanel(int x, int y, int width, int height, PlayerSelectCallback callback) {
        super(x, y, width, height);
        setBackgroundColor(0.04f, 0.04f, 0.08f, 1.0f);
        this.callback = callback;
        loadPlayers();
    }

    /**
     * 加载玩家列表
     */
    private void loadPlayers() {
        playerInfos = new ArrayList<>();
        
        // 添加默认玩家
        DefaultPlayer defaultPlayer = new DefaultPlayer(0.0f, -200.0f);
        playerInfos.add(new PlayerInfo("默认玩家", "标准射击角色", defaultPlayer));
        
        // 添加Reimu玩家
        __ReimuPlayer reimuPlayer = new __ReimuPlayer(0.0f, -200.0f);
        playerInfos.add(new PlayerInfo("灵梦", "博丽神社的巫女", reimuPlayer));
        
        // 这里可以添加更多玩家角色
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
        if (keyStateProvider == null || playerInfos == null || playerInfos.isEmpty()) return;

        // 处理上下选择
        boolean currentUpPressed = keyStateProvider.isUpPressed();
        if (currentUpPressed && !upPressed) {
            selectedIndex = (selectedIndex - 1 + playerInfos.size()) % playerInfos.size();
            upPressed = true;
        } else if (!currentUpPressed) {
            upPressed = false;
        }

        boolean currentDownPressed = keyStateProvider.isDownPressed();
        if (currentDownPressed && !downPressed) {
            selectedIndex = (selectedIndex + 1) % playerInfos.size();
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

        // 处理返回
        boolean currentXPressed = keyStateProvider.isXPressed();
        if (currentXPressed && !xPressed) {
            if (callback != null) {
                callback.onBack();
            }
            xPressed = true;
        } else if (!currentXPressed) {
            xPressed = false;
        }
    }

    /**
     * 处理选择
     */
    private void handleSelection() {
        if (playerInfos != null && !playerInfos.isEmpty() && selectedIndex < playerInfos.size()) {
            PlayerInfo selectedInfo = playerInfos.get(selectedIndex);
            Player selectedPlayer = selectedInfo.getPlayer();
            
            // 为Reimu玩家设置纹理ID
            if (selectedPlayer instanceof __ReimuPlayer) {
                // 注意：这里需要从渲染器获取纹理ID，暂时先设置为-1
                // 实际使用时需要在Window类中获取纹理ID并传递给这里
                ((__ReimuPlayer) selectedPlayer).setReimuTextureId(-1);
            }
            
            startTransition(() -> {
                if (callback != null) {
                    callback.onPlayerSelected(selectedPlayer);
                }
            });
        }
    }

    /**
     * 开始过渡动画
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
     * 渲染玩家选择面板
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        // 绘制背景
        drawBackground(renderer);

        // 绘制标题和玩家列表，应用过渡动画偏移
        float currentOffset = 0;
        if (isTransitioning) {
            // 过渡动画：旧页面向右移动
            currentOffset = transitionOffset;
        } else if (isNewPageAnimating) {
            // 新页面动画：从右侧或左侧进入
            currentOffset = newPageOffset;
        }
        drawTitle(renderer, currentOffset);
        drawPlayers(renderer, currentOffset);
    }

    /**
     * 绘制背景
     * @param renderer 渲染器
     */
    private void drawBackground(IRenderer renderer) {
        // 不绘制背景，使用标题页面的背景
    }

    /**
     * 绘制标题
     * @param renderer 渲染器
     * @param offsetX 偏移量
     */
    private void drawTitle(IRenderer renderer, float offsetX) {
        // 获取字体管理器实例
        FontManager fontManager = FontManager.getInstance();
        Font titleFont = fontManager.getTitleFont();
        String title = "选择玩家";
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
     * 绘制玩家列表
     * @param renderer 渲染器
     * @param offsetX 偏移量
     */
    private void drawPlayers(IRenderer renderer, float offsetX) {
        if (playerInfos == null || playerInfos.isEmpty()) {
            // 绘制无玩家提示
            FontManager fontManager = FontManager.getInstance();
            Font menuFont = fontManager.getFont(32f, Font.BOLD);
            String message = "没有可用的玩家角色";
            float x = getWidth() / 2 - 120 + offsetX;
            float y = getHeight() - getHeight() / 2 - 20;
            
            // 绘制黑色边框
            renderer.drawText(message, x - 1, y - 1, menuFont, Color.BLACK);
            renderer.drawText(message, x + 1, y - 1, menuFont, Color.BLACK);
            renderer.drawText(message, x - 1, y + 1, menuFont, Color.BLACK);
            renderer.drawText(message, x + 1, y + 1, menuFont, Color.BLACK);
            // 绘制未选中颜色
            renderer.drawText(message, x, y, menuFont, UNSELECTED_COLOR);
            return;
        }

        // 获取字体管理器实例
        FontManager fontManager = FontManager.getInstance();
        Font menuFont = fontManager.getFont(32f, Font.BOLD);
        Font descriptionFont = fontManager.getFont(18f, Font.PLAIN);

        for (int i = 0; i < playerInfos.size(); i++) {
            PlayerInfo playerInfo = playerInfos.get(i);
            String name = playerInfo.getName();
            String description = playerInfo.getDescription();
            float x = getWidth() / 2 - 120 + offsetX;
            float y = getHeight() - getHeight() / 2 - 20 - i * 80;

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
                renderer.drawText(name, x - 1, y - 1, menuFont, Color.BLACK);
                renderer.drawText(name, x + 1, y - 1, menuFont, Color.BLACK);
                renderer.drawText(name, x - 1, y + 1, menuFont, Color.BLACK);
                renderer.drawText(name, x + 1, y + 1, menuFont, Color.BLACK);
                // 绘制选中颜色
                renderer.drawText(name, x, y, menuFont, SELECTED_COLOR);
                
                // 绘制黑色边框
                renderer.drawText(description, x + 10 - 1, y + 30 - 1, descriptionFont, Color.BLACK);
                renderer.drawText(description, x + 10 + 1, y + 30 - 1, descriptionFont, Color.BLACK);
                renderer.drawText(description, x + 10 - 1, y + 30 + 1, descriptionFont, Color.BLACK);
                renderer.drawText(description, x + 10 + 1, y + 30 + 1, descriptionFont, Color.BLACK);
                // 绘制选中颜色
                renderer.drawText(description, x + 10, y + 30, descriptionFont, SELECTED_COLOR);
            } else {
                // 绘制黑色边框
                renderer.drawText(name, x - 1, y - 1, menuFont, Color.BLACK);
                renderer.drawText(name, x + 1, y - 1, menuFont, Color.BLACK);
                renderer.drawText(name, x - 1, y + 1, menuFont, Color.BLACK);
                renderer.drawText(name, x + 1, y + 1, menuFont, Color.BLACK);
                // 绘制未选中颜色
                renderer.drawText(name, x, y, menuFont, UNSELECTED_COLOR);
                
                // 绘制黑色边框
                renderer.drawText(description, x + 10 - 1, y + 30 - 1, descriptionFont, Color.BLACK);
                renderer.drawText(description, x + 10 + 1, y + 30 - 1, descriptionFont, Color.BLACK);
                renderer.drawText(description, x + 10 - 1, y + 30 + 1, descriptionFont, Color.BLACK);
                renderer.drawText(description, x + 10 + 1, y + 30 + 1, descriptionFont, Color.BLACK);
                // 绘制未选中颜色
                renderer.drawText(description, x + 10, y + 30, descriptionFont, UNSELECTED_COLOR);
            }
        }
    }

    /**
     * 绘制操作提示
     * @param renderer 渲染器
     */
    private void drawHints(IRenderer renderer) {
        // 移除操作提示
    }

    /**
     * 更新面板
     */
    public void update() {
        handleInput();
    }
}