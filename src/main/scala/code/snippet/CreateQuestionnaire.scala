package code.snippet

import net.liftweb.http.LiftScreen
import code.model.Questionnaire
import net.liftweb.http.S

class CreateQuestionnaire extends LiftScreen {

  object questionnaire extends ScreenVar(Questionnaire.create)
  
  addFields(() => questionnaire)
  
  def finish {
    questionnaire.save
    
    S.notice(S ? "QUESTIONNAIRE_CREATED")
  }
  
}