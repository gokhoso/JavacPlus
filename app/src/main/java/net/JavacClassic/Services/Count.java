package net.JavacClassic.Services;

import net.JavacClassic.Config;
import net.JavacClassic.Interfaces.IService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Count implements IService {
    private final Logger logger = LoggerFactory.getLogger(Count.class);
    private final Guild guild;

    public Count(Guild guild) {
        this.guild = guild;
    }

    @Override
    public Runnable service() {
        VoiceChannel channel = guild.getVoiceChannelById(Config.data.channels.count);

        return new Runnable() {
            @Override
            public void run() {
                logger.debug("Updating count...");

                channel.getManager()
                        .setName("🌸 Üye Sayısı: " + guild.getMemberCount())
                        .queue();
            }
        };
    }

    @Override
    public void run(ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(service(), 1, 15, TimeUnit.MINUTES);
    }
}
