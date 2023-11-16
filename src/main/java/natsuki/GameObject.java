package natsuki;

import java.util.ArrayList;
import java.util.List;

import components.Component;
import imgui.ImGui;

public class GameObject {

    private static int idCounter = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean isDead = false;

    public GameObject(String name) {

        this.name = name;
        this.components = new ArrayList<>();

        this.uid = idCounter++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {

        for (Component c : components) {

            if (componentClass.isAssignableFrom(c.getClass())) {

                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {

                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }
        
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {

        for (int i = 0; i < components.size(); i++) {

            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {

                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {

        c.generateID();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {

        for (Component component : components) component.update(dt);
    }

    public void start() {

        for (int i = 0; i < components.size(); i++)
            components.get(i).start();
    }

    public void imGui() {

        for (Component c : components) {

            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
            c.imGui();
        }
    }

    public void destroy() {

        this.isDead = true;
        for (int i = 0; i < components.size(); i++)
            components.get(i).destroy();
    }

    public boolean isDead() {

        return this.isDead;
    }

    public static void init(int maxID) {

        idCounter = maxID;
    }

    public int getUID() {

        return this.uid;
    }

    public List<Component> getAllComponents() {

        return this.components;
    }

    public void setNoSerialize() {

        this.doSerialization = false;
    }

    public boolean doSerialization() {

        return this.doSerialization;
    }
}
