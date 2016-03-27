package event.project.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
/**
 * Created by aphis on 24-Mar-16.
 */
public class SettingScreen extends Screen{

    private final ScreenStack ss;
    private final ImageLayer settingBG;
    private final ImageLayer backButton;
    private final ImageLayer Sound;
    private final ImageLayer unSound;
    private Boolean checkSound;

//    Private Root root;

    public SettingScreen(final ScreenStack ss){
        this.ss = ss;
        checkSound = true;

        Image bg = assets().getImage("Images/settingBG.png");
        settingBG = graphics().createImageLayer(bg);

        Image backImage = assets().getImage("Images/back_button.png");
        backButton = graphics().createImageLayer(backImage);

        Image SoundImage = assets().getImage("Images/unmute.png");
        Sound = graphics().createImageLayer(SoundImage);

        final Image unSoundImage = assets().getImage("Images/mute.png");
        unSound = graphics().createImageLayer(unSoundImage);

        Sound.setTranslation(75,75);
        unSound.setTranslation(75,75);

        backButton.setTranslation(10,10);
        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });
        Sound.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                layer.remove(Sound);
                layer.add(unSound);
            }
        });
        unSound.addListener(new Mouse.LayerAdapter(){
           public void onMouseUp(Mouse.ButtonEvent event){
               layer.remove(unSound);
               layer.add(Sound);
           }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(settingBG);
        this.layer.add(backButton);
        this.layer.add(Sound);
        this.layer.add(unSound);
    }
}

