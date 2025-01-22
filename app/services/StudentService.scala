package services

import repository.StudentRepositoryImpl

import java.util.UUID
import models._

import javax.inject.Inject
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

class StudentService @Inject()(db: Database,
                               databaseExecutionContext: ExecutionContext,
                               studentRepository: StudentRepositoryImpl) {

  def getStudents(): Future[List[Student]] = {
    studentRepository.getStudents()
  }

  def getStudentById(id: UUID): Future[Option[Student]] = {
    studentRepository.getStudentById(id)
  }

  def createStudent(student: Student): Future[Either[APIError, Student]] = {
    studentRepository.createStudent(student)
  }

  def deleteStudentById(id: UUID): Future[Int] = {
    studentRepository.deleteStudentById(id)
  }

  def updateStudentById(id: UUID, student: Student): Future[Int] = {
    studentRepository.updateStudentById(id, student)
  }
}