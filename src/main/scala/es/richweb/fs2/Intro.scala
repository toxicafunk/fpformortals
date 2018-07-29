package es.richweb.fs2

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import scala.language.higherKinds

trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A => B): F[B]
}

trait Monad[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]
  def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]
}

object Intro {
  val fut: Future[Int] = Future(1)

  type IO[A] = () => A

  object IO {
    def apply[A](a: => A): IO[A] =
      () => a
  }

  implicit val ioMonad: Monad[IO] =
    new Monad[IO] {
        def pure[A](a: A): IO[A] = IO(a)

        def map[A,B](io: IO[A])(f: A => B): IO[B] =
          IO(f(io()))

        def flatMap[A,B](io: IO[A])(f: A => IO[B]): IO[B] =
          f(io())
      }

  def print(message: String): IO[Unit] =
    () => println(message)

  def printIO(msg: String)(implicit io: Monad[IO]): IO[Unit] = 
    io.map(IO(msg))(println)

  def main(args: Array[String]): Unit = {
    //fut.foreach { i => println(i + i) }
    val r = Await.result(fut, 1.second)
    def p = println(r + r)
    val io = printIO("Look ma!!!! No effect!")
    io()
    p
    io()
  }
}
