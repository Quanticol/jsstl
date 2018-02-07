package eu.quanticol.jsstl.dsl.generator

import eu.quanticol.jsstl.dsl.jSSTLScript.Model
import eu.quanticol.jsstl.dsl.jSSTLScript.ConstDeclaration
import eu.quanticol.jsstl.dsl.jSSTLScript.Expression
import eu.quanticol.jsstl.dsl.jSSTLScript.IntExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.FloatExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.TrueFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.FalseFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.NotExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.UnaryMinusExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.LiteralExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.VariableDeclarations
import eu.quanticol.jsstl.dsl.jSSTLScript.ParameterDeclaration
import eu.quanticol.jsstl.dsl.jSSTLScript.MulExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.DivExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.SumExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.DiffExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.LessRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.LessOrEqualRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.GreaterOrEqualRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.GreaterRelationExpression

class SstlToJava {
	
	public final static String CONST_POSTFIX = "_CONST_";
	public final static String VAR_POSTFIX = "_VAR_";
	public final static String PAR_POSTFIX = "_PAR_";
	
	def generateJavaCode( String packageName , String className , Model model ) {
		'''
		package «packageName»;
		
		public class «className» {
			
			«FOR e:model.elements.filter(typeof(ConstDeclaration))»
			public static final double «e.name+CONST_POSTFIX» = «e.value.generateExpressionCode»;
			«ENDFOR»
			
		}
		'''
	}
	
	def generateExpressionCode( IntExpression e ) {
		'''«e.value»'''
	}
	
	def generateExpressionCode( FloatExpression e ) {
		'''«e.intPart».«e.decPart»'''
	}
	
	def generateExpressionCode( TrueFormula e ) {
		'''true'''
	}
	
	def generateExpressionCode( FalseFormula e ) {
		'''false'''
	}
	
	def generateExpressionCode( NotExpression e ) {
		'''!( «e.arg.generateExpressionCode» )'''
	}
	
	def generateExpressionCode( UnaryMinusExpression e ) {
		'''-( «e.arg.generateExpressionCode» )'''
	}
	
	def generateExpressionCode( LiteralExpression e ) {
		val v = e.reference
		switch v {
			VariableDeclarations: v.name+VAR_POSTFIX
			ConstDeclaration: v.name+CONST_POSTFIX
			ParameterDeclaration: v.name+PAR_POSTFIX
		}	
	}
	
	def generateExpressionCode( MulExpression e ) {
		'''( «e.left.generateExpressionCode» )*( «e.right.generateExpressionCode» )'''
	}
	
	def generateExpressionCode( DivExpression e ) {
		'''( «e.left.generateExpressionCode» )/( «e.right.generateExpressionCode» )'''
	}
	
	def generateExpressionCode( SumExpression e ) {
		'''( «e.left.generateExpressionCode» )+( «e.right.generateExpressionCode» )'''
	}
	
	def generateExpressionCode( DiffExpression e ) {
		'''( «e.left.generateExpressionCode» )-( «e.right.generateExpressionCode» )'''
	}
	
	def generateExpressionCode( LessRelationExpression e ) {
		'''((«e.right.generateExpressionCode») - («e.left.generateExpressionCode»))>0'''
	}
	
	def generateExpressionCode( LessOrEqualRelationExpression e ) {
		'''((«e.right.generateExpressionCode») - («e.left.generateExpressionCode»))>=0'''
	}
	
	def generateExpressionCode( GreaterOrEqualRelationExpression e ) {
		'''((«e.left.generateExpressionCode») - («e.right.generateExpressionCode»))>=0'''
	}
	
	def generateExpressionCode( GreaterRelationExpression e ) {
		'''((«e.left.generateExpressionCode») - («e.right.generateExpressionCode»))>0'''
	}
	
	def generateExpressionCode( Expression e ) {
		'''0'''
	}
	
}