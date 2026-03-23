package stg.entity.enemy;

import java.awt.Color;
import java.awt.Graphics2D;

import stg.core.GameWorld;
import stg.entity.base.Obj;
import stg.render.IRenderable;
import stg.render.IRenderer;
import stg.util.objectpool.Resettable;

/**
 * 敌方单位
 * 所有敌人的基类
 * @date 2026-01-19
 * @date 2026-02-20 支持对象池管理
 */
public abstract class Enemy extends Obj implements Resettable, IRenderable {
	protected int hp; // 生命值
	protected int maxHp; // 最大生命值
	protected GameWorld gameWorld; // 游戏世界引用

	public Enemy(int x, int y) {
		this(x, y, 0, 0, 20, Color.BLUE, 10);
	}

	public Enemy(float x, float y) {
		this(x, y, 0, 0, 20, Color.BLUE, 10);
	}

	public Enemy(float x, float y, float vx, float vy, float size, Color color, int hp) {
		super(x, y, vx, vy, size, color);
		this.hp = hp;
		this.maxHp = hp;
	}

	/**
	 * 更新敌人状态
	 * @since 2026-01-19 基类提供基本移动和存活检测
	 */
	@Override
	public void update() {
		super.update();

		// 检查生命值
		if (hp <= 0) {
			setActive(false);
		}
	}
	
	/**
	 * 更新敌人状态
	 * @param canvasWidth 画布宽度
	 * @param canvasHeight 画布高度
	 * @since 2026-02-13 支持动态画布尺寸
	 */
	public void update(int canvasWidth, int canvasHeight) {
		update(); // 调用无参数版本
	}

	/**
	 * 渲染敌人 - @since 2026-01-19 使用中心原点坐标系
	 * @param g 图形上下文
	 * @since 2026-01-19 基类提供基本渲染,子类可自定义渲染
	 */
	@Override
	public void render(Graphics2D g) {
		if (!isActive()) return;

		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(getColor());
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()), (int)(getSize() * 2), (int)(getSize() * 2));

		renderHealthBar(g, screenX, screenY);
	}

	/**
     * 渲染敌人（IRenderer版本，支持OpenGL）
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        if (!isActive()) return;

        // 转换为屏幕坐标
        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(getX(), getY());
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];

        // 绘制敌人主体
        Color color = getColor();
        renderer.drawCircle(screenX, screenY, getSize(), color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);

        // 渲染生命值条
        renderHealthBar(renderer);
    }
    
    /**
     * 获取渲染层级
     * @return 渲染层级
     */
    @Override
    public int getRenderLayer() {
        return 4; // 敌人渲染层级，低于玩家的5
    }
    
    /**
     * 在屏幕中渲染敌人
     * @param renderer 渲染器
     */
    public void renderOnScreen(IRenderer renderer) {
        render(renderer);
    }

	/**
	 * 渲染生命值条 - 使用屏幕坐标
	 * @param renderer 渲染器
	 */
	protected void renderHealthBar(IRenderer renderer) {
		// 转换为屏幕坐标
		requireCoordinateSystem();
		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];
		
		float barWidth = getSize() * 2;
		float barHeight = 4;
		float barX = screenX - getSize();
		float barY = screenY - getSize() - 8;

		// 背景
		renderer.drawRect(barX, barY, barWidth, barHeight, 0.5f, 0.5f, 0.5f, 1.0f);

		// 生命值
		float hpPercent = (float)hp / maxHp;
		renderer.drawRect(barX, barY, barWidth * hpPercent, barHeight, 1.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * 渲染生命值条 - @since 2026-01-19 使用屏幕坐标
	 * @param g 图形上下文
	 * @param screenX 屏幕X坐标
	 * @param screenY 屏幕Y坐标
	 * @since 2026-01-19 在敌人上方显示生命值
	 */
	protected void renderHealthBar(Graphics2D g, float screenX, float screenY) {
		int barWidth = (int)(getSize() * 2);
		int barHeight = 4;
		int barX = (int)(screenX - getSize());
		int barY = (int)(screenY - getSize() - 8);

		// 背景
		g.setColor(Color.GRAY);
		g.fillRect(barX, barY, barWidth, barHeight);

		// 生命值
		float hpPercent = (float)hp / maxHp;
		g.setColor(Color.RED);
		g.fillRect(barX, barY, (int)(barWidth * hpPercent), barHeight);
	}

	/**
	 * 受到伤害
	 * @param damage 伤害值
	 * @since 2026-01-19 处理伤害逻辑
	 */
	public void takeDamage(int damage) {
		hp -= damage;
		if (hp <= 0) {
			hp = 0;
			setActive(false);
			onDeath(); // 调用死亡回调
		}
	}

	/**
	 * 死亡回调 - 子类可重写
	 * @since 2026-01-19 敌人死亡时触发
	 */
	protected void onDeath() {
		// 子类可以重写此方法添加死亡特效、掉落物品等
	}

	/**
	 * 检查是否越界 - @since 2026-01-19 使用游戏逻辑坐标系
	 * @return 是否越界
	 */
	@Override
	public boolean isOutOfBounds() {
		return isOutOfBounds(0, 0); // 调用兼容版本
	}

	/**
	 * 检查是否越界 - @since 2026-01-19 使用游戏逻辑坐标系
	 * @param canvasWidth 画布宽度（兼容参数，不使用）
	 * @param canvasHeight 画布高度（兼容参数，不使用）
	 * @return 是否越界
	 */
	public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
		// 使用游戏逻辑坐标系的固定边界
		stg.entity.base.Obj.requireCoordinateSystem();
		stg.util.CoordinateSystem cs = stg.entity.base.Obj.getSharedCoordinateSystem();
		float leftBound = cs.getLeftBound() - getSize() * 2;
		float rightBound = cs.getRightBound() + getSize() * 2;
		float topBound = cs.getBottomBound() - getSize() * 2;
		float bottomBound = cs.getTopBound() + getSize() * 2;

		return getX() < leftBound || getX() > rightBound ||
		       getY() < topBound || getY() > bottomBound;
	}

	/**
	 * 检查是否存活
	 * @return 是否存活
	 */
	public boolean isAlive() {
		return isActive();
	}

	/**
	 * 获取当前生命值
	 * @return 生命值
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * 设置生命值
	 * @param hp 生命值
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * 获取最大生命值
	 * @return 最大生命值
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * 设置游戏世界引用
	 * @param gameWorld 游戏世界引用
	 */
	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	/**
	 * 获取游戏世界引用
	 * @return 游戏世界引用
	 */
	public GameWorld getGameWorld() {
		return gameWorld;
	}

	/**
	 * 重置敌人状态
	 */
	@Override
	public void reset() {
		super.reset();
		this.hp = maxHp;
	}

	/**
	 * 任务开始时触发的方法 - 用于处理开局对话等
	 */
	protected abstract void onTaskStart();

	/**
     * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉落
     */
    protected abstract void onTaskEnd();
    
    /**
     * 重置敌人状态
     * 当敌人被回收到对象池时调用
     */
    @Override
    public void resetState() {
        // 重置敌人的基本属性
        setActive(true);
        setX(0);
        setY(0);
        setVx(0);
        setVy(0);
        hp = maxHp; // 重置生命值到最大值
        // 保留gameWorld引用，因为它在游戏过程中是不变的
    }
}