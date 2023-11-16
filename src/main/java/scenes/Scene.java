package scenes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import components.Component;
import components.ComponentDeserializer;

import natsuki.Camera;
import natsuki.GameObject;
import natsuki.GameObjectDeserializer;
import natsuki.Transform;
import physics2d.Physics2D;
import renderer.Renderer;

public class Scene {
    
    private Renderer renderer = new Renderer();
    private Camera camera;
    private boolean isRunning = false;
    private List<GameObject> gameObjects = new ArrayList<>();
    private boolean levelLoaded = false;
    private Physics2D physics2D;

    private SceneInitializer sceneInitializer;

    public Scene(SceneInitializer sceneInitializer) {

        this.sceneInitializer = sceneInitializer;
    }

    public void init() {

        this.camera = new Camera(new Vector2f(-250, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    public void start() {

        for (int i = 0; i < gameObjects.size(); i++) {

            GameObject go = gameObjects.get(i);
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

    public void destroy() {

        for (GameObject go : gameObjects)
            go.destroy();
    }

    public List<GameObject> getGameObjects() {

        return this.gameObjects;
    }

    public GameObject getGameObject(int gameObjectID) {

        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getUID() == gameObjectID).findFirst();

        return result.orElse(null);
    }

    public void update(float dt) {

        this.camera.adjustProjection();

        for (int i = 0; i < gameObjects.size(); i++) {

            GameObject go = gameObjects.get(i);
            go.update(dt);

            if (go.isDead()) {

                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }

    public void render() {

        this.renderer.render();
    }

    public Camera camera() {

        return this.camera;
    }

    public void imGui() {

        this.sceneInitializer.imGui();
    }

    public GameObject createGameObject(String name) {

        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);

        return go;
    }

    public void saveExit() {

        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer()).registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).create();

        try {
            FileWriter writer = new FileWriter("level.nat");

            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) 
                if (obj.doSerialization()) objsToSerialize.add(obj);

            writer.write(gson.toJson(objsToSerialize));
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
