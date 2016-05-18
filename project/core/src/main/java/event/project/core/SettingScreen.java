package event.project.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.Sound;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

/**
 * Created by aphis on 24-Mar-16.
 */
public class SettingScreen extends Screen{

    private final ScreenStack ss;
    private final ImageLayer settingBG;
    private final ImageLayer backButton;
    private final ImageLayer SoundLayer;
    private final ImageLayer unSound;
   // private final Sound sound;

    public SettingScreen(final ScreenStack ss){
        this.ss = ss;

        //sound = assets().getSound("sound/lean_on");
        //sound.setLooping(true);
       //sound.play();

        Image bg = assets().getImage("Images/settingBG.png");
        settingBG = graphics().createImageLayer(bg);

        Image backImage = assets().getImage("Images/back_button.png");
        backButton = graphics().createImageLayer(backImage);

        Image SoundImage = assets().getImage("Images/unmute.png");
        SoundLayer = graphics().createImageLayer(SoundImage);

        final Image unSoundImage = assets().getImage("Images/mute.png");
        unSound = graphics().createImageLayer(unSoundImage);

        SoundLayer.setTranslation(75,75);
        unSound.setTranslation(75,75);

        backButton.setTranslation(10,10);
        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });
        SoundLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                //sound.setVolume(0.0f);
                layer.remove(SoundLayer);
                layer.add(unSound);
            }
        });
        unSound.addListener(new Mouse.LayerAdapter(){
           public void onMouseUp(Mouse.ButtonEvent event){
               //sound.setVolume(1.0f);
               layer.remove(unSound);
               layer.add(SoundLayer);
           }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(settingBG);
        this.layer.add(backButton);
        this.layer.add(SoundLayer);
    }
}

