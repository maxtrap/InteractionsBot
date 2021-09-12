package controller;

import eventhandling.EventHandler;
import eventhandling.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import tictactoe.eventhandling.TicTacToeEventHandler;

import javax.security.auth.login.LoginException;
import java.util.LinkedList;
import java.util.List;

public class BotController {

    private static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault("ODg2MzQ2MDk4MzcxODU4NTE0.YT0QIQ.zq1J2DR5KzlsnA4pD7odj4DHGyU")
                .addEventListeners(new EventListener(new BotController()))
                .build();
        jda.awaitReady();

//        createCommands();
    }

    private static void createCommands() {
        getTestServer()
                .updateCommands()
                .addCommands(
                        new CommandData("tictactoe", "Play tic tac toe")
                )
                .queue();
    }

    public static JDA getJDA() {
        return jda;
    }

    public static Guild getTestServer() {
        return jda.getGuildById(698782376754413588L);
    }



    private final List<EventHandler> gameHandlers;

    private BotController() {
        gameHandlers = new LinkedList<>();
        gameHandlers.add(new TicTacToeEventHandler());
    }

    //This returns the reference to the internal game handlers list, and as a result, it should never be modified
    public List<EventHandler> getGameHandlers() {
        return gameHandlers;
    }

}
