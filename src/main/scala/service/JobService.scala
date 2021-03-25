package service

import helper.OperatorHelper.operatorMap
import model.{ClassifiedJobs, Job, JobGroup, Publisher, Rule}
import repository.JobRepository

import scala.reflect.runtime.{universe => ru}

// TODO: What these two imports for?
import scala.concurrent._
import ExecutionContext.Implicits.global

case class JobService() {

  private val jobRepository = JobRepository()

  def classifyJobs(jobs:List[Job]):Future[List[ClassifiedJobs]] = {
    Future.sequence(jobs.map(job => classifyJob(job).map(jobGroup => ClassifiedJobs(job, jobGroup.id,getPublishersForJob(job,jobGroup)))))
  }
  def classifyJob(job:Job):Future[JobGroup]= {
    for {
      allGroups <- jobRepository.getAllJobGroups()
//      TODO: How to throw exception if no match
      jobGroup = allGroups.find(jobGrp=>isJobGroupAMatch(job,jobGrp)).get
    } yield jobGroup
  }
  def getPublishersForJob(job:Job,jobGroup:JobGroup):List[String] = {
    val allPublisherIds = jobGroup.sponsoredPublishers.map(pub=>pub.id)
    if(allPublisherIds.isEmpty)
      return List()
    val rand = new scala.util.Random
    scala.util.Random.shuffle(allPublisherIds).take(1+rand.nextInt(allPublisherIds.size))
  }
  def isJobGroupAMatch(job : Job, jobGroup : JobGroup):Boolean = {
    val allRules = jobGroup.rules
    var doRulesMatch = true
    allRules.foreach(rule=>doRulesMatch=doRulesMatch&&isRuleAMatch(job,rule))
    doRulesMatch
  }
  def isRuleAMatch(job : Job, rule : Rule):Boolean = {
    val fieldName = rule.field
    val fieldSymbol = ru.typeOf[Job].decl(ru.TermName(fieldName)).asTerm
    val jobMirror = ru.runtimeMirror(job.getClass.getClassLoader)
    val jobFieldVal = jobMirror.reflect(job).reflectField(fieldSymbol).get
    operate(jobFieldVal.toString,rule.operator,rule.value)
  }

  def operate(jobFieldVal:String,operator:String,ruleFieldVal:String):Boolean ={
    operatorMap.get(operator).get(jobFieldVal,ruleFieldVal)
  }
}
