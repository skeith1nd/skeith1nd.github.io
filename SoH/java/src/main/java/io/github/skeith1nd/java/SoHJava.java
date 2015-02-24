package io.github.skeith1nd.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import io.github.skeith1nd.core.SoH;

public class SoHJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    config.width = 280;
    config.height = 210;
    JavaPlatform.register(config);
    PlayN.run(new SoH());
  }
}
