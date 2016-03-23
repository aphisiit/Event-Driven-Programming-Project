package event.project.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import event.project.core.MyGame;

public class MyGameActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new MyGame());
  }
}
