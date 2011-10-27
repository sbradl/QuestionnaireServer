package code.lib

import net.liftweb.http._
import net.liftweb.http.rest._
import code.model.Questionnaire
import net.liftweb.common.Box

object QuestionnaireServices extends RestHelper {
  serve {
    case "questionnaire" :: "get" :: _ XmlGet _ => Questionnaire.findAll.head.markup

    case "questionnaire" :: "statistics" :: id :: _ XmlGet _ =>
      <statistics for={ id }/>

    case "questionnaire" :: "verify" :: _ XmlPut input -> _ =>
      println("Input: " + input)
      val messages = Questionnaire.validate(input)

      val status = messages match {
        case List() => "success"
        case _ => "failed"
      }

      <validation status={ status }>
        {
          messages map {
            msg => <message>{msg}</message>
          }
        }
      </validation>

    case "questionnaire" :: "put" :: _ XmlPut input -> _ =>
      <failed/>
  }
}
