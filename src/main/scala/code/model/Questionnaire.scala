package code.model

import net.liftweb.mapper._
import scala.xml.Node
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.common.Failure
import scala.collection.mutable.ListBuffer

object Questionnaire extends Questionnaire with LongKeyedMetaMapper[Questionnaire] {

  def saveAnswers(input: Node) = {
    val questionnaire = Questionnaire.find(By(Questionnaire.id, (input \ "@forQuestionnaire").text.toLong)).open_!

    input \ "answer" foreach {
      node => Answer.fromXml(node, questionnaire).save
    }
  }

  def createDefaultQuestionnaire {
    val questionnaire = Questionnaire.create
    questionnaire.save

    createQuestion(questionnaire, "text", "What's your name?")
    createQuestion(questionnaire, "location", "Where are you now?")

    val q3 = createQuestion(questionnaire, "choice", "What programming language do you prefer?")
    createChoice(q3, "C++")
    createChoice(q3, "Scala")
    createChoice(q3, "Java")

    val q4 = createQuestion(questionnaire, "multichoice", "What programming languages are you interested in?")
    createChoice(q4, "C++")
    createChoice(q4, "Scala")
    createChoice(q4, "Java")

    //createQuestion(questionnaire, "attachment", "Attachments")
  }

  private def createQuestion(questionnaire: Questionnaire, answerType: String, text: String) = {
    val q = Question.create
    q.questionnaire(questionnaire)
    q.answerType(answerType)
    q.text(text)
    q.save

    q
  }

  private def createChoice(question: Question, text: String) {
    val c = Choice.create
    c.question(question)
    c.text(text)
    c.save
  }
}

class Questionnaire extends LongKeyedMapper[Questionnaire] with IdPK with OneToMany[Long, Questionnaire] {
  def getSingleton = Questionnaire

  object questions extends MappedOneToMany(Question, Question.questionnaire)

  def markup =
    <questionnaire id={ id.is.toString }>
      {
        questions map {
          question: Question =>
            {
              <question id={ question.id.is.toString } type={ question.answerType.is }>
                <text>{ question.text.is }</text>
                {
                  if (!question.choices.isEmpty) {
                    <choices>
                      {
                        question.choices map {
                          choice: Choice =>
                            {
                              <choice id={ choice.id.is.toString }>{ choice.text.is }</choice>
                            }
                        }
                      }
                    </choices>
                  }
                }
              </question>
            }
        }
      }
    </questionnaire>
}
