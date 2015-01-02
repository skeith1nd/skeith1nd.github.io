package io.github.skeith1nd.core.world;

import io.github.skeith1nd.network.core.xml.XmlDoc;
import io.github.skeith1nd.network.core.xml.XmlParser;
import io.github.skeith1nd.network.core.xml.XmlTag;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.PlayN;
import playn.core.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;


public class Room {
    private HashMap<ImmediateLayer, ArrayList<Tile>> tiles;
    private String roomId;

    public Room(String roomId) {
        tiles = new HashMap<ImmediateLayer, ArrayList<Tile>>();
        tiles.put(World.getInstance().getBackground(), new ArrayList<Tile>());
        tiles.put(World.getInstance().getForeground(), new ArrayList<Tile>());
        tiles.put(World.getInstance().getTop(), new ArrayList<Tile>());

        this.roomId = roomId;

        init();
    }

    public void init() {
        PlayN.assets().getText("images/rooms/room" + roomId + "/room" + roomId + ".tmx",
                new Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        fillTiles(result);
                    }

                    @Override
                    public void onFailure(Throwable cause) {

                    }
                }
        );
    }

    private void fillTiles(String buffer) {
        try {
            // Load xml
            int imageWidth, imageHeight, tileWidth, tileHeight, firstGid, lastGid, mapWidth, mapHeight;

            XmlDoc xmlDoc = XmlParser.parseXml(buffer);

            // Map Node
            XmlTag mapNode = xmlDoc.root;
            mapWidth = Integer.parseInt(mapNode.attributes.get("width"));
            mapHeight = Integer.parseInt(mapNode.attributes.get("height"));
            tileWidth = Integer.parseInt(mapNode.attributes.get("tilewidth"));
            tileHeight = Integer.parseInt(mapNode.attributes.get("tileheight"));

            // TileSet Node
            XmlTag tileSetNode = mapNode.children.get(0);
            firstGid = Integer.parseInt(tileSetNode.attributes.get("firstgid"));

            // Image Node
            XmlTag imageNode = tileSetNode.children.get(0);
            imageWidth = Integer.parseInt(imageNode.attributes.get("width"));
            imageHeight = Integer.parseInt(imageNode.attributes.get("height"));

            int tilesPerRow = imageWidth / tileWidth;

            // Background Layer Node
            XmlTag backgroundLayerData = mapNode.children.get(1).children.get(0);
            processLayerData(backgroundLayerData, World.getInstance().getBackground(), firstGid, tilesPerRow, tileWidth, tileHeight, mapWidth);

            // Foreground Layer Node
            XmlTag foregroundLayerData = mapNode.children.get(2).children.get(0);
            processLayerData(foregroundLayerData, World.getInstance().getForeground(), firstGid, tilesPerRow, tileWidth, tileHeight, mapWidth);

            // Top Layer Node
            XmlTag topLayerData = mapNode.children.get(3).children.get(0);
            processLayerData(topLayerData, World.getInstance().getTop(), firstGid, tilesPerRow, tileWidth, tileHeight, mapWidth);

            // Objects/Collision node
            XmlTag collisionLayerData = mapNode.children.get(4);
            processCollisionData(collisionLayerData, tilesPerRow, tileWidth, tileHeight);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLayerData(XmlTag layerData, ImmediateLayer layer, int firstGid, int tilesPerRow, int tileWidth, int tileHeight, int mapWidth) {
        int drawX = 0, drawY = 0;

        for (int i = 0; i < layerData.children.size(); i++) {
            int tileGid = Integer.parseInt(layerData.children.get(i).attributes.get("gid"));

            if (tileGid != 0) {
                int tileRow = (tileGid - firstGid) / tilesPerRow;
                int tileCol = (tileGid - firstGid) % tilesPerRow;

                int imageX = tileCol * tileWidth;
                int imageY = tileRow * tileHeight;

                Image tileImage = World.getInstance().getTerrainTileSheet().subImage(imageX, imageY, tileWidth, tileHeight);
                Tile tile = new Tile();
                tile.setX(drawX);
                tile.setY(drawY);
                tile.setImage(tileImage);
                tiles.get(layer).add(tile);
            }

            drawX += tileWidth;
            if (drawX >= (mapWidth * tileWidth)) {
                drawX = 0;
                drawY += tileHeight;
            }
        }
    }

    private void processCollisionData(XmlTag layerData, int tilesPerRow, int tileWidth, int tileHeight) {
        for (int i = 0; i < layerData.children.size(); i++) {
            XmlTag object = layerData.children.get(i);
            int x = Integer.parseInt(object.attributes.get("x"));
            int y = Integer.parseInt(object.attributes.get("y"));
            int width = Integer.parseInt(object.attributes.get("width"));
            int height = Integer.parseInt(object.attributes.get("height"));

            if (object.children.size() > 0) {
                XmlTag properties = object.children.get(0);
                HashMap<String, String> propertyValues = new HashMap<String, String>();

                for (int j = 0; j < properties.children.size(); j++) {
                    XmlTag property = properties.children.get(j);
                    propertyValues.put(property.attributes.get("name"), property.attributes.get("value"));
                }

                if (propertyValues.get("type") != null) {
                    if (propertyValues.get("type").equals("renderable")) {
                        InteractableObject interactableObject = new InteractableObject();
                        interactableObject.setX(x);
                        interactableObject.setY(y);
                        interactableObject.setCollisionWidth(width);
                        interactableObject.setCollisionHeight(height);

                        String[] dimensions = propertyValues.get("dimensions").split(",");
                        interactableObject.setTilesWide(Integer.parseInt(dimensions[0]));
                        interactableObject.setTilesTall(Integer.parseInt(dimensions[1]));
                        interactableObject.setTileGid(Integer.parseInt(propertyValues.get("tile")));
                        interactableObject.init(tilesPerRow, tileWidth, tileHeight);

                        World.getInstance().getRoomObjects().add(interactableObject);
                    }
                }
            }
        }
    }

    public HashMap<ImmediateLayer, ArrayList<Tile>> getTiles() {
        return tiles;
    }

    public void setTiles(HashMap<ImmediateLayer, ArrayList<Tile>> tiles) {
        this.tiles = tiles;
    }
}
