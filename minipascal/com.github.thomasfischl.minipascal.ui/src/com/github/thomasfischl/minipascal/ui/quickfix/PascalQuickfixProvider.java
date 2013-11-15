/*
 * generated by Xtext
 */
package com.github.thomasfischl.minipascal.ui.quickfix;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.github.thomasfischl.minipascal.pascal.Fact;
import com.github.thomasfischl.minipascal.pascal.Stat;
import com.github.thomasfischl.minipascal.pascal.VarDecl;
import com.github.thomasfischl.minipascal.pascal.VarName;
import com.github.thomasfischl.minipascal.pascal.impl.ModelImpl;
import com.github.thomasfischl.minipascal.pascal.impl.PascalFactoryImpl;
import com.github.thomasfischl.minipascal.util.ModelUtil;
import com.github.thomasfischl.minipascal.validation.PascalJavaValidator;

/**
 * Custom quickfixes.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#quickfixes
 */
public class PascalQuickfixProvider extends org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider {

  @Fix(PascalJavaValidator.UNDECLARED_VARIABLE)
  public void fixUndeclaredVariable(final Issue issue, IssueResolutionAcceptor acceptor) {
    String msg = String.format("Declare a new variable '%s'.", issue.getData()[0]);
    acceptor.accept(issue, "Add Declaration", msg, "upcase.png", new ISemanticModification() {
      @Override
      public void apply(EObject element, IModificationContext context) {
        String name = null;
        if (element instanceof Fact) {
          name = ((Fact) element).getVar();
        } else if (element instanceof Stat) {
          Stat stat = (Stat) element;
          if (stat.getLeftside() != null) {
            name = stat.getLeftside();
          } else if (stat.getRead() != null) {
            name = stat.getRead();
          }
        }

        if (name != null) {
          ModelImpl modelImpl = ModelUtil.getModelImpl(element);
          if (modelImpl.getVardecls() == null) {
            modelImpl.setVardecls(PascalFactoryImpl.eINSTANCE.createVarDecl());
          }

          VarName varName = PascalFactoryImpl.eINSTANCE.createVarName();
          varName.setName(name);
          modelImpl.getVardecls().getVars().add(varName);
        } else {
          throw new IllegalArgumentException("The fact element is not a variable usage.");
        }
      }
    });
  }

  @Fix(PascalJavaValidator.REMOVE_VARIABLE)
  public void fixUnusedVariable(final Issue issue, IssueResolutionAcceptor acceptor) {
    String msg = String.format("Remove unused variable '%s'.", issue.getData()[0]);
    acceptor.accept(issue, "Remove variable", msg, "upcase.png", new ISemanticModification() {
      @Override
      public void apply(EObject element, IModificationContext context) {
        if (element instanceof VarName) {
          VarDecl parent = (VarDecl) element.eContainer();
          parent.getVars().remove(element);
          if (parent.getVars().isEmpty()) {
            ModelImpl model = (ModelImpl) parent.eContainer();
            model.setVardecls(null);
          }
        }
        System.out.println(element);
      }
    });
  }
}
