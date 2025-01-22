package models

import java.util.UUID
import play.api.libs.json.{Json, OFormat}

case class Student(
                    id: UUID,
                    name: String,
                    lastName:String,
                    firstName:String,
                    groupName:String,
                    scoreAvg:Double)

object Student{
  implicit val StudentFormat: OFormat[Student] = Json.format[Student]
}