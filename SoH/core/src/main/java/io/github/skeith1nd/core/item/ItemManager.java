package io.github.skeith1nd.core.item;

import io.github.skeith1nd.core.game.AssetManager;
import playn.core.Image;

import java.util.HashMap;

public class ItemManager {
    private static ItemManager instance;
    private HashMap<Integer, Item> items;

    private ItemManager() {
        items = new HashMap<Integer, Item>();

        // Load item sprite sheet
        Image itemSpriteSheet = AssetManager.getInstance().getImages().get("images/items/items.png");
        int width = 24;
        int height = 24;

        // Load items
        Item dagger = new Item();
        dagger.setName("Dagger");
        dagger.setImage(itemSpriteSheet.subImage(6 * width, 9 * height, width, height));
        items.put(Item.DAGGER, dagger);

        Item sword = new Item();
        sword.setName("Sword");
        sword.setImage(itemSpriteSheet.subImage(3 * width, 9 * height, width, height));
        items.put(Item.SWORD, sword);

        Item bow = new Item();
        bow.setName("Bow");
        bow.setImage(itemSpriteSheet.subImage(5 * width, 9 * height, width, height));
        items.put(Item.BOW, bow);
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    public Item getItem(int item) {
        return items.get(item);
    }
}
