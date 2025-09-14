package net.JavacClassic;

import net.JavacClassic.Cache.CCommand;
import net.JavacClassic.Cache.CService;
import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Handlers.Command.CommandMain;
import net.JavacClassic.Handlers.Component.ComponentMain;
import net.JavacClassic.Listeners.LMember;
import net.JavacClassic.Listeners.LMessage;
import net.JavacClassic.Listeners.LReady;
import net.JavacClassic.Security.CooldownManager;
import net.JavacClassic.Services.Count;
import net.JavacClassic.Services.DeleteCache;
import net.JavacClassic.Utils.JavacUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Javac {

    private static final Logger logger = LoggerFactory.getLogger(Javac.class);

    private final CooldownManager cooldownManager = new CooldownManager();
    private final ComponentMain componentMain = new ComponentMain();
    private final JavacUtils javacUtils = new JavacUtils();

    private final CUser cUser = new CUser();
    private final CService cService = new CService();
    private final CCommand cCommand = new CCommand();

    public static void main(String[] args) {
        try {
            logger.info("Starting Javac Base");
            new Javac().run();
        } catch (Exception e) {
            logger.error("Error while running bot: ", e);
        }
    }

    public void run() throws InterruptedException {
        int pool_size = Config.data.config.service_pool;
        CommandMain commandMain = new CommandMain(cCommand, cooldownManager);

        ServiceDaemon serviceDaemon = new ServiceDaemon(pool_size, cService);
        ShardManager sManager = javacUtils.buildShard();

        sManager.getShards().getFirst().getSelfUser().getJDA().awaitReady();

        sManager.addEventListener(new LMember());
        sManager.addEventListener(new LReady());
        sManager.addEventListener(new LMessage(cUser, commandMain, componentMain));

        serviceDaemon.addService("deletecache", new DeleteCache(cUser));
        serviceDaemon.start("deletecache");

        serviceDaemon.addService("count", new Count(sManager.getGuildById(Config.data.bot.guild_id)));
        serviceDaemon.start("count");
    }
}
