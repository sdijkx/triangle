package controllers

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import models.UserForm._

object Application extends Controller {

  val userForm = Form (
    mapping(
      "email" -> nonEmptyText,
      "password" -> text
    )(UserData.apply)(UserData.unapply)
  )

  def index = Action { implicit request =>
    request.session.get("connected").map { email  =>
      Ok(views.html.Application.home(email))
    }.getOrElse {
      Ok(views.html.Application.index())
    }
  }

  def login = Action { implicit request =>
    request.session.get("connected").map { email =>
      Redirect("/index")
    }.getOrElse {
      Ok(views.html.Application.login(userForm))
    }
  }

  def loginSubmit = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.Application.login(formWithErrors)).flashing("failure" -> "form.error")
      },
      userData => {
        if(userData.validateUserInDatabse()) {
          Redirect("/index").withSession( "connected" -> userData.email).flashing(
            "success" -> "login.success"
          )
        } else {
          Redirect("/login").flashing(
            "failure" -> "login.invalid"
          )
        }
      }
    )
  }

  def signoff = Action { implicit request =>
    Redirect("/index").withNewSession.flashing("success" -> "signoff.success")
  }

  def single = Action { implicit  request =>
    Ok(views.html.single());
  }

}
