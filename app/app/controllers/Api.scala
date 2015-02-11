package controllers

import controllers.Application._
import models.Book._
import models.UserForm._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import play.api.Logger
import scala.concurrent._


object Api extends Controller {

  case class Security[A](action: Action[A]) extends Action[A] {

    def apply(request: Request[A]): Future[Result] = {

      val token = request.headers.get("Security")
      Logger.info("Calling action with token:" + token)
      token match {
        case Some(value) => action(request)
        case None => Future.successful(Unauthorized(Json.obj("status" -> "ERROR","message" -> "Unknown user")))
      }
    }

    lazy val parser = action.parser
  }

  object SecurityAction extends ActionBuilder[Request] {
    def invokeBlock[A](request : Request[A], block: (Request[A]) => Future[Result]) = {
      block(request)
    }
    override def composeAction[A](action: Action[A]) = new Security(action)
  }


  def login = Action(parse.json) { request =>
    val userDataResult = request.body.validate[UserData]

    userDataResult.fold( errors => {
      BadRequest(Json.obj("status" -> "ERROR","message" -> JsError.toFlatJson(errors)))
    }, userData => {
      if(userData.validateUserInDatabse()) {
        Ok(Json.obj("status" -> "OK", "username" -> userData.email, "token" -> "TESTTOKEN"));
      } else {
        Unauthorized(Json.obj("status" -> "ERROR","message" -> "Unknown user"))
      }
    } )
  }

  def user = SecurityAction { request =>
    Ok(Json.obj("user" -> "Ok"))
  }

  def listBooks = SecurityAction {
    Ok(Json.toJson(books))
  }

  def saveBook = SecurityAction(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Book]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      book => {
        addBook(book)
        Ok(Json.obj("status" -> "OK"))
      }
    )
  }

}
