package controllers

import java.util.{Date, UUID}
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import models._
import oauth2.models.Account

import scalaoauth2.provider.{AccessToken, AuthInfo, AuthorizedActionFunction, OAuth2ProviderActionBuilders}
import services._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StudentController @Inject()(val controllerComponents: ControllerComponents,
                                  studentService: StudentService)(implicit ec: ExecutionContext)
  extends BaseController with OAuth2ProviderActionBuilders {

  def getStudents: Action[AnyContent] = Action.async { implicit request =>
    validateStudentCertificate(request).flatMap {
      case true =>
        studentService.getStudents().map { students =>
          Ok(Json.toJson(students))
        }
      case false =>
        Future.successful(Forbidden(Json.obj("error" -> "нет прав")))
    }
  }

  def getStudentById(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    validateStudentCertificate(request).flatMap {
      case true =>
        studentService.getStudentById(id).map {
          case Some(student) => Ok(Json.toJson(student))
          case None        => NotFound
        }
      case false =>
        Future.successful(Forbidden(Json.obj("error" -> "нет прав")))
    }
  }


  def deleteStudentById(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    validateStudentCertificate(request).flatMap {
      case true =>
        studentService.deleteStudentById(id).map {
          case 1 => Ok("Ok")
          case 0 => BadRequest("Ошибка при удалении студента")
        }
      case false =>
        Future.successful(Forbidden(Json.obj("error" -> "нет прав")))
    }
  }

  def createStudent(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    validateStudentCertificate(request).flatMap {
      case true =>
        request.body.asJson match {
          case Some(json) =>
            json.validate[NewStudent].fold(
              errors => {
                val errorMessages = errors.map { case (path, validationErrors) =>
                  s"${path.toString}: ${validationErrors.map(_.message).mkString(", ")}"
                }.mkString("; ")
                Future.successful(BadRequest(Json.obj("error" -> errorMessages)))
              },
              newStudent => {
                studentService.createStudent(
                  Student(
                    UUID.randomUUID(),
                    newStudent.name,
                    newStudent.lastName,
                    newStudent.firstName,
                    newStudent.groupName,
                    newStudent.scoreAvg
                  )
                ).map {
                  case Left(_) => BadRequest(Json.obj("error" -> "Ошибка при создании студента"))
                  case Right(student) => Ok(Json.toJson(student))
                }
              }
            )
          case None =>
            Future.successful(BadRequest(Json.obj("error" -> "Ожидался JSON в теле запроса")))
        }
      case false =>
        Future.successful(Forbidden(Json.obj("error" -> "нет прав")))
    }
  }


  private def validateStudentCertificate(request: Request[AnyContent]): Future[Boolean] = {
    val maybeToken = request.headers.get("Authorization").map(_.stripPrefix("Bearer "))
    maybeToken match {
      case Some(token) => Future.successful(true)
      case None => Future.successful(false)
    }
  }

  def updateStudentById(id: UUID): Action[JsValue] = Action(parse.json).async { implicit request =>
    request.body
      .validate[Student]
      .fold(
        errors => {
          val errorMessages = errors.map { case (path, validationErrors) =>
            s"${path.toString}: ${validationErrors.map(_.message).mkString(", ")}"
          }.mkString("; ")
          Future.successful(BadRequest(Json.obj("error" -> errorMessages)))
        },
        student => {
          studentService.updateStudentById(id, student).map {
            case 1 => Ok(Json.obj("status" -> "success"))
            case 0 => BadRequest(Json.obj("error" -> "Ошибка при редактировании студента"))
          }
        }
      )
  }
}