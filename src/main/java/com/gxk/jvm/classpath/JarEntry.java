package com.gxk.jvm.classpath;

import com.gxk.jvm.classfile.ClassFile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarFile;

import com.gxk.jvm.classfile.ClassReader;

public class JarEntry implements Entry {

  public final Path path;

  public JarEntry(Path path) {
    this.path = path;
  }

  @Override
  public ClassFile findClass(String name) {
    JarFile file;
    try {
      file = new JarFile(path.toFile());
    } catch (IOException e) {
      throw new IllegalStateException();
    }

    java.util.jar.JarEntry jarEntry = file.getJarEntry(name + ".class");

    if (jarEntry == null) {
      return null;
    }

    try (InputStream is = file.getInputStream(jarEntry)) {
      ClassFile cf = ClassReader.read(new DataInputStream(new BufferedInputStream(is)));
      cf.setSource(path.toString());

      return cf;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException();
    }
  }
}
