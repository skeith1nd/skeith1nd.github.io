package io.github.skeith1nd.game;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import io.github.skeith1nd.websocket.Server;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandProcessor implements Runnable {
    private static CommandProcessor instance;

    private ConcurrentLinkedQueue<Command> commands;
    private volatile Thread updateThread;

    private CommandProcessor() {
        commands = new ConcurrentLinkedQueue<Command>();
    }

    public static CommandProcessor getInstance() {
        if (instance == null) instance = new CommandProcessor();
        return instance;
    }

    public void start()
    {
        if (updateThread == null)
        {
            updateThread = new Thread(this);
            updateThread.start();
        }
    }

    public void stop(){
        updateThread = null;
    }

    @Override
    public void run(){
        Thread thisThread = Thread.currentThread();
        while(updateThread == thisThread){
            try{
                thisThread.sleep(30);
            } catch (Exception e){
                e.printStackTrace();
            }

            // Process commands
            int amountToProcess = commands.size(); // only pull initial amount off
            for (int i = 0; i < amountToProcess; i++) {
                Command command = commands.poll();
                switch(command.getType()) {
                    case Commands.PLAYER_MOVE_COMMAND:
                        PlayerMoveCommand playerMoveCommand = (PlayerMoveCommand)command;

                        // Validate the move (always true for now)
                        playerMoveCommand.setValidated(true);

                        // Send the move out to everyone else in the room
                        Server.getInstance().sendToAllInRoom(playerMoveCommand.getPlayer().getJSONObject("room").getString("roomId"), playerMoveCommand.serialize());
                        break;
                }
            }
        }
    }

    public ConcurrentLinkedQueue<Command> getCommands() {
        return commands;
    }

    public void setCommands(ConcurrentLinkedQueue<Command> commands) {
        this.commands = commands;
    }
}
