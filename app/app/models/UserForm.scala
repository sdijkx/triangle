package models

import play.api.Logger
import play.api.db.DB
import play.api.data._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.db._
import play.api.Play.current


/**
 * Created by sdi20386 on 2-2-2015.
 */
object UserForm {

  case class UserData (email: String, password: String) {

  def validateUserInDatabse(): Boolean = {
    val conn = DB.getConnection()
    try {
      val stmt = conn.prepareStatement("SELECT name, id FROM users WHERE email = ? and password = ? ")
      stmt.setString(1, email)
      stmt.setString(2, password)
      val rs = stmt.executeQuery()
      (rs.next())
    } catch  {
      case e:Exception => {
        Logger.error(e.getMessage, e)
        false
      }
    } finally {
      conn.close()
    }
  }
  }

  implicit val userDataWrites : Writes[UserData] = (
  (JsPath \ "email").write[String] and
  (JsPath \ "password").write[String]
  )(unlift(UserData.unapply))

  implicit val userDataReads : Reads[UserData] = (
  (JsPath \ "email").read[String] and
  (JsPath \ "password").read[String]
  )(UserData.apply _)
}