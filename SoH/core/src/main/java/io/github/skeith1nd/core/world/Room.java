package io.github.skeith1nd.core.world;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.PlayN;
import playn.core.util.Callback;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
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
        PlayN.assets().getBytes("images/rooms/room" + roomId + "/room" + roomId + ".tmx",
                new Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] result) {
                        fillTiles(result);
                    }

                    @Override
                    public void onFailure(Throwable cause) {

                    }
                });
    }

    private void fillTiles(byte[] buffer) {
        try {
            // Load xml
            int imageWidth, imageHeight, tileWidth, tileHeight, firstGid, lastGid, mapWidth, mapHeight;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(buffer));

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }

            // Map node
            Node mapNode = document.getDocumentElement();
            mapWidth = Integer.parseInt(mapNode.getAttributes().getNamedItem("width").getNodeValue());
            mapHeight = Integer.parseInt(mapNode.getAttributes().getNamedItem("height").getNodeValue());
            tileWidth = Integer.parseInt(mapNode.getAttributes().getNamedItem("tilewidth").getNodeValue());
            tileHeight = Integer.parseInt(mapNode.getAttributes().getNamedItem("tileheight").getNodeValue());

            // Tileset node
            Node tilesetNode = mapNode.getChildNodes().item(0);
            firstGid = Integer.parseInt(tilesetNode.getAttributes().getNamedItem("firstgid").getNodeValue());

            // Image node
            Node imageNode = tilesetNode.getChildNodes().item(0);
            imageWidth = Integer.parseInt(imageNode.getAttributes().getNamedItem("width").getNodeValue());
            imageHeight = Integer.parseInt(imageNode.getAttributes().getNamedItem("height").getNodeValue());

            int tilesPerRow = imageWidth / tileWidth;

            // background layer node
            Node backgroundLayerData = mapNode.getChildNodes().item(1).getChildNodes().item(0);
            processLayerData(backgroundLayerData, World.getInstance().getBackground(), firstGid, tilesPerRow, tileWidth, tileHeight, mapWidth);

            // foreground layer node
            Node foregroundLayerData = mapNode.getChildNodes().item(2).getChildNodes().item(0);
            processLayerData(foregroundLayerData, World.getInstance().getForeground(), firstGid, tilesPerRow, tileWidth, tileHeight, mapWidth);

            // top layer node
            Node topLayerData = mapNode.getChildNodes().item(3).getChildNodes().item(0);
            processLayerData(topLayerData, World.getInstance().getTop(), firstGid, tilesPerRow, tileWidth, tileHeight, mapWidth);

            // Objects/Collision node
            Node collisionLayerData = mapNode.getChildNodes().item(4);
            processCollisionData(collisionLayerData, tilesPerRow, tileWidth, tileHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processLayerData(Node layerData, ImmediateLayer layer, int firstGid, int tilesPerRow, int tileWidth, int tileHeight, int mapWidth) {
        int drawX = 0, drawY = 0;

        for (int i = 0; i < layerData.getChildNodes().getLength(); i++) {
            int tileGid = Integer.parseInt(layerData.getChildNodes().item(i).getAttributes().getNamedItem("gid").getNodeValue());

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

    private void processCollisionData(Node layerData, int tilesPerRow, int tileWidth, int tileHeight) {
        for (int i = 0; i < layerData.getChildNodes().getLength(); i++) {
            Node object = layerData.getChildNodes().item(i);
            int x = Integer.parseInt(object.getAttributes().getNamedItem("x").getNodeValue());
            int y = Integer.parseInt(object.getAttributes().getNamedItem("y").getNodeValue());
            int width = Integer.parseInt(object.getAttributes().getNamedItem("width").getNodeValue());
            int height = Integer.parseInt(object.getAttributes().getNamedItem("height").getNodeValue());

            if (object.getChildNodes().getLength() > 0) {
                Node properties = object.getChildNodes().item(0);
                HashMap<String, String> propertyValues = new HashMap<String, String>();

                for (int j = 0; j < properties.getChildNodes().getLength(); j++) {
                    Node property = properties.getChildNodes().item(j);
                    propertyValues.put(property.getAttributes().getNamedItem("name").getNodeValue(),
                            property.getAttributes().getNamedItem("value").getNodeValue());
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
