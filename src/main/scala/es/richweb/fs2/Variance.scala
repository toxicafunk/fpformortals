package es.richweb.fs2

sealed trait User { val id: Int }
case class Member(id: Int, name: String) extends User
case class Admin(id: Int, accss: Set[String]) extends User


trait Consumer[-T] {
  def consume(value: T): Unit
}

object Variance {
  val members: List[Member] = List(Member(1, "Jan"), Member(2, "Eric"))

  def consumer: Consumer[User] = (value: User) => print(s"User: ${value.id}\n")

  def main(args: Array[String]): Unit = {
    members.foreach(consumer.consume)
  }

}
