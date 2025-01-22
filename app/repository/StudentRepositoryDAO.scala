package repository
import java.sql.Connection
import java.util.UUID

import anorm.{Macro, RowParser, SQL}
import javax.inject.Inject
import org.postgresql.util.PSQLException
import play.api.Logging
import play.api.db.Database
import models._

class StudentRepositoryDAO @Inject()(db: Database) extends Logging {
  val parser: RowParser[Student] = Macro.namedParser[Student]

  def get()(implicit conn: Connection): List[Student] = {
    SQL("SELECT * FROM students").as(parser.*)
  }

  def getBy(id: UUID)(implicit conn: Connection): Option[Student] = {
    SQL("SELECT id, name, lastName, firstName, groupName, scoreAvg FROM Students WHERE id = {id}::uuid")
      .on("id" -> id)
      .as(parser.singleOpt)
  }

  def insert(student: Student)(implicit conn: Connection): Either[APIError, Student] = {
    try {
      SQL("INSERT INTO Students (id, name, lastName, firstName, groupName, scoreAvg) VALUES ({id}::uuid, {name}, {lastName}, {firstName}, {groupName}, {scoreAvg})")
        .on("id" -> student.id,
          "name" -> student.name,
          "lastName" -> student.lastName,
          "firstName" -> student.firstName,
          "groupName" -> student.groupName,
          "scoreAvg" -> student.scoreAvg)
        .execute()
      Right(student)
    } catch {
      case e: PSQLException => {
        logger.error(e.getMessage())
        Left(APIError("Ошибка создании"))
      }
      case e: Exception => {
        logger.error(e.getMessage())
        Left(APIError("Ошибка"))
      }
    }
  }

  def update(id: UUID, student: Student)(implicit conn: Connection): Int = {
    SQL("UPDATE students SET name = {name}, lastName = {lastName}, firstName = {firstName}, groupName = {groupName}, scoreAvg = {scoreAvg},  WHERE id = {id}::uuid")
      .on("id" -> student.id,
        "name" -> student.name,
        "lastName" -> student.lastName,
        "firstName" -> student.firstName,
        "groupName" -> student.groupName,
        "scoreAvg" -> student.scoreAvg)
      .executeUpdate()
  }

  def delete(id: UUID)(implicit conn: Connection): Int = {
    SQL("DELETE FROM students WHERE id = {id}::uuid").on("id" -> id).executeUpdate()
  }
}
