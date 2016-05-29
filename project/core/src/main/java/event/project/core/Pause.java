package event.project.core;

import static playn.core.PlayN.*;

import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

/**
 * Created by aphis on 24-Mar-16.
 */
public class Pause extends Screen{

    private final ScreenStack ss;
    private final ImageLayer pauseLayer;
    private final ImageLayer resumeLayer;
    private final ImageLayer restartLayer;
    private final ImageLayer homeLayer;
    private final GameScreen gameScreen;
    private final StartScreen startScreen;

    public Pause(final ScreenStack ss){
        this.ss = ss;

        startScreen = new StartScreen(ss);
        gameScreen = new GameScreen(ss);

        Image bg = assets().getImage("Images/pauseScreen.png");
        pauseLayer = graphics().createImageLayer(bg);

        Image backImage = assets().getImage("Images/resume.png");
        resumeLayer = graphics().createImageLayer(backImage);
        resumeLayer.setTranslation(280,205);

        resumeLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                GameScreen.pause = false;
            }
        });

        Image restart = assets().getImage("Images/restart.png");
        restartLayer = graphics().createImageLayer(restart);
        restartLayer.setTranslation(283,263);
        restartLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(gameScreen);
                GameScreen.pause = false;
            }
        });

        Image home = assets().getImage("Images/home.png");
        homeLayer = graphics().createImageLayer(home);
        homeLayer.setTranslation(289,324);
        homeLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(startScreen);
                GameScreen.pause = false;
            }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(pauseLayer);
        this.layer.add(resumeLayer);
        this.layer.add(restartLayer);
        this.layer.add(homeLayer);
    }
}

