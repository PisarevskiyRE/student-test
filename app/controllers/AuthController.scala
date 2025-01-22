package controllers

import play.api.mvc.{Action, BaseController, ControllerComponents}

import models.LoginRequest
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import repository.UserDAO

import scala.concurrent.ExecutionContext

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents)
                              (implicit ec: ExecutionContext)
  extends BaseController {

  def login: Action[JsValue] = Action(parse.json) { implicit request =>
    request.body
      .validate[LoginRequest]
      .fold(
        errors => BadRequest(errors.mkString),
        req => {
          val auth_token = UserDAO.getAuthToken(req)
          auth_token match {
            case Some(s) => Ok(Json.parse(s"""{"access_token": "$s"}"""))
            case None    => Unauthorized("Нет доступа")
          }
        }
      )
  }
}