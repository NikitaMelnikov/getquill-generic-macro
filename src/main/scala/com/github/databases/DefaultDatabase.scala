package com.github.databases

import io.getquill.{PostgresAsyncContext, SnakeCase}

trait DefaultDatabase  {
  implicit val database = new PostgresAsyncContext[SnakeCase]("postgres")
}
