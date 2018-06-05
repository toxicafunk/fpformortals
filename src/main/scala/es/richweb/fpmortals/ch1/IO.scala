package es.richweb.fpmortals.ch1

import es.richweb.fpmortals.ch1.TerminalHKT.{Execution, Terminal}

class IO[A](val interpret: () => A) {
  def map[B](f: A => B): IO[B] =
    IO(f(interpret())) // uses apply

  def flatMap[B](f: A => IO[B]): IO[B] =
    f(interpret())
}

object IO {
  def apply[A](a: => A): IO[A] = new IO(() => a)
}

object ExecutionIO extends Execution[IO] {
  override def doAndThen[A, B](c: IO[A])(f: A => IO[B]): IO[B] = c.flatMap(f)

  override def create[B](b: B): IO[B] = IO(b)
}

object TerminalIO extends Terminal[IO] {
  override def read: IO[String] = IO {
    io.StdIn.readLine
  }

  override def write(t: String): IO[Unit] = IO {
    println(t)
  }


  def main(args: Array[String]): Unit = {
    import TerminalHKT._

    val delayed: IO[String] = echoM[IO](TerminalIO, ExecutionIO)
    delayed.interpret()
  }
}
