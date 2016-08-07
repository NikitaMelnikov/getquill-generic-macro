package com.github

import com.github.contexts.DefaultExecutionContext
import com.github.databases.DefaultDatabase
import com.github.repositories.FooRepository

import scala.util.{Failure, Success}

object Main extends App with DefaultExecutionContext with DefaultDatabase {
  val barRepository = Repositories.repository[Foo]
  val fooRepository = new FooRepository()

  fooRepository.findByName("Test #1") onComplete {
    case Success(foo) => println(s"Foo: $foo")
    case Failure(cause) => println(cause)
  }

  barRepository find() onComplete {
    case Success(bar) => println(s"Bar: $bar")
    case Failure(cause) => println(cause)
  }

  Thread.sleep(1000)
}
