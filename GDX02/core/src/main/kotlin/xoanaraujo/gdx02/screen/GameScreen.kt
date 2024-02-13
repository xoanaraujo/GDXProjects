package xoanaraujo.gdx02.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.log.logger
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import ktx.assets.disposeSafely
class GameScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val texture: Texture = Texture("assets/graphics/player.png")
    private val world: World = configureWorld {  }
    override fun show() {
        log.debug { "GameScreen show" }
        stage.addActor(
            Image(texture).apply {
                setPosition(1f, 1f)
                setSize(1f, 1f)
                setScaling(Scaling.fill)
            }
        )
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
        with(stage){
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        stage.disposeSafely()
        texture.disposeSafely()
        world.dispose()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
