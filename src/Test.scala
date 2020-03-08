import scalafx.Includes._
import scalafx.animation.{AnimationTimer, KeyFrame, Timeline}
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
import scalafx.util.Duration

object Test extends JFXApp {
  class Map(xPos:Double,yPos:Double,w:Double,h:Double) extends Rectangle {
    stroke = Blue
    x = xPos
    y = yPos
    width = w
    height = h
  }
  class Wall(x1:Double,y1:Double,x2:Double,y2:Double) extends Line {
    stroke = Yellow
    startX = x1
    startY = y1
    endX = x2
    endY = y2
  }
  class Player(x:Double,y:Double,a:Double) extends Line {
    stroke = White
    strokeWidth = 3
    startX = x
    startY = y
    val angle = new Rotate() {
      angle = math.toRadians(a)
      pivotX = startX.value
      pivotY = startY.value
    }
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
      angle.setAngle(angle.getAngle - math.toRadians(5))
      println(angle.getPivotX,angle.getPivotY,angle.getAngle)
      endX = startX.value + math.cos(angle.getAngle) * 5
      endY = startY.value + math.sin(angle.getAngle) * 5
    }
    def turnRight() = {
      angle.setAngle(angle.getAngle + math.toRadians(5))
      println(angle.getPivotX,angle.getPivotY,angle.getAngle)
      endX = startX.value + math.cos(angle.getAngle) * 5
      endY = startY.value + math.sin(angle.getAngle) * 5
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

  var (px: Double, py: Double, angle: Double) = (50.0, 50.0, 0.0)

  stage = new PrimaryStage {
    scene = new Scene(340,200) {
      onKeyPressed = (e: KeyEvent) => {
        if (e.code == KeyCode.W) {
          px = px + math.cos(angle)
          py = py + math.sin(angle)
        }
        if (e.code == KeyCode.S) {
          println("m")
          px = px - math.cos(angle)
          py = py - math.sin(angle)
        }
        if (e.code == KeyCode.Q) {
          angle = angle - 0.1
        }
        if (e.code == KeyCode.E) {
          angle = angle + 0.1
        }
        if (e.code == KeyCode.A) {
          px = px + math.sin(angle)
          py = py - math.cos(angle)
        }
        if (e.code == KeyCode.D) {
          px = px - math.sin(angle)
          py = py + math.cos(angle)
        }
        if (e.code == KeyCode.Escape)
          System.exit(0)

        fill = Black

        val (vx1: Double, vy1: Double, vx2: Double, vy2: Double) = (70.0, 20.0, 70.0, 70.0)

        //Draw absolute map
        val map1 = new Map(4, 40, 99, 109)
        val wall1 = new Line {
          stroke = Yellow
          startX = vx1
          startY = vy1
          endX = vx2
          endY = vy2
          translateX = 4
          translateY = 40
        }
        var player1 = new Line {
          stroke = White
          startX = px
          startY = py
          endX = math.cos(angle) * 5 + px
          endY = math.sin(angle) * 5 + py
          translateX = 4
          translateY = 40
        }

        //Draw transformed map
        val map2 = new Map(109, 40, 99, 109)

        //Transform vertexes relative to player
        var (tx1, ty1) = (vx1 - px, vy1 - py)
        var (tx2, ty2) = (vx2 - px, vy2 - py)
        //Rotate around player
        var tz1 = tx1 * math.cos(angle) + ty1 * math.sin(angle)
        var tz2 = tx2 * math.cos(angle) + ty2 * math.sin(angle)
        tx1 = tx1 * math.sin(angle) - ty1 * math.cos(angle)
        tx2 = tx2 * math.sin(angle) - ty2 * math.cos(angle)

        var wall2 = new Line {
          stroke = Yellow
          startX = 50 - tx1
          startY = 50 - tz1
          endX = 50 - tx2
          endY = 50 - tz2
          translateX = 109
          translateY = 40
        }
        val player2 = new Line {
          stroke = White
          startX = 50
          startY = 50
          endX = 50
          endY = 45
          translateX = 109
          translateY = 40
        }

        //Draw perspective-transformed map
        val map3 = new Map(214, 40, 99, 109)
        var t = new Line
        var b = new Line
        var l = new Line
        var r = new Line

        if (tz1 > 0 || tz2 > 0) {
          //If line crosses viewpoint, clip it
          var (ix1, iz1) = Intersect(tx1, tz1, tx2, tz2, -0.0001, 0.0001, -20, 5)
          var (ix2, iz2) = Intersect(tx1, tz1, tx2, tz2, 0.0001, 0.0001, 20, 5)
          if (tz1 <= 0) {
            if (iz1 > 0) {
              tx1 = ix1
              tz1 = iz1
            } else {
              tx1 = ix2
              tz1 = iz2
            }
          }
          if (tz2 <= 0) {
            if (iz1 > 0) {
              tx2 = ix1
              tz2 = iz1
            } else {
              tx2 = ix2
              tz2 = iz2
            }
          }

          var x1 = -tx1 * 16 / tz1
          var y1a = -50 / tz1
          var y1b = 50 / tz1
          var x2 = -tx2 * 16 / tz2
          var y2a = -50 / tz2
          var y2b = 50 / tz2


          t.stroke = Yellow
          t.startX = 50 + x1
          t.startY = 50 + y1a
          t.endX = 50 + x2
          t.endY = 50 + y2a
          t.translateX = 214
          t.translateY = 40

          b.stroke = Yellow
          b.startX = 50 + x1
          b.startY = 50 + y1b
          b.endX = 50 + x2
          b.endY = 50 + y2b
          b.translateX = 214
          b.translateY = 40

          l.stroke = Yellow
          l.startX = 50 + x1
          l.startY = 50 + y1a
          l.endX = 50 + x1
          l.endY = 50 + y1b
          l.translateX = 214
          l.translateY = 40

          r.stroke = Yellow
          r.startX = 50 + x2
          r.startY = 50 + y2a
          r.endX = 50 + x2
          r.endY = 50 + y2b
          r.translateX = 214
          r.translateY = 40
        }
        content = List(map1, wall1, player1, map2, player2, wall2, map3, t, b, l, r)
      }
    }
  }

  def Cross(x1:Double,y1:Double,x2:Double,y2:Double):Double = {
    x1 * y2 - y1 * x2
  }

  def Intersect(x1:Double,y1:Double,x2:Double,y2:Double,x3:Double,y3:Double,x4:Double,y4:Double):(Double,Double) = {
    val a = Cross(x1,y1,x2,y2)
    val b = Cross(x3,y3,x4,y4)
    val det = Cross(x1-x2,y1-y2,x3-x4,y3-y4)
    val x = Cross(a,x1-x2,b,x3-x4) / det
    val y = Cross(a,y1-y2,b,y3-y4) / det
    (x,y)
  }
}
