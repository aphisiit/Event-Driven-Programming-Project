package event.project.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

/**
 * Created by aphis on 28-May-16.
 */
public class OverScreen extends Screen {

    private ScreenStack ss;
    private ImageLayer overLayer;
    private ImageLayer homeLayer;
    private ImageLayer restartLayer;

    public OverScreen(final ScreenStack ss){
        System.out.println("THIS IS OVERSCREEN CLASS");
        this.ss = ss;

        Image overImage = assets().getImage("images/gameOver.png");
        overLayer = graphics().createImageLayer(overImage);

        Image homeImage = assets().getImage("images/homeButton.png");
        homeLayer = graphics().createImageLayer(homeImage);
        homeLayer.setTranslation(100,250);

        Image restartImage = assets().getImage("images/restartButton.png");
        restartLayer = graphics().createImageLayer(restartImage);
        restartLayer.setTranslation(350,250);

        homeLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new StartScreen(ss));
            }
        });
        restartLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new GameScreen(ss));

            }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(overLayer);
        this.layer.add(homeLayer);
        this.layer.add(restartLayer);
    }
}
