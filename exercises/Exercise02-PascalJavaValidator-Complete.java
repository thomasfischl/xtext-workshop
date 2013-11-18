/*
 * generated by Xtext
 */
package com.github.thomasfischl.minipascal.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;

import com.github.thomasfischl.minipascal.pascal.Fact;
import com.github.thomasfischl.minipascal.pascal.Model;
import com.github.thomasfischl.minipascal.pascal.PascalPackage;
import com.github.thomasfischl.minipascal.pascal.Stat;
import com.github.thomasfischl.minipascal.pascal.VarDecl;
import com.github.thomasfischl.minipascal.pascal.VarName;

/**
 * Custom validation rules.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
public class PascalJavaValidator
		extends
		com.github.thomasfischl.minipascal.validation.AbstractPascalJavaValidator {

	public final static String REMOVE_VARIABLE = "REMOVE_VARIABLE";

	@Check
	public void checkDoubleVariableDeclaration(VarDecl varDecl) {
		Set<String> varNames = new HashSet<String>();
		for (VarName var : varDecl.getVars()) {
			if (varNames.contains(var.getName())) {
				String msg = String
						.format("The variable '%s' is already declared.",
								var.getName());
				error(msg, var, PascalPackage.Literals.VAR_NAME__NAME,
						REMOVE_VARIABLE, var.getName());
			} else {
				varNames.add(var.getName());
			}
		}
	}

}