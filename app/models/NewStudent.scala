package models

import play.api.libs.json.{Json, OFormat}

case class NewStudent(name: String,
                      lastName: String,
                      firstName:String,
                      groupName:String,
                      scoreAvg:Double
                     )

object NewStudent {
  implicit val NewStudentFormat: OFormat[NewStudent] = Json.format[NewStudent]
}
