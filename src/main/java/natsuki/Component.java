package natsuki;

import java.lang.reflect.Field;

import imgui.ImGui;

public abstract class Component {
    
    public transient GameObject gameObject = null;

    public void start() {

        
    }

    public void update(float dt) {


    }

    public void imGui() {

        try {

            Field[] fields = this.getClass().getDeclaredFields();

            for (Field field : fields) {

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class) {

                    int val = (int)value;
                    int[] imInt = {val};

                    if (ImGui.dragInt(name + ": ", imInt))
                        field.set(this, imInt[0]);
                }
            }
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }
}
