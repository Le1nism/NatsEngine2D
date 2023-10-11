package scenes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.Component;
import components.ComponentDeserializer;
import imgui.ImGui;
import natsuki.Camera;
import natsuki.GameObject;
import natsuki.GameObjectDeserializer;
import renderer.Renderer;

public abstract class Scene {
    
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

    public Scene() {


    }

    public void init() {

        
    }

    public void start() {

        for (GameObject go : gameObjects) {

            go.start();
            this.renderer.add(go);
        }

        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {

        if (!isRunning) {

            gameObjects.add(go);
        } else {

            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);
    public abstract void render();

    public Camera camera() {

        return this.camera;
    }

    public void sceneImGui() {

        if (activeGameObject != null) {

            ImGui.begin("Inspector");
            activeGameObject.imGui();
            ImGui.end();
        }

        imGui();
    }

    public void imGui() {


    }

    public void saveExit() {

        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer()).registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).create();

        try {
            FileWriter writer = new FileWriter("level.nat");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {

        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer()).registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.nat")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {

            int maxGoID = -1;
            int maxCompID = -1;

            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);

            for (GameObject obj : objs) {

                addGameObjectToScene(obj);

                for (Component c : obj.getAllComponents()) {

                    if (c.getUID() > maxCompID)
                        maxCompID = c.getUID();

                    if (obj.getUID() > maxGoID)
                        maxGoID = obj.getUID();
                }
            }

            maxGoID++;
            maxCompID++;

            System.out.println(maxGoID);
            System.out.println(maxCompID);

            GameObject.init(maxGoID);
            Component.init(maxCompID);

            this.levelLoaded = true;            
        }
    }
}
