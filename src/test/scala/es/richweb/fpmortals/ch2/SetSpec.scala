package es.richweb.fpmortals.ch2

import es.richweb.fpmortals.ch2.Data._
import org.scalatest.Matchers._
import org.scalatest._

class SetSpec extends FlatSpec {
  "Business Logic" should "generate an initial world view" in {
    val mutable =  new Mutable(needsAgents)
    import mutable._
    program.initial shouldBe needsAgents
  }

  it should "remove changed nodes from pending" in {
    val world = Worldview(0, 0, managed, Map(node1 -> time3), Map.empty, time3)
    val mutable = new Mutable(world)
    import mutable._

    val old = world.copy(alive = Map.empty,
      pending = Map(node1 -> time2),
      time = time2)
    program.update(old) shouldBe world
  }

  it should "request agents when needed" in {
    val mutable = new Mutable(needsAgents)
    import mutable._

    val expected = needsAgents.copy(
      pending = Map(node1 -> time1)
    )

    program.act(needsAgents) shouldBe expected

    mutable.stopped shouldBe 0
    mutable.started shouldBe 1
  }
}