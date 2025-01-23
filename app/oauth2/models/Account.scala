package oauth2.models

import org.joda.time.DateTime
import play.api.libs.json.Json

import java.security.MessageDigest
import java.util.UUID

case class Account(
                    _id: UUID,
                    email: String,
                    password: String,
                    createdAt: DateTime)

object Account {
  implicit val jsonFormat = Json.format[Account]
}