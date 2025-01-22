package repository

import java.util.UUID

import models._

import scala.concurrent.Future

trait StudentRepository {
  def getStudents(): Future[List[Student]]
  def getStudentById(id: UUID): Future[Option[Student]]
  def createStudent(student: Student): Future[Either[APIError, Student]]
  def deleteStudentById(id: UUID): Future[Int]
  def updateStudentById(id: UUID, student: Student): Future[Int]
}
