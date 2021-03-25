import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import controller.JobController.{jobGroupRoute, jobRoute, publisherRoute, ruleRoute}

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.Duration

object Server extends App {
  implicit val system: ActorSystem = ActorSystem("web-app")
  private implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  private implicit val materialize: ActorMaterializer = ActorMaterializer()
  print("server started")
//  TODO: Q : Why lazy?
  lazy val apiRoutes: Route = pathPrefix("api") {
    Directives.concat(
      jobGroupRoute,
      jobRoute,
      publisherRoute,
      ruleRoute
    )
  }

  Http().bindAndHandle(apiRoutes, "localhost", 8080)
  Await.result(system.whenTerminated, Duration.Inf)
}