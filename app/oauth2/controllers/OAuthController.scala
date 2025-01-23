package oauth2.controllers

import oauth2.models.Account

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import play.mvc.{Action, Controller}
import scalaoauth2.provider.{AuthInfo, AuthorizationCode, ClientCredentials, OAuth2Provider, OAuthGrantType, Password, RefreshToken, TokenEndpoint}

@Singleton
class OAuthController @Inject() (mongoDataHandler: MongoDataHandler) extends Controller with OAuth2Provider {


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

  override val tokenEndpoint = new TokenEndpoint {
    override val handlers = Map(
      OAuthGrantType.AUTHORIZATION_CODE -> new AuthorizationCode(),
      OAuthGrantType.REFRESH_TOKEN -> new RefreshToken(),
      OAuthGrantType.CLIENT_CREDENTIALS -> new ClientCredentials(),
      OAuthGrantType.PASSWORD -> new Password())
  }

  def accessToken = Action.async { implicit request =>
    issueAccessToken(mongoDataHandler)
  }

  def resources = AuthorizedAction(mongoDataHandler) { request =>
    Ok(Json.toJson(request.authInfo))
  }

}
