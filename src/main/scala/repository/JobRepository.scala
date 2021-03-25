package repository


import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import model.{JobGroup, JobGroupReq, Publisher, Rule}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase}
//import repository.JobRepository.publishers

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContextExecutor, Future}


case class JobRepository() {
  implicit val system: ActorSystem = ActorSystem("web-app")
  private implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  private implicit val materialize: ActorMaterializer = ActorMaterializer()
  import org.mongodb.scala.bson.codecs.Macros._

  private val customCodecs = fromProviders(classOf[JobGroupReq],classOf[Publisher],classOf[Rule],classOf[JobGroup])

  def saveJobGroup(jobGrpReq: JobGroupReq): Future[Completed] = {
// completedOp
    val completedOperation = for {
      publishers<-getPublisherByIds(jobGrpReq.sponsoredPublishers)
      rules<-getRulesByIds(jobGrpReq.rules)
      jobGroup = JobGroup(id=jobGrpReq.id,rules=rules,sponsoredPublishers = publishers)
    } yield jobGroups.insertOne(jobGroup).toFuture()
    completedOperation.flatten
  }

  def savePublishers(publisher: Publisher): Future[Completed] = publishers.insertOne(publisher).toFuture()

  def saveRule(rule: Rule): Future[Completed] = rules.insertOne(rule).toFuture()

  def getAllJobGroups():Future[Seq[JobGroup]]={
    jobGroups.find().toFuture()
  }
  private def getRulesByIds(publisherIds: List[Int]): Future[List[Rule]] = Future.sequence(publisherIds.map(pubId=>getRuleById(pubId)))

  private def getRuleById(id:Int): Future[Rule] = {
    rules.find(equal("id",id)).first().toFuture()
  }

  private def getPublisherByIds(ruleIds: List[Int]): Future[List[Publisher]] = Future.sequence(ruleIds.map(ruleId=>getPublisherById(ruleId)))

  private def getPublisherById(id:Int): Future[Publisher] = {
    publishers.find(equal("id",id.toString)).first().toFuture()
  }
  private val codecRegistry = fromRegistries(customCodecs, DEFAULT_CODEC_REGISTRY)
  // TODO : Q: Is MongoClient by default connected to localhost:27017 as we havent configured it anywhere
  private val database: MongoDatabase = MongoClient().getDatabase("JobData")
    .withCodecRegistry(codecRegistry)
  private val publishers: MongoCollection[Publisher] = database.getCollection("Publishers")
  private val jobGroups: MongoCollection[JobGroup] = database.getCollection("JobGroups")
  private val rules: MongoCollection[Rule] = database.getCollection("Rules")
}
