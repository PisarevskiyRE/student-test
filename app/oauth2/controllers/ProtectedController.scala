package oauth2.controllers

import oauth2.models._
import oauth2.services.MyDataHandler
import play.api.Logging

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import play.mvc.Controller
import scalaoauth2.provider._

import scala.concurrent.ExecutionContext

class ProtectedController @Inject()(val controllerComponents: ControllerComponents,
                                    dataHandler: MyDataHandler)
                                   (implicit exec: ExecutionContext) extends BaseController with OAuth2ProviderActionBuilders with Logging {

  implicit val authInfoWrites = new Writes[AuthInfo[Account]] {
    def writes(authInfo: AuthInfo[Account]) = {
      Json.obj(
        "account" -> Json.obj(
          "email" -> authInfo.user.email
        ),
        "clientId" -> authInfo.clientId,
        "redirectUri" -> authInfo.redirectUri
      )
    }
  }
  def resources = AuthorizedAction(dataHandler) { request =>
    Ok(Json.toJson(request.authInfo))
  }
}