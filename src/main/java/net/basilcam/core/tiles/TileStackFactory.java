package net.basilcam.core.tiles;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import org.jetbrains.annotations.TestOnly;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Stack;
import java.util.function.Consumer;

public enum TileStackFactory {
    ;
    public static final int MAX_TILE_ID = 24;

    public static Stack<Tile> createTileStack() {
        JsonTileConfig.JsonTileStack jsonTileStack = readStackFromJson();
        Stack<Tile> tileStack = convertJsonToTileStack(jsonTileStack);
        Collections.shuffle(tileStack);
        return tileStack;
    }

    @TestOnly
    public static Multimap<Integer, Tile> createTileMap() {
        JsonTileConfig.JsonTileStack jsonTileStack = readStackFromJson();
        return convertJsonToTileMap(jsonTileStack);
    }

    private static JsonTileConfig.JsonTileStack readStackFromJson() {
        Gson gson = new Gson();
        URL resource = TileStackFactory.class.getClassLoader().getResource(JsonTileConfig.TILES_FILE_NAME);
        assert resource != null;
        // todo: check for autocloseable
        try (Reader reader = new InputStreamReader(new FileInputStream(resource.getFile()), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, JsonTileConfig.JsonTileStack.class);
        } catch (IOException aE) {
            aE.printStackTrace();
            throw new RuntimeException("could not find tiles.json file");
        }
    }

    private static Stack<Tile> convertJsonToTileStack(JsonTileConfig.JsonTileStack jsonTileStack) {
        Stack<Tile> tileStack = new Stack<>();
        convertJson(jsonTileStack, tileStack::push);
        return tileStack;
    }

    private static Multimap<Integer, Tile> convertJsonToTileMap(JsonTileConfig.JsonTileStack jsonTileStack) {
        Multimap<Integer, Tile> tileMap = ArrayListMultimap.create();
        convertJson(jsonTileStack, (Tile tile) -> tileMap.put(tile.getId(), tile));
        return tileMap;
    }

    private static void convertJson(JsonTileConfig.JsonTileStack jsonTileStack, Consumer<Tile> tileConsumer) {
        for (JsonTileConfig.JsonTile jsonTile : jsonTileStack.tiles) {
            for (int count = 0; count < jsonTile.quantity; count++) {
                Tile.Builder builder = new Tile.Builder(jsonTile.id)
                        .withTop(JsonTileConfig.convertTypeNameToTileSection(jsonTile.topSection))
                        .withLeft(JsonTileConfig.convertTypeNameToTileSection(jsonTile.leftSection))
                        .withBottom(JsonTileConfig.convertTypeNameToTileSection(jsonTile.bottomSection))
                        .withRight(JsonTileConfig.convertTypeNameToTileSection(jsonTile.rightSection));
                for (String typeName : jsonTile.centerSections) {
                    builder.addCenter(JsonTileConfig.convertTypeNameToTileSection(typeName));
                }
                tileConsumer.accept(builder.build());
            }
        }
    }
}
