package io.github.skeith1nd.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import io.github.skeith1nd.core.SoH;

public class SoHHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("soh/");
    PlayN.run(new SoH());
  }
}
