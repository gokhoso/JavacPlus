package net.JavacClassic.Services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.JavacClassic.Cache.CUser;
import net.JavacClassic.Entites.CachedMessage;
import net.JavacClassic.Interfaces.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteCache implements IService {

    private final Logger logger = LoggerFactory.getLogger(DeleteCache.class);
    private final CUser cUser;

    public DeleteCache(CUser cUser) {
        this.cUser = cUser;
    }

    @Override
    public Runnable service() {
        logger.info("Running Service DeleteCache");
        Map<String, CachedMessage> cachedMessages = cUser.getCachedMessages();

        return new Runnable() {
            @Override
            public void run() {
                if (cachedMessages.isEmpty()) {
                    return;
                }

                List<CachedMessage> expiredMessages = getExpiredAndDelete(
                        cachedMessages
                );

                if (!expiredMessages.isEmpty()) {
                    logger.info("Removed {} expired cache message", expiredMessages.size());
                }
            }
        };
    }

    @Override
    public void run(ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(service(), 1, 15, TimeUnit.MINUTES);
    }

    private List<CachedMessage> getExpiredAndDelete(
            Map<String, CachedMessage> cachedMessages
    ) {
        List<CachedMessage> expiredCache = new ArrayList<>();
        long now = Instant.now().toEpochMilli();
        long fiveMinuteMillis = TimeUnit.MINUTES.toMillis(5);

        Iterator<Map.Entry<String, CachedMessage>> iterator = cachedMessages
                .entrySet()
                .iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CachedMessage> entry = iterator.next();
            long created = entry.getValue().getCreated().toEpochMilli();
            long deltaMillis = now - created;

            if (deltaMillis >= fiveMinuteMillis) {
                expiredCache.add(entry.getValue());
                iterator.remove();
            }
        }

        return expiredCache;
    }
}
