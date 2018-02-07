package eu.quanticol.jsstl.dsl.util

import eu.quanticol.jsstl.dsl.jSSTLScript.LiteralExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.VariableDeclarations
import eu.quanticol.jsstl.dsl.jSSTLScript.Expression
import eu.quanticol.jsstl.dsl.jSSTLScript.MulExpression
import java.util.Set
import eu.quanticol.jsstl.dsl.jSSTLScript.DivExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.NotExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.UnaryMinusExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.SumExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.DiffExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.LessRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.LessOrEqualRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.GreaterOrEqualRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.GreaterRelationExpression
import eu.quanticol.jsstl.dsl.jSSTLScript.OrFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.AndFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.UntilFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.GloballyFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.EventuallyFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.SurroundFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.SomewhereFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.EverywhereFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.NotFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.ReferencedFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.AtomicFormula
import eu.quanticol.jsstl.dsl.jSSTLScript.FormulaDeclaration
import org.eclipse.emf.common.util.URI

class Utilities {
	
	def dispatch Set<VariableDeclarations> collectUsedVariables( LiteralExpression e ) {
		val v = e.reference
		switch v {
			VariableDeclarations: newHashSet( v )
			default: newHashSet()
		}
	}
	
	def dispatch Set<VariableDeclarations> collectUsedVariables( GreaterRelationExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}

	def dispatch Set<VariableDeclarations> collectUsedVariables( GreaterOrEqualRelationExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}

	def dispatch Set<VariableDeclarations> collectUsedVariables( LessOrEqualRelationExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}

	def dispatch Set<VariableDeclarations> collectUsedVariables( LessRelationExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}

	def dispatch Set<VariableDeclarations> collectUsedVariables( DiffExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}

	def dispatch Set<VariableDeclarations> collectUsedVariables( SumExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}

	def dispatch Set<VariableDeclarations> collectUsedVariables( MulExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}
	
	def dispatch Set<VariableDeclarations> collectUsedVariables( DivExpression e ) {
		val s = e.left.collectUsedVariables
		s.addAll( e.right.collectUsedVariables )
		s
	}
	
	def dispatch Set<VariableDeclarations> collectUsedVariables( NotExpression e ) {
		e.arg.collectUsedVariables
	}
	
	def dispatch Set<VariableDeclarations> collectUsedVariables( UnaryMinusExpression e ) {
		e.arg.collectUsedVariables
	}
	
	def dispatch collectUsedVariables( Expression e ) {
		newHashSet()
	}
	
	def dispatch Set<FormulaDeclaration> referencedFormulas( OrFormula formula ) {
		var rf = formula.left.referencedFormulas
		rf.addAll( formula.right.referencedFormulas )
		rf
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( AndFormula formula ) {
		var rf = formula.left.referencedFormulas
		rf.addAll( formula.right.referencedFormulas )
		rf
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( UntilFormula formula ) {
		var rf = formula.left.referencedFormulas
		rf.addAll( formula.right.referencedFormulas )
		rf
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( GloballyFormula formula ) {
		formula.arg.referencedFormulas
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( EventuallyFormula formula ) {
		formula.arg.referencedFormulas
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( SurroundFormula formula ) {
		var rf = formula.left.referencedFormulas
		rf.addAll( formula.right.referencedFormulas )
		rf
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( SomewhereFormula formula ) {
		formula.arg.referencedFormulas
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( EverywhereFormula formula ) {
		formula.arg.referencedFormulas
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( NotFormula formula ) {
		formula.arg.referencedFormulas
	}

	def dispatch Set<FormulaDeclaration> referencedFormulas( ReferencedFormula formula ) {
		newHashSet( formula.reference ) 
	}
	
	def dispatch Set<FormulaDeclaration> referencedFormulas( AtomicFormula formula ) {
		newHashSet()
	}
	
	def getPackageName(URI uri) {
		if (uri.isPlatform()) {
			var toReturn = uri.toPlatformString(true);
			if (toReturn.startsWith("/")) {
				toReturn = toReturn.substring(1);
			}
			var lastSlash = toReturn.lastIndexOf('/');
			var packageFolder = "";
			if (lastSlash >= 0) {
				packageFolder = "jsstl"
			}
			packageFolder = packageFolder.replace('.', '_');
			var lastDot = toReturn.lastIndexOf('.');
			if (lastDot >= 0) {
				packageFolder = packageFolder + "/" + (toReturn.substring(lastDot + 1));
			}
			packageFolder;
		} else {
			"jsstl"
		}
	}	
	
	def getClassName(URI uri) {
		if (uri.isPlatform()) {
			var toReturn = uri.toPlatformString(true);
			var start = toReturn.lastIndexOf('/');
			if (start < 0) {
				start = 0;
			} else {
				start = start + 1;
			}
			var end = toReturn.lastIndexOf('.');
			if (end < 0) {
				end = toReturn.length();
			}
			toReturn.substring(start, end);
		} else {
			"Model";			
		}
	}
}