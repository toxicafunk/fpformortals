package es.richweb.fpmortals.ch4

final case class Person private(name: String, age: Int)

object Person {
  def apply(name: String, age: Int): Either[String, Person] = {
    if (name.nonEmpty && age > 0) Right(new Person(name, age))
    else Left(s"Bad input: $name, $age")
  }

  def main(args: Array[String]): Unit = {
    def welcome(person: Person): String = s"Welcome ${person.name}, you look great at ${person.age}!"

    val t1 = for {
      p <- Person("", -1)
    } yield welcome(p)

    println(t1)

    val t2 = for {
      p <- Person("Eric", 40)
    } yield welcome(p)

    println(t2)
  }
}
