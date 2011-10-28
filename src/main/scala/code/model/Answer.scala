package code.model

import net.liftweb.mapper._
import net.liftweb.common.Full
import scala.xml.Node

object Answer extends Answer with LongKeyedMetaMapper[Answer] {

  def fromXml(node: Node, questionnaire: Questionnaire) = {
    val answer = Answer.create

    val question = Question.find(By(Question.id, (node \ "@forQuestion").text.toLong),
      By(Question.questionnaire, questionnaire)).open_!
      
    answer.question(question)
    answer.text(node.text)

    answer
  }

}

class Answer extends LongKeyedMapper[Answer] with IdPK {
  def getSingleton = Answer

  object question extends MappedLongForeignKey(this, Question) {
    override def validSelectValues = Full(Question.findAll map (question => (question.id.is, question.text.is)))
  }

  object text extends MappedText(this)
}
