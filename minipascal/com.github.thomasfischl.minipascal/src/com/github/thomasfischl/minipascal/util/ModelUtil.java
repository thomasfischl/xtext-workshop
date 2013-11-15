package com.github.thomasfischl.minipascal.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import com.github.thomasfischl.minipascal.pascal.Fact;
import com.github.thomasfischl.minipascal.pascal.Stat;
import com.github.thomasfischl.minipascal.pascal.VarName;
import com.github.thomasfischl.minipascal.pascal.impl.ModelImpl;

public class ModelUtil {

  public static ModelImpl getModelImpl(EObject obj) {

    if (obj instanceof ModelImpl) {
      return (ModelImpl) obj;
    }

    if (obj != null) {
      return getModelImpl(obj.eContainer());
    }

    throw new IllegalStateException("Invalid Model");
  }

  public static Set<String> getAllUsedVariable(EObject eObj) {
    Set<String> usedVars = new HashSet<String>();
    TreeIterator<EObject> it = eObj.eResource().getAllContents();
    while (it.hasNext()) {
      EObject obj = it.next();
      if (obj instanceof Stat) {
        Stat stat = (Stat) obj;
        if (stat.getLeftside() != null) {
          usedVars.add(stat.getLeftside());
        } else if (stat.getRead() != null) {
          usedVars.add(stat.getRead());
        }
      } else if (obj instanceof Fact) {
        Fact fact = (Fact) obj;
        if (fact.getVar() != null) {
          usedVars.add(fact.getVar());
        }
      }
    }
    return usedVars;
  }

  public static VarName getVarName(String name, EObject obj) {
    ModelImpl modelImpl = ModelUtil.getModelImpl(obj);
    if (modelImpl.getVardecls() != null) {
      for (VarName var : modelImpl.getVardecls().getVars()) {
        if (name.equals(var.getName())) {
          return var;
        }
      }
    }
    return null;
  }
}
