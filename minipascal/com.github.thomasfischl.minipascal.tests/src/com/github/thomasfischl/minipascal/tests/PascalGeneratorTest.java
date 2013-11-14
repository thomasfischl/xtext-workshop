package com.github.thomasfischl.minipascal.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.thomasfischl.minipascal.PascalInjectorProvider;
import com.github.thomasfischl.minipascal.pascal.Model;
import com.google.inject.Inject;

@RunWith(XtextRunner.class)
@InjectWith(PascalInjectorProvider.class)
public class PascalGeneratorTest {

  @Inject
  IGenerator generator;
  @Inject
  ParseHelper<Model> parser;

  @Test
  public void testHelloWorld() throws Exception {
    testMiniPasFile("helloworld");
  }

  private void testMiniPasFile(String name) throws Exception {
    Model model = parser.parse(readFile(name + ".minipas"));
    assertNotNull(model);
    InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
    generator.doGenerate(model.eResource(), fsa);

    assertEquals(1, fsa.getTextFiles().size());
    String result = fsa.getTextFiles().values().iterator().next().toString();
    assertEquals(readFile(name + ".output"), result);
  }

  private String readFile(String filename) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader is = null;
    try {
      is = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));

      String line;
      while ((line = is.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } finally {
      if (is != null) {
        is.close();
      }
    }
    return sb.toString();
  }

}
