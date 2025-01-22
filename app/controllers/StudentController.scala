package controllers

import java.util.UUID

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import models._
import services._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StudentController @Inject()(val controllerComponents: ControllerComponents,
                                  studentService: StudentService,
                                  authService: AuthService)(implicit ec: ExecutionContext)
  extends BaseController {

  def getStudents: Action[AnyContent] = Action.async { implicit request =>
    authService.withUser(_ =>
      studentService.getStudents().map { students =>
        Ok(Json.toJson(students))
      }
    )
  }

  def getStudentById(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    authService.withUser(_ =>
      studentService.getStudentById(id).map {
        case Some(student) => Ok(Json.toJson(student))
        case None        => NotFound
      }
    )
  }

  def deleteStudentById(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    authService.withUser(user =>
      if (user.isAdmin) studentService.deleteStudentById(id).map {
        case 1 => Ok("Ok")
        case 0 => BadRequest("Ошибка при удалении студента")
      } else Future { Unauthorized("Нет доступа") }
    )
  }

  def createStudent: Action[JsValue] = Action(parse.json).async { implicit request => {
    request.body
      .validate[NewStudent]
      .fold(
        errors => Future {
          BadRequest(errors.mkString)
        },
        newStudent => {
          studentService.createStudent(
            Student(UUID.randomUUID(),
              newStudent.name,
              newStudent.lastName,
              newStudent.firstName,
              newStudent.groupName,
              newStudent.scoreAvg
            )).map {
            case Left(_) => BadRequest("Ошибка при создании студента")
            case Right(student) => Ok(Json.toJson(student))
          }
        }
      )
  }
  }

  def updateStudentById(id: UUID): Action[JsValue] = Action(parse.json).async { implicit request =>
    request.body
      .validate[Student]
      .fold(
        errors => Future { BadRequest(errors.mkString) },
        student => {
          studentService.updateStudentById(id, student).map {
            case 1 => Ok("Ok")
            case 0 => BadRequest("Ошибка при редактировании студента")
          }
        }
      )
  }
}