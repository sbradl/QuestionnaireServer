package code.model

import net.liftweb.mapper._
import net.liftweb.common.Full

object Choice extends Choice with LongKeyedMetaMapper[Choice] {
}

class Choice extends LongKeyedMapper[Choice] with IdPK {
  def getSingleton = Choice
  
  object question extends MappedLongForeignKey(this, Question) {
      override def validSelectValues = Full(Question.findAll map(question => (question.id.is, question.text.is)))
  }
  
  object text extends MappedText(this)
} 
