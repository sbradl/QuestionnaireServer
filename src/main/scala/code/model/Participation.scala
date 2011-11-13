package code.model

import net.liftweb.mapper._

object Participation extends Participation with LongKeyedMetaMapper[Participation]

class Participation extends LongKeyedMapper[Participation] with IdPK {

  def getSingleton = Participation
  
}