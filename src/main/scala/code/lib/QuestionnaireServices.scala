package code.lib

import net.liftweb.http._
import net.liftweb.http.rest._

object QuestionnaireServices extends RestHelper {
  serve {
    case Req("questionnaire" :: _, "xml", GetRequest) => <questionnaire></questionnaire>
  }
}