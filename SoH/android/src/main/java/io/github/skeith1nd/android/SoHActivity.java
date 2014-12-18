package io.github.skeith1nd.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import io.github.skeith1nd.core.SoH;

public class SoHActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new SoH());
  }
}
