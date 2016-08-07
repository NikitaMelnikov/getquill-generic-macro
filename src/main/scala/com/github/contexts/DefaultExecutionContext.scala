package com.github.contexts

trait DefaultExecutionContext {
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
}
