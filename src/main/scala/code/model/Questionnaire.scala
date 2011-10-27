package code.model

import net.liftweb.mapper._

object Questionnaire extends Questionnaire with LongKeyedMetaMapper[Questionnaire] {

  def createDefaultQuestionnaire {
    val questionnaire = Questionnaire.create
    questionnaire.save

    val q1 = Question.create
    q1.questionnaire(questionnaire)
    q1.answerType("text")
    q1.text("What's your name?")
    q1.save

    val q2 = Question.create
    q2.questionnaire(questionnaire)
    q2.answerType("location")
    q2.text("Where are you now?")
    q2.save

    val q3 = Question.create
    q3.questionnaire(questionnaire)
    q3.answerType("choice")
    q3.text("What programming language do you prefer?")
    q3.save

    val c1 = Choice.create
    c1.question(q3)
    c1.text("C++")
    c1.save

    val c2 = Choice.create
    c2.question(q3)
    c2.text("Scala")
    c2.save

    val c3 = Choice.create
    c3.question(q3)
    c3.text("Java")
    c3.save

    val q4 = Question.create
    q4.questionnaire(questionnaire)
    q4.answerType("multichoice")
    q4.text("What programming languages are you interested in?")
    q4.save

    val c4 = Choice.create
    c4.question(q4)
    c4.text("C++")
    c4.save

    val c5 = Choice.create
    c5.question(q4)
    c5.text("Scala")
    c5.save

    val c6 = Choice.create
    c6.question(q4)
    c6.text("Java")
    c6.save

    val q5 = Question.create
    q5.questionnaire(questionnaire)
    q5.answerType("attachment")
    q5.text("Attachments")
    q5.save
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
              <question id={ question.id.is.toString } type={question.answerType.is}>
              {question.text.is}
              {
                if(!question.choices.isEmpty) {
                  question.choices map {
                    choice: Choice => {
                      <choice id={choice.id.is.toString}>{choice.text.is}</choice>
                    }
                  }
                }
              }
              </question>
            }
        }
      }
    </questionnaire>
}
