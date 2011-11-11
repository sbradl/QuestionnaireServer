package code.lib

import code.model._
import net.liftweb.mapper._
import scala.xml.Node
import scala.collection.mutable.ListBuffer
import net.liftweb.common.Full

object InputValidator {
  def apply(input: Node) = {
    val validator = new InputValidator
    validator.validate(input)
  }
}

class InputValidator {

  val messages = ListBuffer[(String, String, String)]()
  var currentAnswer: String = null
  var currentQuestion: Question = null

  def validate(input: Node): List[(String, String, String)] = Questionnaire.find(By(Questionnaire.id, (input \ "@forQuestionnaire").text.toLong)) match {
    case Full(questionnaire) => {
      validate(input, questionnaire)
      messages toList
    }
    case _ => List(("INVALID_ID", "", ""))
  }

  private def validate(input: Node, questionnaire: Questionnaire) {
    input \ "answer" foreach {
      node => validateAnswer(questionnaire, node)
    }
  }

  private def validateAnswer(questionnaire: Questionnaire, answerNode: Node) {
    val questionID = (answerNode \ "@forQuestion").text.toLong
    val questionBox = Question.find(By(Question.id, questionID), By(Question.questionnaire, questionnaire))

    currentAnswer = answerNode.text

    questionBox match {
      case Full(question) => {
        currentQuestion = question

        question.answerType.is match {
          case "text" => validateText
          case "location" => validateLocation
          case "choice" => validateSingleChoice
          case "multichoice" => validateMultiChoice
          case "attachment" =>
          case _ =>
        }
      }
      case _ => {
        messages += (("INVALID_QUESTION_ID", questionID.toString, ""))
      }
    }
  }

  private def validateText {
    if (currentQuestion.isRequired.is) {
      if (currentAnswer.trim().isEmpty()) {
        messages += (("INVALID_TEXT", currentAnswer, ""))
      }
    }
  }

  private def validateLocation {
    if (currentQuestion.isRequired.is) {
      val parts = currentAnswer.split(",")

      try {
        val lat = parts(0).toDouble
        val lng = parts(1).toDouble
      } catch {
        case _ => messages += (("INVALID_LOCATION", currentAnswer, ""))
      }
    }
  }

  private def validateSingleChoice {
    try {
      if (!currentAnswer.trim().isEmpty() && currentQuestion.isRequired.is) {
        validateChoice(currentAnswer.toLong)
      }
    } catch {
      case _ => messages += (("INVALID_CHOICE", currentAnswer, ""))
    }
  }

  private def validateMultiChoice {
    try {
      if (!currentAnswer.trim().isEmpty() && currentQuestion.isRequired.is) {
        val choiceIDs = currentAnswer.split(",") map (_.toLong)

        choiceIDs foreach {
          choiceID: Long => validateChoice(choiceID)
        }
      }
    } catch {
      case _ => messages += (("INVALID_CHOICE", currentAnswer, ""))
    }
  }

  private def validateChoice(choiceID: Long) {
    currentQuestion.choices.exists {
      choice: Choice => choice.id.is == choiceID
    } match {
      case true =>
      case false => messages += (("INVALID_CHOICE", currentAnswer, currentQuestion.choices.map(_.id.is).mkString(",")))
    }
  }
}
