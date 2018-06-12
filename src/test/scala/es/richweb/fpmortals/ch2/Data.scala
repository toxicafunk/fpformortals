package es.richweb.fpmortals.ch2

import java.time.Instant

import es.richweb.fpmortals.ch1.HKT.Id
import scalaz.NonEmptyList

object Data {
  val node1 = MachineNode("1243d1af-828f-4ba3-9fc0-a19d86852b5a")
  val node2 = MachineNode("550c4943-229e-47b0-b6be-3d686c5f013f")
  val managed = NonEmptyList(node1, node2)

  import java.time.Instant.parse
  val time1 = parse("2017-03-03T18:07:00.000+01:00[Europe/London]")
  val time2 = parse("2017-03-03T18:59:00.000+01:00[Europe/London]") // +52 mins
  val time3 = parse("2017-03-03T19:06:00.000+01:00[Europe/London]") // +59 mins
  val time4 = parse("2017-03-03T23:07:00.000+01:00[Europe/London]") // +5 hours

  val needsAgents = Worldview(5, 0, managed, Map.empty, Map.empty, time1)
}

class Mutable(state: Worldview) {
  var started, stopped = 0

  private val D: Drone[Id] = new Drone[Id] {
    override def getBacklog: Id[Int] = state.backlog

    override def getAgents: Id[Int] = state.agents
  }

  private val M: Machines[Id] = new Machines[Id] {
    override def getTime: Instant = state.time

    override def getManaged: NonEmptyList[MachineNode] = state.managed

    override def getAlive: Map[MachineNode, Instant] = state.alive

    override def start(node: MachineNode): MachineNode = { started += 1; node }

    override def stop(node: MachineNode): MachineNode = {stopped += 1; node }
  }

  val program = new DynAgents[Id](D, M)
}
