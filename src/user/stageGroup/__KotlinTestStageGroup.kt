package user.stageGroup

import stg.stage.StageGroup
import stg.core.GameWorld
import user.stage.__KotlinTestStage

class __KotlinTestStageGroup(gameWorld: GameWorld) : StageGroup("Kotlin Test Stage Group", "A test stage created with Kotlin", StageGroup.Difficulty.NORMAL, gameWorld) {
    override fun initStages() {
        addStage(__KotlinTestStage(1, "Kotlin Test Stage", gameWorld))
    }
}
