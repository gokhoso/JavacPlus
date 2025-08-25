package javacplus;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import javacplus.Handlers.Command.CommandHandler;
import javacplus.Handlers.Component.ComponentHandler;
import javacplus.Handlers.Database.DatabaseHandler;
import javacplus.Handlers.Security.SpamProtect;
import javacplus.Listeners.ListenerComponent;
import javacplus.Listeners.ListenerMember;
import javacplus.Listeners.ListenerMessage;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Javac {
    private static final Logger logger = LoggerFactory.getLogger(Javac.class);
    public static Javac instance;
    private static ShardManager shardManager;
    private final SpamProtect spamProtect = new SpamProtect();
    private final DatabaseHandler mainDatabase = new DatabaseHandler();
    private final CommandHandler commandHandler = new CommandHandler();
    private final ComponentHandler componentHandler = new ComponentHandler();

    public static void main(String[] args) {
        System.out.println("Hello... :3\n");
        try {
            (instance = new Javac()).javacMain();
        } catch (Exception e) {
            logger.error("Failed while executing javacMain: {}", e);
        }

    }

    private ShardManager createShardManager(Dotenv dotenv) {
        var manager = DefaultShardManagerBuilder
                .createDefault(dotenv.get("TOKEN"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT);
        return manager.build();
    }

    public static void addEventListener(@Nonnull Object... listeners) {
        if (shardManager == null) {
            logger.error("ShardManager is not initialized. Cannot add event listeners.");
            return;
        }
        shardManager.addEventListener(listeners);
    }

    private void javacMain() throws Exception {
        Dotenv dotenv = Dotenv.load();
        shardManager = createShardManager(dotenv);

        addEventListener(new ListenerMessage(commandHandler, componentHandler, mainDatabase, spamProtect));
        addEventListener(new ListenerMember(mainDatabase));
        addEventListener(new ListenerComponent(componentHandler, mainDatabase));
    }

}