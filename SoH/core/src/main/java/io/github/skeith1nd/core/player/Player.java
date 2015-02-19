package io.github.skeith1nd.core.player;

import io.github.skeith1nd.core.network.Client;
import io.github.skeith1nd.core.world.*;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import playn.core.*;

public class Player extends PlayerEntity {
    private static Player instance;
    private ClientRoom room;

    private Player() {}

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void fromJSON(Json.Object playerJSON, Json.Object roomJSON) {
        super.fromJSON(playerJSON);

        // Room
        room = new ClientRoom();
        room.fromJSON(roomJSON);
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = super.toJSON();

        if (room != null) {
            jsonObject.put("roomId", room.getRoomId());
        }

        return jsonObject;
    }

    public void init() {
        super.init();

        // Add to world
        if (room != null) {
            World.getInstance().getRooms().get(room.getRoomId()).getRenderedObjects().add(this);
        }
    }

    public void moveUp(){
        super.moveUp();

        if (!checkForCollision(y - 3, PlayerMoveCommand.MOVE_UP)) {
            y -= 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_UP);
        }
        resting = false;
    }

    public void moveLeft(){
        super.moveLeft();

        if (!checkForCollision(x - 3, PlayerMoveCommand.MOVE_LEFT)) {
            x -= 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_LEFT);
        }
        resting = false;
    }

    public void moveDown(){
        super.moveDown();

        if (!checkForCollision(y + 3, PlayerMoveCommand.MOVE_DOWN)) {
            y += 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_DOWN);
        }
        resting = false;
    }

    public void moveRight(){
        super.moveRight();

        if (!checkForCollision(x + 3, PlayerMoveCommand.MOVE_RIGHT)) {
            x += 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_RIGHT);
        }
        resting = false;
    }

    public void rest() {
        super.rest();

        // Send move command to server if the last move wasn't resting
        if (!resting) {
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.REST);
            resting = true;
        }
    }

    private boolean checkForCollision(int target, int direction) {
        int newX = x, newY = y;
        switch (direction) {
            case PlayerMoveCommand.MOVE_RIGHT:
                newX = target;
                break;
            case PlayerMoveCommand.MOVE_UP:
                newY = target;
                break;
            case PlayerMoveCommand.MOVE_DOWN:
                newY = target;
                break;
            case PlayerMoveCommand.MOVE_LEFT:
                newX = target;
                break;
        }

        // Loop through objects in room and check for collision
        for (CollisionObject object : World.getInstance().getRooms().get(room.getRoomId()).getCollisionObjects()) {
            CollisionRectangle playerRect = new CollisionRectangle(newX - collisionWidth / 2, newY - collisionHeight / 2, collisionWidth, collisionHeight);
            CollisionRectangle objectRect = new CollisionRectangle(object.getX() - object.getCollisionWidth() / 2, object.getY() - object.getCollisionHeight() / 2, object.getCollisionWidth(), object.getCollisionHeight());

            if (playerRect.intersects(objectRect)) {
                return true;
            }
        }
        return false;
    }

    public ClientRoom getRoom() {
        return room;
    }

    public void setRoom(ClientRoom room) {
        this.room = room;
    }
}
