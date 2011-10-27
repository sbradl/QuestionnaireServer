package code.model

import net.liftweb.mapper._
import net.liftweb.common.Full

object Question extends Question with LongKeyedMetaMapper[Question] {
}

class Question extends LongKeyedMapper[Question] with IdPK with OneToMany[Long, Question] {
  def getSingleton = Question
  
  object questionnaire extends MappedLongForeignKey(this, Questionnaire) {
      override def validSelectValues = Full(Questionnaire.findAll map(questionnaire => (questionnaire.id.is, questionnaire.id.is.toString)))
  }
  
  object answerType extends MappedString(this, 16)
  
  object text extends MappedText(this)
  
  object choices extends MappedOneToMany(Choice, Choice.question)
} 
