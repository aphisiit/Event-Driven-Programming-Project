package event.project.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

/**
 * Created by aphis on 18-May-16.
 */
public class SelectScreen extends Screen {
    private ScreenStack ss;
    private ImageLayer bgLayer;
    private ImageLayer backLayer;
    private ImageLayer selectLayer1;
    private ImageLayer selectLayer2;


    public SelectScreen(final ScreenStack ss){
        this.ss = ss;

        Image bg = assets().getImage("images/SelectScreen.png");
        bgLayer = graphics().createImageLayer(bg);

        Image backButtom = assets().getImage("images/back_button.png");
        backLayer = graphics().createImageLayer(backButtom);
        backLayer.setTranslation(10,10);

        Image select1 = assets().getImage("images/SelectButton.png");
        selectLayer1 = graphics().createImageLayer(select1);
        selectLayer1.setTranslation(100,350);

        Image select2 = assets().getImage("images/SelectButton.png");
        selectLayer2 = graphics().createImageLayer(select2);
        selectLayer2.setTranslation(350,350);


        selectLayer1.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new GameScreen(ss));
            }
        });
        selectLayer2.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new GameScreen(ss));
            }
        });
        backLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });

    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backLayer);
        this.layer.add(selectLayer1);
        this.layer.add(selectLayer2);
    }
}
