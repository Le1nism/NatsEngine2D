package physics2dtmp.primitives;

import org.joml.Vector2f;

import physics2dtmp.rigidbody.Rigidbody2D;

// Axis Aligned Bounding Box
public class AABB {

    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();

    private Rigidbody2D rigidbody = null;

    public AABB() {

        this.halfSize = new Vector2f(size).div(2.0f); 
    }

    public AABB(Vector2f min, Vector2f max) {

        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2.0f);
    }

    public Vector2f getMin() {

        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax() {

        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }

    public void setRigidbody(Rigidbody2D rigidbody) {

        this.rigidbody = rigidbody;
    }

    public void setSize(Vector2f size) {

        this.size.set(size);
        this.halfSize.set(size.x / 2.0f, size.y / 2.0f);
    }
}
