package event.project.core;

import static playn.core.PlayN.*;

import event.project.characters.Black;
import event.project.characters.Zealot;
import event.project.characters.Zombie;
import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import playn.core.Mouse;

/**
 * Created by Administrator on 23/3/2559.
 */
public class TestScreen extends Screen {
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;
    private final Zombie zombie;
//    private Black zealot;
//    private final Image bgImage;
//    private final Image backImage;

//    private Root root;

    public TestScreen(final ScreenStack ss){
        this.ss = ss;

        Image bgImage = assets().getImage("images/bg.png");
        bg = graphics().createImageLayer(bgImage);

        Image backImage= assets().getImage("images/back_button.png");
        backButton = graphics().createImageLayer(backImage);

        backButton.setTranslation(10,10);
        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top()); // <- Pop Screen
            }
        });

        zombie = new Zombie(100f,100f);
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(backButton);
        this.layer.add(zombie.layer());
    }
    public void update(int delta){
        super.update(delta);
        zombie.update(delta);
    }
}
