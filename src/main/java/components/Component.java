package components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import editor.JImGui;
import imgui.ImGui;
import natsuki.GameObject;

public abstract class Component {

    private static int idCounter = 0;
    private int uid = -1;
    
    public transient GameObject gameObject = null;

    public void start() {

        
    }

    public void update(float dt) {


    }

    public void imGui() {

        try {

            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields) {

                boolean isTransient = Modifier.isTransient(field.getModifiers());
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());

                if (isPrivate)
                    field.setAccessible(true);
                
                if (isTransient) continue;

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class) {

                    int val = (int)value;
                    field.set(this, JImGui.dragInt(name, val));
                }
                else if (type == float.class) {

                    float val = (float)value;
                    field.set(this, JImGui.dragFloat(name, val));
                }
                else if (type == boolean.class) {

                    boolean val = (boolean)value;

                    if (ImGui.checkbox(name + ": ", val))
                        field.set(this, !val);
                }
                else if (type == Vector3f.class) {

                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};

                    if (ImGui.dragFloat3(name + ": ", imVec))
                        val.set(imVec[0], imVec[1], imVec[2]);
                }
                else if (type == Vector4f.class) {

                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};

                    if (ImGui.dragFloat4(name + ": ", imVec))
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                }
                else if (type == Vector2f.class) {

                    Vector2f val = (Vector2f)value;
                    JImGui.drawVec2Control(name, val);
                }

                if (isPrivate)
                    field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }

    public void generateID() {

        if (this.uid == -1)
            this.uid = idCounter++;
    }

    public void destroy() {

        
    }

    public int getUID() {

        return this.uid;
    }

    public static void init(int maxID) {

        idCounter = maxID;
    }
}
