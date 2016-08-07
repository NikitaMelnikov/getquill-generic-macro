package com.github

import io.getquill.{PostgresAsyncContext, SnakeCase}

import scala.util.{Failure, Success}

object Main extends App {
  case class Foo(id: Int, name: String) extends Entity

  implicit val context = new PostgresAsyncContext[SnakeCase]("postgres")
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  val foo = Repositories.repository[Foo](context, ec)
  foo.find() onComplete {
    case Success(result) => println(result)
    case Failure(cause) => println(cause)
  }

  foo.find() onComplete {
    case Success(result) => println(result)
    case Failure(cause) => println(cause)
  }

  foo.find(10) onComplete {
    case Success(result) => println(result)
    case Failure(cause) => println(cause)
  }

  Thread.sleep(1000)
}
