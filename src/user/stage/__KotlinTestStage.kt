package user.stage

import stg.stage.Stage
import stg.core.GameWorld
import stg.entity.base.Obj
import user.enemy.__FairyEnemy
import user.enemy.__MidFairyEnemy
import user.enemy.Elf
import user.boss.__MinorikoBoss

class __KotlinTestStage(stageId: Int, stageName: String, gameWorld: GameWorld) : Stage(stageId, stageName, gameWorld) {
    private var fairyCount = 0
    private var midFairyCount = 0
    private var elfCount = 0
    private var hasSpawnedBoss = false
    
    private companion object {
        const val MAX_FAIRIES = 20
        const val MAX_MID_FAIRIES = 8
    }

    override fun initStage() {
        fairyCount = 0
        midFairyCount = 0
        elfCount = 0
        hasSpawnedBoss = false
    }

    override fun onStageStart() {
        fairyCount = 0
        midFairyCount = 0
        elfCount = 0
        hasSpawnedBoss = false
    }

    override fun onStageEnd() {
        // 关卡结束时的清理工作
    }

    override fun updateWaveLogic() {
        val frame = currentFrame
        
        println("Kotlin Stage: updateWaveLogic called, frame: $frame, elfCount: $elfCount")
        
        // 生成Fairy敌人
        if (frame >= 60 && frame % 80 == 0 && fairyCount < MAX_FAIRIES) {
            val x = (Math.random() * 600 - 300).toFloat()
            val fairy = Obj.create(__FairyEnemy::class.java, x, -400f)
            addEnemy(fairy)
            fairyCount++
            println("Kotlin Stage: 生成Fairy敌人: $fairyCount")
        }
        
        // 生成MidFairy敌人
        if (frame >= 200 && frame % 150 == 0 && midFairyCount < MAX_MID_FAIRIES) {
            val x = (Math.random() * 400 - 200).toFloat()
            val midFairy = Obj.create(__MidFairyEnemy::class.java, x, -400f)
            addEnemy(midFairy)
            midFairyCount++
            println("Kotlin Stage: 生成MidFairy敌人: $midFairyCount")
        }
        
        // 每隔1秒（60帧）在屏幕正中心生成一个Elf敌人
        if (frame >= 60 && frame % 50 == 0) {
            val elf = Obj.create(Elf::class.java, 0f, 0f)
            // 设置Elf敌人向上移动
            elf.vy = 3.5f
            addEnemy(elf)
            elfCount++
            println("Kotlin Stage: 生成Elf敌人: $elfCount")
        }
        
        // 生成Boss敌人
        if (frame >= 800 && !hasSpawnedBoss) {
            val boss = Obj.create(__MinorikoBoss::class.java, 0f, -300f)
            addEnemy(boss)
            hasSpawnedBoss = true
            println("Kotlin Stage: 生成Boss敌人")
        }
    }

    override fun nextStage(): Stage? {
        return null
    }

    override fun load() {
        setLoaded()
    }
}
