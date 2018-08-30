package es.richweb.fs2

sealed trait Stream[A] {
  import Stream._

  def map[B](f: A => B): Stream[B] =
    Map(this, f)

  def zip[B](that: Stream[B]): Stream[(A,B)] =
    Zip(this, that)

  def flatMap[B](f: A => Stream[B]): Stream[B] =
    FlatMap(this, f)

  def run[B](zero: B)(f: (B, A) => B): B =
    ???
  /* def loop[C](stream: Stream[C])(zero: B)(f: (B,C) => B): B =
      stream match {
        case Map
      }
   */ 
}

object Stream {
  final case class Map[A,B](source: Stream[A], f: A => B) extends Stream[B]

  final case class Zip[A,B](source: Stream[A], that: Stream[B]) extends Stream[(A,B)]

  final case class FlatMap[A, B](source: Stream[A], f: A => Stream[B]) extends Stream[B]

  final case class Run[A,B](source: Stream[A], zero: B, f: (B,A) => B) extends Stream[B]

  def fromIterator[A](source: Iterator[A]): Stream[A] =
    ???
}
