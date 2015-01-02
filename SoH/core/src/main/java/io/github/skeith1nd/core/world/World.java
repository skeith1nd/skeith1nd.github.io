package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.Surface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import static playn.core.PlayN.graphics;

public class World {
    private static World instance;
    private HashMap<String, Room> rooms;
    private ImmediateLayer background, foreground, top;
    private Image terrainTileSheet;
    private TreeSet<InteractableObject> roomObjects;

    private World() {
        rooms = new HashMap<String, Room>();
        roomObjects = new TreeSet<InteractableObject>(new Comparator<InteractableObject>() {
            @Override
            public int compare(InteractableObject o1, InteractableObject o2) {
                if (o1.getY() < o2.getY()) return -1;
                else if (o1.getY() == o2.getY()) return 0;
                else return 1;
            }
        });

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
            ArrayList<Tile> tiles = currentRoom.getTiles().get(foreground);
            for (Tile tile : tiles) {
                surface.drawImage(tile.getImage(), tile.getX(), tile.getY());
            }

            boolean drawnPlayer = false;
            for (InteractableObject object : roomObjects) {
                if (!drawnPlayer && Player.getInstance().getY() < object.getY()) {
                    System.out.println("behind");
                    // Draw player
                    surface.drawImage(Player.getInstance().getCurrentImage(),
                            Player.getInstance().getX() - Player.getInstance().getWidth() / 2,
                            Player.getInstance().getY() - Player.getInstance().getHeight());
                    drawnPlayer = true;
                }
                surface.drawImage(object.getImage(),
                        object.getX() - object.getWidth() / 2,
                        object.getY() - object.getHeight());
            }

            if (Player.getInstance().isLoaded()) {
                // Draw other players in the room
                for (ClientPlayer player : Player.getInstance().getRoom().getPlayers().values()) {
                    if (!player.getUserId().equals(Player.getInstance().getUserId()) && player.isLoaded())
                        surface.drawImage(player.getCurrentImage(), player.getX(), player.getY());
                }

                if (!drawnPlayer) {
                    // Draw player
                    surface.drawImage(Player.getInstance().getCurrentImage(),
                            Player.getInstance().getX() - Player.getInstance().getWidth() / 2,
                            Player.getInstance().getY() - Player.getInstance().getHeight());
                }
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

    public TreeSet<InteractableObject> getRoomObjects() {
        return roomObjects;
    }

    public void setRoomObjects(TreeSet<InteractableObject> roomObjects) {
        this.roomObjects = roomObjects;
    }
}
