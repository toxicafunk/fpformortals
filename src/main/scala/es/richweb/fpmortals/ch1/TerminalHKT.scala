package es.richweb.fpmortals.ch1

import scala.concurrent.Future

object TerminalHKT {

  trait Terminal[C[_]] {
    def read: C[String]

    def write(t: String): C[Unit]
  }

  type Now[T] = T

  object TerminalSync extends Terminal[Now] {
    override def read: String = ???

    override def write(t: String): Unit = ???
  }

  object TerminalAsync extends Terminal[Future] {
    override def read: Future[String] = ???

    override def write(t: String): Future[Unit] = ???
  }

  trait Execution[C[_]] {
    def doAndThen[A,B](c: C[A])(f: A => C[B]): C[B]
    def create[B](b: B): C[B]
  }

  def echo[C[_]](t: Terminal[C], e: Execution[C]): C[String] =
    e.doAndThen(t.read) { in: String =>
      e.doAndThen(t.write(in)) { _: Unit =>
        e.create(in)
      }
    }

  object Execution {
    implicit class Ops[A, C[_]](c: C[A]) {
      def flatMap[B](f: A => C[B])(implicit e: Execution[C]): C[B] =
        e.doAndThen(c)(f)

      def map[B](f: A => B)(implicit e: Execution[C]): C[B] =
        e.doAndThen(c)(f andThen e.create)
    }
  }

  import Execution._

  def echoM[C[_]](implicit t: Terminal[C], e: Execution[C]): C[String] =
    t.read.flatMap { in: String =>
      t.write(in).map { _: Unit =>
        in
      }
    }

  def echoF[C[_]](implicit t: Terminal[C], execution: Execution[C]): C[String] =
    for {
      in <- t.read
      _ <- t.write(in)
    } yield in
}
