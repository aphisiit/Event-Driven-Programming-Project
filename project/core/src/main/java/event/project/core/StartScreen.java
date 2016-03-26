package event.project.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.ScreenStack;
import tripleplay.game.Screen;

/**
 * Created by Administrator on 23/3/2559.
 */
public class StartScreen extends Screen {

    private final ScreenStack ss;
    private final ImageLayer startLayer;
    private final ImageLayer startButtonLayer;
    private final ImageLayer settingButtonLayer;

    public StartScreen(final ScreenStack ss){
        this.ss = ss;

        Image startImage = assets().getImage("images/StartScreen.png");
        Image startButton = assets().getImage("images/StartButton.png");
        Image settingButton = assets().getImage("images/SettingButton.png");

        startLayer = graphics().createImageLayer(startImage);
        startButtonLayer = graphics().createImageLayer(startButton);
        settingButtonLayer = graphics().createImageLayer(settingButton);

    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(startLayer);
        this.layer.add(startButtonLayer);
        this.layer.add(settingButtonLayer);
    }
}
