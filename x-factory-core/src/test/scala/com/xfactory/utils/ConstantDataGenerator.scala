package com.xfactory.utils

import java.util.UUID

import com.xfactory.generators.TestDataGenerator

trait ConstantDataGenerator extends TestDataGenerator {
  def someDouble: Double = 1.23

  def someFloat: Float = 1.23f

  def someLong: Long = 65536L

  def someInt: Int = 1024

  def someChar: Char = 'm'

  def someShort: Short = 10

  def someByte: Byte = 1

  def someUnit: Unit = ()

  def someBoolean: Boolean = true

  def someString: String = "test"
}

object ConstantDataGeneratorWithUUID extends ConstantDataGenerator {
  def someUUID: UUID = UUID.fromString("520ca213-f896-4248-bafa-fcf543e7e324")
}