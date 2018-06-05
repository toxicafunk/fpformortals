package es.richweb.fpmortals.ch1

import scala.concurrent.Future

trait TerminalSync {
  def read(): String
  def write(t: String): Unit
}

trait TerminalAsync {
  def read(): Future[String]
  def write(t: String): Future[Unit]
}

