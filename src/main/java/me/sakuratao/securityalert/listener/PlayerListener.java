package me.sakuratao.securityalert.listener;

import litebans.api.Database;
import me.sakuratao.securityalert.SecurityAlert;
import me.sakuratao.securityalert.Utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerEvent(PlayerJoinEvent event){
        Player p = event.getPlayer();

        Database db = Database.get();
        TaskUtils.taskAsync(() -> {
            for (UUID uuid : db.getUsersByIP(p.getAddress().getHostName())){

                if (!db.isPlayerBanned(uuid, p.getAddress().getHostName())) continue;
                try (PreparedStatement st = db.prepareStatement("SELECT * FROM {bans} WHERE uuid=?")) {
                    st.setString(1, uuid.toString());
                    try (ResultSet rs = st.executeQuery()){
                        if (rs.next()){

                            if (SecurityAlert.INSTANCE.config.getBoolean("NO_REASON")){

                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), SecurityAlert.INSTANCE.config.getString("BAN_COMMAND").replace("%player%", p.getName()));

                            } else if (rs.getString("reason").contains(Objects.requireNonNull(SecurityAlert.INSTANCE.config.getString("REASON")))){

                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), SecurityAlert.INSTANCE.config.getString("BAN_COMMAND").replace("%player%", p.getName()));

                            }

                        }
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

}
