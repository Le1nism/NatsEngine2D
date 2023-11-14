package natsuki;

import components.Sprite;
import components.SpriteRenderer;

public class Prefabs {
    
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {

        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);

        block.addComponent(renderer);

        return block;
    }
}
