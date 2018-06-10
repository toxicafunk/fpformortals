package es.richweb.fpmortals.ch2

import java.time.Instant
import java.time.temporal.ChronoUnit

import scalaz._
import Scalaz._

import scala.concurrent.duration._

final case class Worldview (backlog: Int, agents: Int, managed: NonEmptyList[MachineNode],
                            alive: Map[MachineNode, Instant], pending: Map[MachineNode, Instant],
                            time: Instant)

final case class DynAgents[F[_]](D: Drone[F], M: Machines[F])(implicit F: Monad[F]) {
  def initial: F[Worldview] = for {
    db <- D.getBacklog
    da <- D.getAgents
    mn <- M.getManaged
    ma <- M.getAlive
    mt <- M.getTime
  } yield Worldview(db, da, mn, ma, Map.empty, mt)

  def update(old: Worldview): F[Worldview] = for {
    snap <- initial
    changed = symdiff(old.alive.keySet, snap.alive.keySet)
    pending = (old.pending -- changed).filterNot {
      case (_, started) => timediff(started, snap.time) >= 10.minutes
    }
    update = snap.copy(pending = pending)
  } yield update

  private def symdiff[T](a: Set[T], b: Set[T]): Set[T] =
    (a union b) -- (a intersect b)

  private def timediff(from: Instant, to: Instant): FiniteDuration =
    ChronoUnit.MINUTES.between(from, to).minutes

  private object NeedsAgent {
    def unapply(world: Worldview): Option[MachineNode] = world match {
      case Worldview(backlog, 0, managed, alive, pending, _) if backlog > 0 && alive.isEmpty && pending.isEmpty =>
        Option(managed.head)
      case _ => None
    }
  }

  private object Stale {
    def unapply(world: Worldview): Option[NonEmptyList[MachineNode]] = world match {
      case Worldview(backlog, _, _, alive, pending, time) if alive.nonEmpty => (alive --  pending.keys).collect {
        case (n, started) if backlog == 0 && timediff(started, time).toMinutes % 60 >= 58 => n
        case (n, started) if timediff(started, time) >= 5.hours => n
      }.toList.toNel
      case _ => None
    }
  }

  def act(world: Worldview): F[Worldview] = world match {
    case NeedsAgent(node) => for {
      _ <- M.start(node)
      update = world.copy(pending = Map(node -> world.time))
    } yield update

    case Stale(nodes) => nodes.foldLeftM(world) { (world, n) =>
      for {
        _ <- M.stop(n)
        update <- world.copy(pending = world.pending + (n -> world.time))
      } yield update
    }

    case _ => world.pure[F]

  }
}