package com.xfactory

import java.util.UUID

import com.xfactory.TestObjects._
import com.xfactory.dsl._
import com.xfactory.utils.ConstantDataGeneratorWithUUID
import org.specs2.mutable.SpecificationWithJUnit

object TestObjects {
  case class DoubleWrapper(value: Double)
  case class FloatWrapper(value: Float)
  case class LongWrapper(value: Long)
  case class IntWrapper(value: Int)
  case class CharWrapper(value: Char)
  case class ShortWrapper(value: Short)
  case class ByteWrapper(value: Byte)
  case class UnitWrapper(value: Unit)
  case class BooleanWrapper(value: Boolean)
  case class StringWrapper(value: String)

  case class Person(name: String, age: Short, height: Double, gender: Char)
  case class Address(street: String, city: String)
  case class PersonWithAddress(name: String, address: Address, id: UUID)
}

class RandomMacroTest extends SpecificationWithJUnit {
  implicit val generator = ConstantDataGeneratorWithUUID
  import ConstantDataGeneratorWithUUID._

  "x-factory simple factory" should {
    "generate random objects with" >> {
      "double value" in {
        random[DoubleWrapper] must be_==(DoubleWrapper(someDouble))
      }

      "float value" in {
        random[FloatWrapper] must be_==(FloatWrapper(someFloat))
      }

      "long value" in {
        random[LongWrapper] must be_==(LongWrapper(someLong))
      }

      "int value" in {
        random[IntWrapper] must be_==(IntWrapper(someInt))
      }

      "char value" in {
        random[CharWrapper] must be_==(CharWrapper(someChar))
      }

      "short value" in {
        random[ShortWrapper] must be_==(ShortWrapper(someShort))
      }

      "byte value" in {
        random[ByteWrapper] must be_==(ByteWrapper(someByte))
      }

      "unit value" in {
        random[UnitWrapper] must be_==(UnitWrapper(someUnit))
      }

      "boolean value" in {
        random[BooleanWrapper] must be_==(BooleanWrapper(someBoolean))
      }

      "string value" in {
        random[StringWrapper] must be_==(StringWrapper(someString))
      }
    }

    "generate random instance of a given case class with different field types" in {
      random[Person] must be_==(Person(someString, someShort, someDouble, someChar))
    }

    "generate random instance of a given case class with complex field types" in {
      random[PersonWithAddress] must be_==(PersonWithAddress(someString, Address(someString, someString), someUUID))
    }

    "generate random instance of a value class" in {
      random[String] must be_==(someString)
    }
  }
}
