package code.model

import net.liftweb.mapper._
import net.liftweb.common.Full
import net.liftweb.http.S

object Question extends Question with LongKeyedMetaMapper[Question] {
}

class Question extends LongKeyedMapper[Question] with IdPK with OneToMany[Long, Question] {
  def getSingleton = Question

  object questionnaire extends MappedLongForeignKey(this, Questionnaire) {
    override def displayName = S ? "QUESTIONNAIRE"
    override def validSelectValues = Full(Questionnaire.findAll map (questionnaire => (questionnaire.id.is, questionnaire.title.is)))
  }

  object answerType extends MappedString(this, 16) {
    override def displayName = S ? "ANSWER_TYPE"
  }

  object isRequired extends MappedBoolean(this) {
    override def defaultValue = true
    override def displayName = S ? "IS_REQUIRED"
  }

  object text extends MappedString(this, 240) {
    override def displayName = S ? "TEXT"
    override def validations = List(valMinLen(1, S ? "NAME_TOO_SHORT"))
  }

  object choices extends MappedOneToMany(Choice, Choice.question)
} 
