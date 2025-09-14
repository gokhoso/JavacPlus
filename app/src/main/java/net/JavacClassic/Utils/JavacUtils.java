package net.JavacClassic.Utils;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class JavacUtils {

    private final Dotenv dotenv = Dotenv.load();

    public ShardManager buildShard() {
        var manager = DefaultShardManagerBuilder.createDefault(dotenv.get("TOKEN"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL);

        return manager.build();
    }
}
