package com.xfactory

import com.xfactory.generators.TestDataGenerator

import scala.reflect.macros.blackbox.Context


class SimpleFactoryMacro[C <: Context](val context: C) {
  import context.universe._

  private def isCaseClass(tpe: Type) = tpe.companion.members.exists(m => m.isMethod && m.name == TermName("apply"))

  private def caseClassConstructor(tpe: Type) = {
    tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head
  }

  private def findGenerationFn(generator: Expr[TestDataGenerator], `type`: Type) = {
    generator.actualType.members.find { m =>
      m.isMethod &&
        m.asMethod.paramLists.isEmpty &&
        m.asMethod.returnType.typeSymbol == `type`.typeSymbol
    }
  }

  private def createCaseClassInstance[T](generator: Expr[TestDataGenerator], `type`: Type): Expr[T] = {
    val companionObject = `type`.typeSymbol.companion
    val constructorArgs = caseClassConstructor(`type`)

    val constructorAssignments = constructorArgs.map { field =>
      val name = field.asTerm.name
      val returnType = `type`.decl(name).typeSignature

      val assignment = findGenerationFn(generator, returnType) match {
        case Some(method) => q"""$name = $generator.$method"""

        case None if isCaseClass(returnType) => q"""$name = random[${returnType.typeSymbol}]($generator)"""

        case _ =>  context.abort(context.enclosingPosition, s"cannot assign $name in class ${`type`}. A valid generator for ${returnType.typeSymbol.name} type was not found, or ${returnType.typeSymbol} is not a valid case class")
      }

      assignment
    }

    context.Expr(q"""$companionObject.apply(..$constructorAssignments)""")
  }

  def simpleFactoryImpl[T: WeakTypeTag](withGenerator: Expr[TestDataGenerator]): Expr[T] = {
    val tpe = weakTypeOf[T]

    findGenerationFn(withGenerator, tpe) match {
      case Some(method) => context.Expr(q"""$withGenerator.$method""")
      case _ => createCaseClassInstance[T](withGenerator, tpe)
    }
  }
}

object SimpleFactoryMacro {
  def apply[T: c.WeakTypeTag](c: Context)(withGenerator: c.universe.Expr[TestDataGenerator]): c.Expr[T] = new SimpleFactoryMacro[c.type](c).simpleFactoryImpl(withGenerator)
}