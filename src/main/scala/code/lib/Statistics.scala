package code.lib

import code.model._
import net.liftweb.mapper._
import scala.collection.mutable.ListBuffer

object Statistics {

  def apply(questionnaire: Questionnaire) = {
    val stats = new Statistics(questionnaire)

    stats.fetch

    stats
  }

}

class Statistics(questionnaire: Questionnaire) {

  private val stats = ListBuffer[(String, String, Int)]()
  
  def getData = stats.toList groupBy (_._1)

  def fetch {
    val questions = Question.findAll(By(Question.questionnaire, questionnaire))
    
    val data = questions map {
      question => {
        val answers = Answer.findAll(By(Answer.question, question))
        
        val answersByText = answers groupBy (_.text)
        (question, answersByText)
      }
    }
    
    data foreach {
      qta => {
        val question = qta._1
        
        qta._2 foreach {
          ta => {
            val text = ta._1
            val answers = ta._2.size
            
            question.answerType.is match {
              
              case "choice" => {
                val choice = Choice.find(By(Choice.id, text.is.toLong)).open_!
                stats += ((question.text, choice.text.is, answers))
              }
              
              case "multichoice" => {
                val parts = text.split(",")
                
                parts foreach {
                  part => {
                    val choice = Choice.find(By(Choice.id, part.toLong)).open_!
                    stats += ((question.text, choice.text.is, answers))
                  }
                }
                
              }
              case _ => {
                stats += ((question.text, text, answers))
              }
            }
          }
        }
      }
    }
  }

}
