package code.snippet

import net.liftweb.http.LiftScreen
import code.model.Question
import net.liftweb.http.S
import code.model.Choice

class AddQuestion extends LiftScreen {
  
  object question extends ScreenVar(Question.create)
  
  val questionnaire = field(question.questionnaire)
  val answerType = select(S ? "ANSWER_TYPE", "text", Seq("text", "location", "attachment", "choice", "multichoice"))
  val required = field(question.isRequired)
  val text = field(question.text)
  
  val choices = field(S ? "CHOICES", "")
  
  def finish {
    println(questionnaire.is)
    
    question.questionnaire(questionnaire.is)
    question.answerType(answerType.is)
    question.isRequired(required.is)
    question.text(text.is)
    
    choices.split(",") foreach {
      choiceText => {
        val choice = Choice.create
        choice.question(question.id)
        choice.text(choiceText)
        question.choices += choice
      }
    }
    
    question.save
    
    S.notice(S ? "QUESTION_ADDED")
  }
  
}