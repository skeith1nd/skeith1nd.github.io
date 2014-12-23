package io.github.skeith1nd.core.game;

import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
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

                        if (playerMoveCommand.getPlayer().getString("userId").equals(Player.getInstance().getUserId())) {
                            // If invalid, snap player back to last valid position
                            if (!playerMoveCommand.isValidated()) {
                                Player.getInstance().setX(playerMoveCommand.getPlayer().getInt("x"));
                                Player.getInstance().setY(playerMoveCommand.getPlayer().getInt("y"));
                            }
                        } else {
                            // If valid, send move command to client player
                            if (playerMoveCommand.isValidated()) {
                                String userId = playerMoveCommand.getPlayer().getString("userId");
                                ClientPlayer clientPlayer = Player.getInstance().getRoom().getPlayers().get(userId);

                                switch (playerMoveCommand.getDirection()) {
                                    case PlayerMoveCommand.MOVE_UP:
                                        clientPlayer.moveUp();
                                        break;
                                    case PlayerMoveCommand.MOVE_LEFT:
                                        clientPlayer.moveLeft();
                                        break;
                                    case PlayerMoveCommand.MOVE_DOWN:
                                        clientPlayer.moveDown();
                                        break;
                                    case PlayerMoveCommand.MOVE_RIGHT:
                                        clientPlayer.moveRight();
                                        break;
                                }
                            }
                        }

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
