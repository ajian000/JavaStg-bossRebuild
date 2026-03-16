# 文档目录

`doc` 目录包含了 JavaSTG 游戏项目各种组件的文档文件，提供了关于不同游戏元素设计和实现的信息。

## 目录中的文件

- **Boss.md**: Boss 相关类和功能的文档
- **Bullet.md**: 子弹相关类和功能的文档
- **Enemy.md**: 敌人相关类和功能的文档
- **EnemySpellcard.md**: 敌人符卡相关的文档
- **IBullet.md**: IBullet 接口的文档
- **IEnemy.md**: IEnemy 接口的文档
- **IGameObject.md**: IGameObject 接口的文档
- **IItem.md**: IItem 接口的文档
- **IPlayer.md**: IPlayer 接口的文档
- **Item.md**: 道具相关的文档
- **Laser.md**: 激光相关的文档
- **Player.md**: 玩家相关的文档
- **Stage.md**: 关卡相关的文档
- **TEAM_CODE_STYLE.md**: 团队代码风格规范
- **UNUSED_CLASSES.md**: 未使用的类的文档

## 文档用途

这些文档文件详细说明了各个游戏组件的设计思路、接口定义和使用方法，帮助开发者：

1. **理解代码结构**: 快速了解各个组件的设计和实现
2. **遵循开发规范**: 按照文档中定义的接口和规范进行开发
3. **扩展游戏功能**: 基于现有组件和接口扩展新功能
4. **维护代码质量**: 确保代码修改符合项目规范和设计意图

## 文档结构

文档按功能模块组织，对应代码库的实际结构：

- **核心实体**: Player.md, Enemy.md, Bullet.md, Item.md, Laser.md
- **接口定义**: IPlayer.md, IEnemy.md, IBullet.md, IItem.md, IGameObject.md
- **特殊实体**: Boss.md, EnemySpellcard.md
- **关卡系统**: Stage.md
- **代码规范**: TEAM_CODE_STYLE.md
- **维护信息**: UNUSED_CLASSES.md

## 使用建议

1. **开发前参考**: 在开发新功能前，参考相关组件的文档
2. **维护文档更新**: 当修改代码时，同步更新相关文档
3. **保持文档一致性**: 确保文档内容与实际代码保持一致
4. **遵循代码规范**: 参考 TEAM_CODE_STYLE.md 中的代码风格规范

## 注意事项

- 文档应简洁明了，重点突出组件的核心功能和使用方法
- 对于复杂的组件，可提供示例代码帮助理解
- 定期检查文档的完整性和准确性
- 文档更新应与代码变更保持同步
- 新增功能或修改现有功能时，应同时更新相关文档