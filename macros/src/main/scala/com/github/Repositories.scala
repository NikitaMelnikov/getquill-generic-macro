package com.github

import io.getquill.context.async.AsyncContext

import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait Entity {
  val id: Int
}

abstract class Repository[T <: Entity](database: AsyncContext[_, _, _], ec: ExecutionContext) {
  def find(): Future[Option[T]]
  def find(id: Int): Future[Option[T]]
}

object Repositories {
  def repository[T <: Entity](implicit database: AsyncContext[_, _, _], ec: ExecutionContext): Repository[T] = macro repository_impl[T]

  def repository_impl[T <: Entity : c.WeakTypeTag](c: blackbox.Context)(database: c.Expr[AsyncContext[_, _, _]], ec: c.Expr[ExecutionContext]): c.Expr[Repository[T]] = {
    import c.universe._
    val clazz = weakTypeOf[T].typeSymbol.asClass

    c.Expr[Repository[T]](q"""
      new Repository[$clazz]($database, $ec) {
        def find() = {
          val q = context.quote {
            context.query[$clazz]
          }
          context.run(q).map(_.headOption)
        }

        def find(id: Int) = {
          val q = context.quote {
            (id: Int) => context.query[$clazz].filter(_.id == id)
          }
          context.run(q)(id).map(_.headOption)
        }
      }
    """)
  }
}
