package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.util.UpdateableTreeSet;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.Surface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import static playn.core.PlayN.graphics;

public class World {
    private static World instance;
    private HashMap<String, Room> rooms;
    private ImmediateLayer background, foreground, top;
    private Image terrainTileSheet;

    private World() {
        rooms = new HashMap<String, Room>();

        // Background layer
        background = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            public void render(Surface surface) {
                World.getInstance().paintBackgroundLayer(surface);
            }
        });
        graphics().rootLayer().add(background);

        // Foreground layer
        foreground = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            public void render(Surface surface) {
                World.getInstance().paintForegroundLayer(surface);
            }
        });
        graphics().rootLayer().add(foreground);

        // Top layer
        top = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            public void render(Surface surface) {
                World.getInstance().paintTopLayer(surface);
            }
        });
        graphics().rootLayer().add(top);
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    public void init() {
        // Load terrain tile sheet
        terrainTileSheet = AssetManager.getInstance().getImages().get("images/rooms/RPG_Maker_VX_RTP_Tileset_by_telles0808.png");

        // Create rooms
        rooms.put("1", new Room("1"));
    }

    public void paintBackgroundLayer(Surface surface) {
        // Generally the terrain
        if (Player.getInstance().getRoom() != null) {
            Room currentRoom = rooms.get(Player.getInstance().getRoom().getRoomId());
            ArrayList<Tile> tiles = currentRoom.getTiles().get(background);
            for (Tile tile : tiles) {
                surface.drawImage(tile.getImage(), tile.getX(), tile.getY());
            }
        }
    }

    public void paintForegroundLayer(Surface surface) {
        if (Player.getInstance().getRoom() != null) {
            Room currentRoom = rooms.get(Player.getInstance().getRoom().getRoomId());

            // Update room objects position
            currentRoom.getRenderedObjects().updateAll();

            // Generally objects that have no depth
            ArrayList<Tile> tiles = currentRoom.getTiles().get(foreground);
            for (Tile tile : tiles) {
                surface.drawImage(tile.getImage(), tile.getX(), tile.getY());
            }

            for (RenderedObject object : currentRoom.getRenderedObjects()) {
                surface.drawImage(object.getImage(),
                        object.getX() - object.getWidth() / 2,
                        object.getY() - object.getHeight());
            }
        }
    }

    public void paintTopLayer(Surface surface) {
        if (Player.getInstance().getRoom() != null) {
            Room currentRoom = rooms.get(Player.getInstance().getRoom().getRoomId());
            ArrayList<Tile> tiles = currentRoom.getTiles().get(top);
            for (Tile tile : tiles) {
                surface.drawImage(tile.getImage(), tile.getX(), tile.getY());
            }
        }
    }

    public ImmediateLayer getBackground() {
        return background;
    }

    public void setBackground(ImmediateLayer background) {
        this.background = background;
    }

    public ImmediateLayer getForeground() {
        return foreground;
    }

    public void setForeground(ImmediateLayer foreground) {
        this.foreground = foreground;
    }

    public ImmediateLayer getTop() {
        return top;
    }

    public void setTop(ImmediateLayer top) {
        this.top = top;
    }

    public Image getTerrainTileSheet() {
        return terrainTileSheet;
    }

    public void setTerrainTileSheet(Image terrainTileSheet) {
        this.terrainTileSheet = terrainTileSheet;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, Room> rooms) {
        this.rooms = rooms;
    }
}
