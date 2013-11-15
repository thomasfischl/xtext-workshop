package com.github.thomasfischl.minipascal.util;

import org.eclipse.emf.ecore.EObject;

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
}
