package serviceTest

import model.{Job, JobGroup, Rule}
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import service.JobService

import scala.Option.when

class JobServiceTests extends FunSuite with BeforeAndAfter with MockitoSugar {

//  test ("positive test for matching jobGroup") {
//
//    // (1) init
//    val jobService = mock[JobService]
//
//    // (2) setup: when someone logs in as "johndoe", the service should work;
//    //            when they try to log in as "joehacker", it should fail.
//    when(jobService.login("johndoe", "secret")).thenReturn(Some(User("johndoe")))
//    when(jobService.login("joehacker", "secret")).thenReturn(None)
//
//    // (3) access the service
//    val johndoe = jobService.login("johndoe", "secret")
//    val joehacker = jobService.login("joehacker", "secret")
//
//    // (4) verify the results
//    assert(johndoe.get == User("johndoe"))
//    assert(joehacker == None)
//
//  }
  test ("positive test for matching jobGroup") {

    // (1) init
    val jobService = JobService()
    val mockJob = Job(jobId="1",category = "IT",title="Software Developer",minExperience = 3,List("Hiring"),valid = true)
    val ruleList = List(Rule(id=1,field = "title",operator = "substring",value="Software"),Rule(id=2,field = "title",operator = "substring",value="Developer"))
    val mockJobGroup = JobGroup(id="1",rules = ruleList,List.empty)
    // (2) assertion
    assert(
      jobService.isJobGroupAMatch(mockJob,mockJobGroup)===true
    )
  }

//  test ("test to check if job classified correctly") {
//
//    // (1) init
//    val jobService = JobService()
//    val mockJob
//    val mockJob = Job(jobId="1",category = "IT",title="Software Developer",minExperience = 3,List("Hiring"),valid = true)
//    val ruleList = List(Rule(id=1,field = "title",operator = "substring",value="Software"),Rule(id=2,field = "title",operator = "substring",value="Developer"))
//    val mockJobGroup = JobGroup(id="1",rules = ruleList,List.empty)
//    // (2) assertion
//    assert(
//      jobService.isJobGroupAMatch(mockJob,mockJobGroup)===true
//    )
//  }
}