package physics2d.rigidbody;

import org.joml.Vector2f;

import components.Component;

public class Rigidbody2D extends Component {

    private Vector2f position = new Vector2f();
    private float rotation = 0.0f;

    public Vector2f getPosition() {

        return this.position;
    }

    public void setPosition(Vector2f position) {

        this.position = position;
    }

    public float getRotation() {

        return this.rotation;
    }

    public void setRotation(float rotation) {

        this.rotation = rotation;
    }
}
