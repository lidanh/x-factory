package com.xfactory

import com.xfactory.generators.TestDataGenerator

import scala.language.experimental.macros

package object dsl {
  def random[T](implicit withGenerator: TestDataGenerator): T = macro SimpleFactoryMacro[T]
}
