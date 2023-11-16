package physics2d.components;

import org.joml.Vector2f;

import components.Component;

public class Box2DCollider extends Component {

    private Vector2f halfSize = new Vector2f(1);

    public Vector2f getHalfSize() {

        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {

        this.halfSize = halfSize;
    }
}
