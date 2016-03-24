package event.project.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.Sound;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Root;
/**
 * Created by aphis on 24-Mar-16.
 */
public class SettingScreen extends Screen{

    private final ScreenStack ss;
    private final ImageLayer settingBG;
    private final ImageLayer backButton;
    private final ImageLayer Sound;

//    Private Root root;

    public SettingScreen(final ScreenStack ss){
        this.ss = ss;

        Image bg = assets().getImage("Images/settingBG.png");
        settingBG = graphics().createImageLayer(bg);

        Image backImage = assets().getImage("Images/back_button.png");
        backButton = graphics().createImageLayer(backImage);

        Image SoundImage = assets().getImage("Images/unmute.png");
        Sound = graphics().createImageLayer(SoundImage);

        Sound.setTranslation(75,75);

        backButton.setTranslation(10,10);
        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(settingBG);
        this.layer.add(backButton);
        this.layer.add(Sound);
    }
}
