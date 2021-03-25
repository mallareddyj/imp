package helper

object OperatorHelper {

  val inFunction : (String,String)=>Boolean = (jobFieldVal: String, ruleFieldVal: String) => {
    jobFieldVal.toLowerCase() contains ruleFieldVal.toLowerCase()
  }

  val startsWith: (String,String)=>Boolean =(jobFieldVal: String, ruleFieldVal: String)=>{
    jobFieldVal.startsWith(ruleFieldVal)
  }

  val endsWith: (String,String)=>Boolean =(jobFieldVal: String, ruleFieldVal: String)=> {
    jobFieldVal.endsWith(ruleFieldVal)
  }
  val operatorMap : Map[String,(String,String)=>Boolean] = Map(
    "substring" -> inFunction,
    "startsWith" -> startsWith,
    "endsWith" -> endsWith
  )
}
