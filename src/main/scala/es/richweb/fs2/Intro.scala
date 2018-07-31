package es.richweb.fs2

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import scala.language.higherKinds

object Intro {
  val fut: Future[Int] = Future(1)

  case class IO[A](unsafeRun: () => A) { self =>
    def map[B](f: A => B): IO[B] = IO(() => f(self.unsafeRun()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.unsafeRun()).unsafeRun())
  }

  object IO {
    //def apply[A](a: A): IO[A] =
    //  new IO(() => a)

    implicit val ioMonad: Monad[IO] =
      new Monad[IO] {
        def pure[A](a: A): IO[A] = IO(() => a)

        def map[A,B](io: IO[A])(f: A => B): IO[B] =
          io.map(f)

        def flatMap[A,B](io: IO[A])(f: A => IO[B]): IO[B] =
          io.flatMap(f)
      }

    def pure[A](a: => A): IO[A] = IO(() => a)
  }

  trait Functor[F[_]] {
    def map[A,B](fa: F[A])(f: A => B): F[B]
  }

  trait Monad[F[_]] extends Functor[F] {
    def pure[A](a: A): F[A]
    def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]
  }

  object Monad {
    def apply[F[_]](implicit F: Monad[F]) = F
  }

  implicit class MonadSyntax[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit F: Monad[F]) = F.map(fa)(f)

    def flatMap[B](f: A => F[B])(implicit F: Monad[F]) = F.flatMap(fa)(f)
  }

  def pure[F[_],A](a: => A)(implicit F: Monad[F]): F[A] = F.pure(a)

  def print(message: String): IO[Unit] =
    IO(() => println(message))

  def printIO(msg: String)(implicit io: Monad[IO]): IO[Unit] = 
    IO.pure(msg).map(println)

  def main(args: Array[String]): Unit = {
    //fut.foreach { i => println(i + i) }
    val r = Await.result(fut, 1.second)
    def p = println(r + r)
    val io = printIO("Look ma!!!! No effect!")
    io.unsafeRun()
    p
    io.unsafeRun()
  }
}
