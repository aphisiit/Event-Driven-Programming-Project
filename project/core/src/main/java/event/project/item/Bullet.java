package event.project.item;

import event.project.core.GameScreen;
import event.project.sprite.Sprite;
import event.project.sprite.SpriteLoader;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;

/**
 * Created by aphis on 02-Apr-16.
 */
public class Bullet {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private Body body;

    public enum State{
        IDLE
    }

    public State state = State.IDLE;
    public int e = 0;
    public int offset = 4;

    public Bullet(final World world,final float x,final float y){
        sprite = SpriteLoader.getSprite("images/Bullet.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite sprite) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x , y);

                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL * x,
                        GameScreen.M_PER_PIXEL * y);

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
    private Body initPhysicsBody(World world,float x,float y){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        GameScreen.bodies.put(body, "Boy");
        //GameScreen.count++;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.layer().width() * GameScreen.M_PER_PIXEL / 2,
                sprite.layer().height() * GameScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.75f;
        fixtureDef.restitution = 0.0f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);

        body.applyLinearImpulse(new Vec2(25f,-1.5f), body.getPosition());
        return body;
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

            }
/*            if(state == State.IDLE | state == State.IDLE)
                spriteIndex = offset + ((spriteIndex + 1) % 5);
                */
            spriteIndex = offset + ((spriteIndex + 1) % 4);

            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                body.getPosition().x / GameScreen.M_PER_PIXEL - 10,
                body.getPosition().y / GameScreen.M_PER_PIXEL
        );
    }

    public Body getBody() {
        return body;
    }
}
