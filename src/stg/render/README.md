# render 目录

## 功能说明

render 目录包含游戏的渲染系统，负责处理所有图形渲染相关的功能，包括窗口绘制、面板管理、字体渲染等。

## 主要文件

### 核心渲染接口
- **IRenderable.java**: 可渲染对象接口，定义了渲染方法
- **IRenderer.java**: 渲染器接口，定义了渲染器的基本方法

### 渲染器实现
- **GLRenderer.java**: OpenGL渲染器实现，负责底层图形渲染
- **STBFontRenderer.java**: STB字体渲染器，负责文本渲染
- **VirtualKeyboardRenderer.java**: 虚拟键盘渲染器

### 面板系统
- **Panel.java**: 面板基类，定义了面板的基本行为
- **GamePanel.java**: 游戏主面板
- **LeftPanel.java**: 左侧面板
- **RightPanel.java**: 右侧面板
- **TitlePanel.java**: 标题面板
- **PlayerSelectPanel.java**: 玩家选择面板
- **StageGroupSelectPanel.java**: 关卡组选择面板

### 辅助类
- **FontManager.java**: 字体管理器，负责字体加载和管理
- **PanelCoordinateSystem.java**: 面板坐标系，处理面板内的坐标转换

## 功能详解

1. **渲染系统**:
   - 使用OpenGL进行底层图形渲染
   - 支持2D图形和文本渲染
   - 提供统一的渲染接口

2. **面板系统**:
   - 模块化的面板设计
   - 支持多面板布局
   - 面板间的切换和交互

3. **字体渲染**:
   - 支持TTF字体加载
   - 文本渲染和排版
   - 字体资源管理

## 代码结构

```
render/
├── FontManager.java            # 字体管理器
├── GLRenderer.java             # OpenGL渲染器
├── GamePanel.java              # 游戏主面板
├── IRenderable.java            # 可渲染接口
├── IRenderer.java              # 渲染器接口
├── LeftPanel.java              # 左侧面板
├── Panel.java                  # 面板基类
├── PanelCoordinateSystem.java  # 面板坐标系
├── PlayerSelectPanel.java      # 玩家选择面板
├── RightPanel.java             # 右侧面板
├── STBFontRenderer.java        # STB字体渲染器
├── StageGroupSelectPanel.java  # 关卡组选择面板
├── TitlePanel.java             # 标题面板
└── VirtualKeyboardRenderer.java # 虚拟键盘渲染器
```