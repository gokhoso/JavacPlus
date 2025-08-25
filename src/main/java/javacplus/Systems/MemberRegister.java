package javacplus.Systems;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MemberRegister {
        private final Connection connection;
        private final GuildMemberJoinEvent event;

        public MemberRegister(Connection connection, GuildMemberJoinEvent event) {
                this.connection = connection;
                this.event = event;
        }

        public void execute() {
                String query = "Select doorChannel FROM SERVERS WHERE serverID = ?";
                try (var pstmt = connection.prepareStatement(query)) {
                        pstmt.setString(1, event.getGuild().getId());
                        var rs = pstmt.executeQuery();
                        String doorChannel = rs.getString("doorChannel");
                        Role role = event.getGuild().getRoleById("1407135910813765735");
                        event.getGuild().addRoleToMember(event.getMember(), role).queue();
                        if (doorChannel != null && !doorChannel.isEmpty()) {
                                event.getGuild().getTextChannelById(doorChannel)
                                                .sendMessage("Hoş geldin " + event.getUser().getAsMention() +
                                                                "! Raid güncellemeleri dolayısıyla kayıt sistemine geçtik lütfen sabırlı ol!")
                                                .queueAfter(5, TimeUnit.SECONDS);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
