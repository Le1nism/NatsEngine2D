package physics2d.forces;

import java.util.ArrayList;
import java.util.List;

import physics2d.rigidbody.Rigidbody2D;

public class ForceRegistry {

    private List<ForceRegistration> registry;

    public ForceRegistry() {

        this.registry = new ArrayList<>();
    }

    public void add(Rigidbody2D rb, ForceGenerator fg) {

        ForceRegistration fr = new ForceRegistration(fg, rb);
        registry.add(fr);
    }

    public void remove(Rigidbody2D rb, ForceGenerator fg) {

        ForceRegistration fr = new ForceRegistration(fg, rb);
        registry.remove(fr);
    }

    public void clear() {

        registry.clear();
    }

    public void updateForces(float dt) {

        for (ForceRegistration fr : registry)
            fr.fg.updateForce(fr.rb, dt);
    }

    public void zeroForces() {

        for (ForceRegistration fr : registry) {

            // IMPLEMENT ME
            //fr.rb.zeroForces();
        }
    }
}
