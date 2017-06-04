package com.lilypepper.blah

import java.time.LocalTime
import java.util.UUID

import org.scalajs.dom._

import scala.scalajs.js.JSApp


object SvgApp extends JSApp {
  val bundle = scalatags.JsDom

  import bundle.TypedTag

  type ID = String

  lazy val Clock: ID = s"clock-${UUID.randomUUID()}"
  lazy val Face: ID = s"clock-face-${UUID.randomUUID()}"
  lazy val Ticks: ID = s"clock-ticks-group-${UUID.randomUUID()}"
  lazy val Hands: ID = s"clock-hands-group-${UUID.randomUUID()}"
  lazy val Hour: ID = s"clock-hands-hour-${UUID.randomUUID()}"
  lazy val Minute: ID = s"clock-hands-minute-${UUID.randomUUID()}"
  lazy val Second: ID = s"clock-hands-second-${UUID.randomUUID()}"
  lazy val Knob: ID = s"clock-knob-${UUID.randomUUID()}"

  def main(): Unit = {


    document.body.appendChild(doSvg().render)

    val clock = document.getElementById(Clock).asInstanceOf[svg.SVG]
    val hour = document.getElementById(Hour).asInstanceOf[svg.Path]
    val minute = document.getElementById(Minute).asInstanceOf[svg.Path]
    val second = document.getElementById(Second).asInstanceOf[svg.Path]

    window.setInterval(() => requestAnimateOneFrame(), 200)

    def requestAnimateOneFrame(): Unit = {
      window.requestAnimationFrame(animate)
    }

    def animate(timeStamp: Double): Unit = {
      val time = LocalTime.now

      lazy val s = time.getSecond + time.getNano / 1000000000.0
      lazy val m = time.getMinute + s / 60.0
      lazy val h = time.getHour + m / 60.0

      clock.pauseAnimations()
      try {
        hour.setAttribute("transform", s"rotate(${h * 360 / 12})")
        minute.setAttribute("transform", s"rotate(${m * 360 / 60})")
        second.setAttribute("transform", s"rotate(${s * 360 / 60})")
      } finally {
        clock.unpauseAnimations()
      }
    }
  }

  def doSvg(): TypedTag[svg.SVG] = {
    import bundle.implicits._
    import bundle.svgAttrs._
    import bundle.svgTags._

    svg(id := Clock, width := "250", height := "250", viewBox := "0 0 250 250",
      desc("Analog Clock"),

      circle(id := Face, cx := 125, cy := 125, r := 100, style := "fill:white;stroke:black;stroke-width:2px;"),

      g(
        Seq(id := Ticks, transform := "translate(125,125)") ++
          (for (a <- 30 to 360 by 30) yield
            path(d := "M 95,0 L 100,-5 L 100,5 z", transform := s"rotate($a)")): _*),

      g(Seq(id := "Blah", transform := "translate(125,125)", style := "stroke:black;stroke-width:2px;stroke-linecap:round;") ++
        (for (a <- 0 to 360 by 6 if a % 30 != 0) yield
          path(d := "M0,-95 L0,-100", transform := s"rotate($a)")): _*),

      g(id := Hands, style := "stroke:black;stroke-width:5px;stroke-linecap:round;", transform := "translate(125,125)",
        path(id := Second, d := "M0,0 l0,-85", transform := "rotate(0)", style := "stroke:red;stroke-width:2px"),
        path(id := Hour, d := "M0,0 l0,-50", transform := "rotate(0)", style := "stroke-width:8px"),
        path(id := Minute, d := "M0,0 l0,-80", transform := "rotate(0)", style := "stroke-width:6px")
      ),

      circle(id := Knob, cx := 125, cy := 125, r := 9, style := "fill:#555")
    )
  }
}
