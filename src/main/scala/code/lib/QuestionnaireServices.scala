package code.lib

import net.liftweb.http._
import net.liftweb.http.rest._

object QuestionnaireServices extends RestHelper {
  serve {
    case "questionnaire" :: "get" :: _ XmlGet _ =>
      <questionnaire id="1">
        <question id="1" type="text">What's your name?</question>
        <question id="2" type="location">Where are you now?</question>
        <question id="3" type="choice">
          What language do you prefer?
          <choice id="cpp">C++</choice>
          <choice id="java">Java</choice>
          <choice id="scala">Scala</choice>
        </question>
        <question id="4" type="multichoice">
          What languages are you interested in?
          <choice id="cpp">C++</choice>
          <choice id="java">Java</choice>
          <choice id="scala">Scala</choice>
        </question>
        <question id="5" type="attachment">Attachments</question>
      </questionnaire>
      
    case "questionnaire" :: "statistics" :: id :: _ XmlGet _ =>
      <statistics for={id} />

    case "questionnaire" :: "verify" :: _ XmlPut input -> _ =>
      println("Got input: " + input)
      <validation status="success" />
      
    case "questionnaire" :: "put" :: _ XmlPut input -> _ =>
      <success />
  }
}