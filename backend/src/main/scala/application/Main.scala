package application

import java.util.UUID

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.concurrent.Queue
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text

import scala.collection.concurrent.TrieMap

object EventsToClient {
  trait EventToClient
  case class UpdateSumResponse(v: Int) extends EventToClient

  import io.circe.syntax._
  import io.circe._

  implicit val enc: Encoder.AsObject[EventToClient] = {
    implicit val updateSumReqEnc: io.circe.Encoder.AsObject[UpdateSumResponse] = io.circe.derivation.deriveEncoder[UpdateSumResponse]

    Encoder.AsObject.instance {
      case u : UpdateSumResponse => u.asJsonObject.add("$type", "UpdateSumResponse".asJson)
    }
  }

  implicit val dec: Decoder[EventToClient] = {
    implicit val updateSumReqDec: io.circe.Decoder[UpdateSumResponse] = io.circe.derivation.deriveDecoder[UpdateSumResponse]

    for {
      visitorType <- Decoder[String].prepare(_.downField("$type"))
      value <- visitorType match {
        case "UpdateSumResponse" => Decoder[UpdateSumResponse]
        case other => Decoder.failedWithMessage(s"invalid type: $other")
      }
    } yield value
  }
}

object EventsFromClient {
  trait EventFromClient
  case class UpdateSumRequest(fields: Seq[Int]) extends EventFromClient

  import io.circe.syntax._
  import io.circe._

  implicit val enc: Encoder.AsObject[EventFromClient] = {
    implicit val updateSumReqEnc: io.circe.Encoder.AsObject[UpdateSumRequest] = io.circe.derivation.deriveEncoder[UpdateSumRequest]

    Encoder.AsObject.instance {
      case u : UpdateSumRequest => u.asJsonObject.add("$type", "UpdateSumRequest".asJson)
    }
  }

  implicit val dec: Decoder[EventFromClient] = {
    implicit val updateSumReqDec: io.circe.Decoder[UpdateSumRequest] = io.circe.derivation.deriveDecoder[UpdateSumRequest]

    for {
      visitorType <- Decoder[String].prepare(_.downField("$type"))
      value <- visitorType match {
        case "UpdateSumRequest" => Decoder[UpdateSumRequest]
        case other => Decoder.failedWithMessage(s"invalid type: $other")
      }
    } yield value
  }
}

object BusinessLogic {
  def doSomethingWithEvent(sessionId: String, event: EventsFromClient.EventFromClient): fs2.Stream[IO, Unit] = {
    import io.circe.syntax._
    val eventToClient: EventsToClient.EventToClient = event match {
      case EventsFromClient.UpdateSumRequest(v) => EventsToClient.UpdateSumResponse(v.sum)
    }

    val element: Text = Text(eventToClient.asJson.noSpaces)

    val response: IO[Unit] = Main.toClient.get(sessionId).map{ q =>
      q.enqueue1(element)
    }.getOrElse(IO.unit)

    fs2.Stream.eval(response)
  }

  def wsHandler(sessionId: String)(fromClient: fs2.Stream[IO, WebSocketFrame]): fs2.Stream[IO, Unit] = fromClient.collect {
    case WebSocketFrame.Text(text, _) => io.circe.parser.decode[EventsFromClient.EventFromClient](text) match {
      case Left(error) => {
        println(s"Error ${error}")
        fs2.Stream[IO, Unit]()
      }
      case Right(event) => doSomethingWithEvent(sessionId, event)
    }
    case x@_ => {
      println(s"got $x")
      fs2.Stream[IO, Unit]()
    }
  }.flatten
}

object Main extends IOApp {
  val toClient: TrieMap[String, Queue[IO, WebSocketFrame]] = TrieMap.empty[String, Queue[IO, WebSocketFrame]]

  val baseRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "healthz" => Ok()
    case GET -> Root / "ws" => {
      val qIO: IO[Queue[IO, WebSocketFrame]] =  Queue
        .unbounded[IO, WebSocketFrame]

      val sessionId = UUID.randomUUID().toString

      qIO.flatMap{ q =>
        toClient.update(sessionId, q)

        val preparedHandler: fs2.Stream[IO, WebSocketFrame] => fs2.Stream[IO, Unit] =
          BusinessLogic.wsHandler(sessionId)

        WebSocketBuilder[IO]
          .build(q.dequeue, preparedHandler)
      }
    }
  }

  val appRoutes = Router(
    "/" -> baseRoutes
  ).orNotFound

  val server: IO[ExitCode] = BlazeServerBuilder[IO]
    .bindHttp(8080, "0.0.0.0")
    .withHttpApp(appRoutes)
    .serve
    .compile
    .drain
    .as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] = server
}
