package dungeonforge.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GameConfig {
    private static GameConfig instance;
    public final Map<String, Object> settings = new LinkedHashMap<>();
    private GameConfig() {
        loadDefaults();
        loadFromClasspath("config.json");
    }

    public static synchronized GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }
    private void loadDefaults() {
        settings.put("playerStartingHp", 80.0);
        settings.put("playerStartingAttack",10.0);
        settings.put("playerStartingDefense",3.0);
        settings.put("carryCapacity",60.0);
        settings.put("dungeonDepth",3.0);
        settings.put("roomsPerLevel",8.0);
        settings.put("maxMonstersPerRoom",2.0);
        settings.put("seed",2026002.0);

    }
    private void loadFromClasspath(String resourceName) {
        try (InputStream in = GameConfig.class.getResourceAsStream("/data/" + resourceName)) {
            if (in == null) return;
            String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            settings.putAll(Json.parseObject(text));
        } catch (IOException | RuntimeException a) {
            System.err.println("[config] could not read " + resourceName + ": using defaults.");
        }
    }
    public int getInt(String key) {
        Object value = settings.get(key);
        return value instanceof Number ? ((Number)value).intValue() : 0;
    }
    public double getDouble(String key) {
        Object value = settings.get(key);
        return value instanceof Number ? ((Number)value).doubleValue() : 0.0D;
    }
    public long getSeed() {
        return (long)getDouble("seed");
    }
    public static void resetForTests() {instance = null;}

}