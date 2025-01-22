package repository

import java.util.UUID

import anorm._
import javax.inject.Inject
import play.api.db.Database
import models._

import scala.concurrent.{ExecutionContext, Future}

class StudentRepositoryImpl @Inject()(db: Database,
                                    databaseExecutionContext: ExecutionContext,
                                    studentRepositoryDAO: StudentRepositoryDAO)
  extends StudentRepository {

  val parser: RowParser[Student] = Macro.namedParser[Student]

  def getStudents(): Future[List[Student]] = {
    Future {
      db.withConnection { implicit conn =>
        studentRepositoryDAO.get()
      }
    }(databaseExecutionContext)
  }

  def getStudentById(id: UUID): Future[Option[Student]] = {
    Future {
      db.withConnection { implicit conn =>
        studentRepositoryDAO.getBy(id)
      }
    }(databaseExecutionContext)
  }

  def createStudent(student: Student): Future[Either[APIError, Student]] = {
    Future {
      db.withConnection { implicit conn =>
        studentRepositoryDAO.insert(student)
      }
    }(databaseExecutionContext)
  }

  def updateStudentById(id: UUID, student: Student): Future[Int] = {
    Future {
      db.withConnection { implicit conn =>
        studentRepositoryDAO.update(id, student)
      }
    }(databaseExecutionContext)
  }

  def deleteStudentById(id: UUID): Future[Int] = {
    Future {
      db.withConnection { implicit conn =>
        studentRepositoryDAO.delete(id)
      }
    }(databaseExecutionContext)
  }

}
