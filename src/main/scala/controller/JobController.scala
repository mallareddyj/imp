package controller


import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, pathPrefix, post}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import model.{JobGroupReq, _}
import repository.JobRepository
import service.JobService
import spray.json.DefaultJsonProtocol
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}




object JobGroupReqJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
//  TODO : Q : Is there a better way instead of jsonFormatN
  implicit val jobGroupReqFormat = jsonFormat3(JobGroupReq)
  implicit val publisherFormat = jsonFormat4(Publisher)
  implicit val jobFormat = jsonFormat6(Job)
  implicit val ruleFormat = jsonFormat4(Rule)
  implicit val jobGrp = jsonFormat3(JobGroup)
  implicit val classifiedJobsFormat = jsonFormat3(ClassifiedJobs)
}
object JobController {
  import JobGroupReqJsonProtocol._
  implicit val system: ActorSystem = ActorSystem("web-app")
  private implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  private implicit val materialize: ActorMaterializer = ActorMaterializer()

  val jobRepository = JobRepository()
  val jobService = JobService()

  val createJobGroup = post {
    entity(as[JobGroupReq]) {
      req => onComplete(jobRepository.saveJobGroup(req)) {
//        println("incoming req = "+req)
        _ match {
          case Success(_) => complete(StatusCodes.OK,"job group saved")
          case Failure(e) => throw e
        }
      }
    }
  }

  val createRule = post {
    entity(as[Rule]) {
      req => onComplete(jobRepository.saveRule(req)) {
        _ match {
          case Success(_) => complete(StatusCodes.OK,"rule saved")
          case Failure(e) => throw e
        }
      }
    }
  }

  val createPublisher = post {
    entity(as[Publisher]) {
      req => onComplete(jobRepository.savePublishers(req)) {
        _ match {
          case Success(_) => complete(StatusCodes.OK,"publisher saved")
          case Failure(e) => throw e
        }
      }
    }
  }

  val classifyJobs = pathPrefix("classify"){
    get {
      entity(as[List[Job]]) {
        req => {
          onComplete(jobService.classifyJobs(req)) {
            _ match {
              case Success(res) => complete(StatusCodes.OK, res)
              case Failure(e) => {
                println(e.getMessage)
                throw e
              }
            }
          }
        }
      }
    }
  }
  val jobGroupRoute: Route =
    pathPrefix("jobgroup") {
      Directives.concat(
        createJobGroup
      )
  }
  val ruleRoute: Route =
    pathPrefix("rule") {
      Directives.concat(
        createRule
      )
    }
  val publisherRoute: Route =
    pathPrefix("publisher") {
      Directives.concat(
        createPublisher
      )
    }
  val jobRoute: Route =
    pathPrefix("job") {
      Directives.concat(
        classifyJobs
      )
    }
}