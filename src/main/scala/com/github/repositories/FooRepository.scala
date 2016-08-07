package com.github.repositories

import com.github.contexts.DefaultExecutionContext
import com.github.databases.DefaultDatabase
import com.github.{BaseRepository, Foo, Repositories, Repository}

class FooRepository extends BaseRepository[Foo] with DefaultDatabase with DefaultExecutionContext {
  import database._

  val crud: Repository[Foo] = Repositories.repository[Foo](database, ec)

  def findByName(name: String) = {
    val q  = quote {
      (name: String) => query[Foo].filter(_.name == name)
    }

    run(q)(name).map(_.headOption)
  }
}