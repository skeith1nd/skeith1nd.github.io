package io.github.skeith1nd.core.game;

import io.github.skeith1nd.core.SoH;
import playn.core.AssetWatcher;
import playn.core.Image;
import playn.core.PlayN;

import java.util.HashMap;

public class AssetManager {
    private static AssetManager instance;

    private HashMap<String, Image> images;
    private AssetWatcher assetWatcher;
    private SoH game;

    private AssetManager() {
        images = new HashMap<String, Image>();

        assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
            @Override
            public void done() {
                game.start();
            }

            @Override
            public void error(Throwable e) {

            }
        });
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public void init(SoH game) {
        this.game = game;

        // Load all terrain tile sheets
        String terrain1TileSheet = "images/rooms/RPG_Maker_VX_RTP_Tileset_by_telles0808.png";
        Image terrainTileSheet1 = PlayN.assets().getImage(terrain1TileSheet);
        images.put(terrain1TileSheet, terrainTileSheet1);
        assetWatcher.add(terrainTileSheet1);

        // Load all character tile sheets
        String man1SpriteSheet = "images/characters/man1/man1.png";
        Image characterSpriteSheet = PlayN.assets().getImage(man1SpriteSheet);
        images.put(man1SpriteSheet, characterSpriteSheet);
        assetWatcher.add(characterSpriteSheet);

        // Load all item sprite sheets
        String itemsSpriteSheet = "images/items/items.png";
        Image itemsSpriteSheet1 = PlayN.assets().getImage(itemsSpriteSheet);
        images.put(itemsSpriteSheet, itemsSpriteSheet1);
        assetWatcher.add(itemsSpriteSheet1);

        // Load mouse cursors
        String blackArrowCursor = "images/black_arrow.png";
        Image blackArrowImage = PlayN.assets().getImage(blackArrowCursor);
        images.put(blackArrowCursor, blackArrowImage);
        assetWatcher.add(blackArrowImage);

        // Start the asset watcher
        assetWatcher.start();
    }

    public HashMap<String, Image> getImages() {
        return images;
    }

    public void setImages(HashMap<String, Image> images) {
        this.images = images;
    }
}
