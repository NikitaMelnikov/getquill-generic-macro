package com.github

import io.getquill.context.async.AsyncContext

import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait Entity {
  val id: Int
}

abstract class Repository[T <: Entity] {
  implicit val database: AsyncContext[_, _, _]
  implicit val ec: ExecutionContext

  def find(): Future[Option[T]]
  def find(id: Int): Future[Option[T]]
}

abstract class BaseRepository[T <: Entity] extends Repository[T] {
  val crud: Repository[T]

  def find(): Future[Option[T]] = crud.find()
  def find(id: Int): Future[Option[T]] = crud.find(id)
}

object Repositories {

  def repository[T <: Entity](implicit database: AsyncContext[_, _, _], ec: ExecutionContext): Repository[T] = macro repository_impl[T]

  def repository_impl[T <: Entity : c.WeakTypeTag](c: blackbox.Context)(database: c.Expr[AsyncContext[_, _, _]], ec: c.Expr[ExecutionContext]): c.Expr[Repository[T]] = {
    import c.universe._
    val clazz = weakTypeOf[T].typeSymbol.asClass

    c.Expr[Repository[T]](q"""
      new Repository[$clazz]() {
        implicit val database = $database
        implicit val ec = $ec

        def find() = {
          val q = database.quote {
            database.query[$clazz]
          }
          database.run(q).map(_.headOption)
        }

        def find(id: Int) = {
          val q = database.quote {
            (id: Int) => database.query[$clazz].filter(_.id == id)
          }
          database.run(q)(id).map(_.headOption)
        }
      }
    """)
  }
}
