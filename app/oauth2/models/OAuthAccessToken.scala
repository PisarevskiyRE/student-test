package oauth2.models

import org.joda.time.DateTime
import play.api.libs.json.Json
import java.util.UUID

case class OAuthAccessToken(
                             _id: UUID,
                             accountId: UUID,
                             account: Option[Account] = None,
                             oauthClientId: UUID,
                             oauthClient: Option[OAuthClient] = None,
                             accessToken: String,
                             refreshToken: String,
                             createdAt: DateTime)

object OAuthAccessToken {
  implicit val jsonFormat = Json.format[OAuthAccessToken]
}