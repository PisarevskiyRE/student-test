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

@Singleton
class OAuthController @Inject()(val controllerComponents: ControllerComponents,
                                dataHandler: MyDataHandler)
                               (implicit exec: ExecutionContext) extends BaseController with OAuth2Provider with OAuth2ProviderActionBuilders with Logging {

  override val tokenEndpoint = new TokenEndpoint {
    override val handlers = Map(
      OAuthGrantType.AUTHORIZATION_CODE -> new AuthorizationCode(),
      OAuthGrantType.REFRESH_TOKEN -> new RefreshToken(),
      OAuthGrantType.CLIENT_CREDENTIALS -> new ClientCredentials(),
      // OAuthGrantType.PASSWORD -> new Password()
    )
  }

  def accessToken = Action.async { implicit request =>
    issueAccessToken(dataHandler)
  }
}