import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.input.MouseEvent._
import scalafx.scene.layout.{BorderPane, FlowPane, GridPane, Pane, StackPane, TilePane}
import scalafx.scene.paint.Color._
import scalafx.scene.shape.{Circle, Line, Rectangle}
import scalafx.scene.transform.Rotate

object Test extends JFXApp {
  object wall extends Line {
    stroke = Yellow
    startX = 70
    startY = 20
    endX = 70
    endY = 70
  }

  object player extends Line {
    val angle = new Rotate() {
      angle = 0
    }

    stroke = White
    strokeWidth = 5
    startX = 50
    startY = 50
    endX = math.cos(angle.getAngle) * 5 + startX.value
    endY = math.sin(angle.getAngle) * 5 + startY.value

    def moveForward() = {
      startX = startX.value + math.cos(angle.getAngle)
      endX = startX.value + math.cos(angle.getAngle) * 5
      startY = startY.value + math.sin(angle.getAngle)
      endY = startY.value + math.sin(angle.getAngle) * 5
      println("x:" + startX.value,endX.value + " --- y:" + startY.value,endY.value)
    }
    def moveBackward() = {
      startX = startX.value - math.cos(angle.getAngle)
      endX = startX.value + math.cos(angle.getAngle) * 5
      startY = startY.value + math.sin(angle.getAngle)
      endY = startY.value - math.sin(angle.getAngle)
      println("x:" + startX.value,endX.value + " --- y:" + startY.value,endY.value)
    }
    def turnLeft() = {
      angle.setAngle(angle.getAngle - 0.1)
    }
    def turnRight() = {

    }
    def strafeLeft() = {
      startX = startX.value + math.sin(angle.getAngle)
      endX = startX.value + math.cos(angle.getAngle) * 5
      startY = startY.value - math.cos(angle.getAngle)
      endY = startY.value - math.sin(angle.getAngle) * 5
      println("x:" + startX.value,endX.value + " --- y:" + startY.value,endY.value)
    }
    def strafeRight() = {
      startX = startX.value - math.sin(angle.getAngle)
      endX = startX.value + math.cos(angle.getAngle) * 5
      startY = startY.value + math.cos(angle.getAngle)
      endY = startY.value + math.sin(angle.getAngle) * 5
      println("x:" + startX.value,endX.value + " --- y:" + startY.value,endY.value)
    }
  }


  stage = new PrimaryStage {
    scene = new Scene(340,120) {
      fill = Black
      content = List(wall,player)
      onKeyPressed = (e:KeyEvent) => {
        if (e.code == KeyCode.W) {
          player.moveForward
        }
        if (e.code == KeyCode.S) {
          player.moveBackward
        }
        if (e.code == KeyCode.E) {
          player.turnLeft
        }
        if (e.code == KeyCode.Q) {
          player.turnRight
        }
        if (e.code == KeyCode.A) {
          player.strafeLeft
        }
        if (e.code == KeyCode.D) {
          player.strafeRight
        }
      }
    }
  }
}