package es.richweb.fpmortals.ch1

object HKT {

  trait Foo[C[_]] {
    def create(i: Int): C[Int]
  }

  object FooList extends Foo[List] {
    override def create(i: Int): List[Int] = List(i)
  }

  type EitherString[T] = Either[String, T]

  object FooEitherString extends Foo[EitherString] {
    override def create(i: Int): EitherString[Int] = Right(i)
  }

  object FooEitherStringKP extends Foo[Either[String, ?]] {
    override def create(i: Int): Either[String, Int] = Right(i)
  }

  type Id[T] = T

  object FooId extends Foo[Id] {
    override def create(i: Int): Id[Int] = i
  }

}
