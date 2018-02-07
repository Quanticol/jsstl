/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Michele Loreti
 *  	Laura Nenzi
 *  	Luca Bortolussi
 *******************************************************************************/
/**
 * 
 */
package eu.quanticol.jsstl.dsl;

import java.util.Arrays;
import java.util.LinkedList;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import eu.quanticol.jsstl.dsl.generator.JSSTLScriptGenerator;
import eu.quanticol.jsstl.dsl.jSSTLScript.Model;
import eu.quanticol.jsstl.dsl.util.Utilities;
import eu.quanticol.jsstl.formula.Formula;
import eu.quanticol.jsstl.formula.jSSTLScript;
import eu.quanticol.jsstl.util.compiler.CharSequenceCompiler;
import eu.quanticol.jsstl.util.compiler.CharSequenceCompilerException;

import com.google.inject.Injector;


/**
 * @author loreti
 *
 */
public class ScriptLoader {

	private XtextResourceSet resourceSet;
	private CharSequenceCompiler<jSSTLScript> compiler;
	private Utilities utilities;
	private JSSTLScriptGenerator generator;

	public ScriptLoader() {
		Injector injector = new JSSTLScriptStandaloneSetup().createInjectorAndDoEMFRegistration();
		this.resourceSet = injector.getInstance(XtextResourceSet.class);
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);		
		this.generator = injector.getInstance(JSSTLScriptGenerator.class);
		this.utilities = injector.getInstance(Utilities.class);
		this.compiler = new CharSequenceCompiler<>(jSSTLScript.class.getClassLoader(), new LinkedList<>());
    }
	
	public jSSTLScript load( String fileName ) {
		System.out.println("Loading: "+fileName);
		URI uri = URI.createURI(fileName);
		Resource resource = resourceSet.getResource(uri, true);
		EObject object = resource.getContents().get(0);
		if (object instanceof Model) {
			Model model = (Model) object;
			String packageName = utilities.getPackageName(uri); 
			String className = utilities.getClassName(uri);
			String fullClassName = packageName+"."+className;
			CharSequence code = generator.generateJavaCode(model, packageName, className);
			final DiagnosticCollector<JavaFileObject> errs =
		            new DiagnosticCollector<JavaFileObject>();
			try {
				Class<jSSTLScript> compiledScript = compiler.compile(fullClassName, code, errs, new Class<?>[] { jSSTLScript.class } );
				return compiledScript.newInstance();
			} catch (ClassCastException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CharSequenceCompilerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}

		
	    return null;

	}
	
	public static void main(String[] argv) {
		if (argv.length > 0) {
			ScriptLoader loader = new ScriptLoader();
			jSSTLScript script = loader.load(argv[0]);
			String[] formulae = script.getFormulae(); 
			System.out.println(Arrays.toString( formulae ));
			Formula f = script.getFormula( formulae[0] );
		} else {
			throw new IllegalArgumentException("Script file expected!");
		}
	}
	
	class JavaSourceFromString extends SimpleJavaFileObject {
		  final String code;

		  JavaSourceFromString(String name, String code) {
		    super(java.net.URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
		    this.code = code;
		  }

		  @Override
		  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		    return code;
		  }
	}

}
