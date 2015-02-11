package models

import play.api.Logger
import play.api.db.DB
import play.api.Play.current

/**
 * Created by sdi20386 on 1-2-2015.
 */
object User {

  case class UserEmailAuthorization(email: String, password: String) {
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

  case class User(name : String, email : String, personalia : String)
}
