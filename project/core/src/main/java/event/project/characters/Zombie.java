package event.project.characters;

import event.project.sprite.Sprite;
import event.project.sprite.SpriteLoader;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;

/**
 * Created by aphis on 02-Apr-16.
 */
public class Zombie {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x2;

    public enum State{
        IDLE,WALK,DIE
    }
    private State state = State.IDLE;
    public int e = 0;
    public int offset = 4;

    public Zombie(final float x,final float y){
        x2 = x;
        PlayN.keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key() == Key.SPACE){
//                    state = State.WALK;
                    switch (state){
                        case IDLE: state = State.WALK; break;
                        case WALK: state = State.DIE; break;
                        case DIE: state = State.IDLE; break;
                    }
                }
            }

            @Override
            public void onKeyDown(Keyboard.Event event) {
                if(event.key() == Key.RIGHT){
                    state = State.WALK;
                    x2 += 1f;
                    sprite.layer().setTranslation(x2,y);
                }
            }
        });
        sprite = SpriteLoader.getSprite("images/zombie.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite sprite) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x , y);
                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!",cause);
            }
        });
    }
    public Layer layer(){
        return sprite.layer();
    }
    public void update(int delta){
        if(hasLoaded == false){
            return;
        }
        e = e + delta;
        if (e > 150) {
            switch (state) {
                case IDLE:
                    offset = 0;
                    break;
                case WALK:
                    offset = 4;
                    break;
                case DIE:
                    offset = 8;
//                    if (spriteIndex == 10) {
//                        state = State.IDLE;                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }
}
