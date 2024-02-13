package xoanaraujo.gdx02.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.FamilyDefinition
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import xoanaraujo.gdx02.component.ImageComponent
import com.github.quillraven.fleks.*

class RenderSystem(
    private val stage: Stage
) : IteratingSystem() {
    override fun onTick() {
        super.onTick()
    }
    override fun onTickEntity(entity: Entity) {
        TODO("Not yet implemented")
    }
}
