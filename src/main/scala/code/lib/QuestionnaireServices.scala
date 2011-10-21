package code.lib

import net.liftweb.http._
import net.liftweb.http.rest._

object QuestionnaireServices extends RestHelper {
  serve {
    case "questionnaire" :: "get" :: _ XmlGet _ =>
      <questionnaire>
        <question id="1" type="text">What's your name?</question>
        <question id="2" type="location">Where are you now?</question>
        <question id="4" type="attachment">Attachments</question>
      </questionnaire>
      
    case "questionnaire" :: "verify" :: _ XmlPut input -> _ =>
      println("Got input: " + input)
      <answer>
        {input}
        </answer>
  }
}