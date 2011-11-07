package code.lib

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.util.BasicTypesHelpers._
import code.model.Questionnaire
import net.liftweb.common.Box
import net.liftweb.mapper.By
import net.liftweb.common.Full

object QuestionnaireServices extends RestHelper {
  serve {
    case "questionnaire" :: "list" :: _ XmlGet _ =>
      <questionnaires>
        {
            Questionnaire.findAll map {
              questionnaire => <questionnaire id={questionnaire.id.toString} />
            }
        }
      </questionnaires>

    case "questionnaire" :: "get" :: AsLong(id) :: _ XmlGet _ => Questionnaire.find(By(Questionnaire.id, id)) match {
      case Full(questionnaire) => questionnaire.markup
      case _ => <failed reason="INVALID_ID"/>
    }

    case "questionnaire" :: "statistics" :: AsLong(id) :: _ XmlGet _ =>
      Questionnaire.find(By(Questionnaire.id, id)) match {
        case Full(questionnaire) => {
          val stats = Statistics(questionnaire)
          <statistics for={ id toString }>
            {
              stats.getData map {
                record =>
                  {
                    <question text={ record._1.text } answerType={ record._1.answerType }>
                      {
                        record._2 map {
                          x => <answer votes={ x._3 toString }>{ x._2 }</answer>
                        }
                      }
                    </question>
                  }
              }
            }
          </statistics>
        }
        case _ => <failed reason="INVALID_ID"/>
      }

    case "questionnaire" :: "verify" :: _ XmlPut input -> _ =>
      println("Input: " + input)
      val messages = InputValidator(input)

      val status = messages match {
        case List() => "success"
        case _ => "failed"
      }

      <validation status={ status }>
        {
          messages map {
            msg =>
              msg match {
                case (s: String, "", "") => <message error={ s }/>
                case (s: String, in: String, "") => <message error={ s }>
                                                      <input>{ in }</input>
                                                    </message>
                case (s: String, in: String, e: String) => <message error={ s }>
                                                             <input>{ in }</input>
                                                             <expected>{ e }</expected>
                                                           </message>
              }
          }
        }
      </validation>

    case "questionnaire" :: "put" :: _ XmlPut input -> _ =>
      InputValidator(input).isEmpty match {
        case true => {
          try {
            Questionnaire.saveAnswers(input)
            <success/>
          } catch {
            case _ => <failed reason="SAVE_FAILED"/>
          }
        }

        case false => {
          <failed reason="VALIDATION_FAILED"/>
        }
      }
  }
}
