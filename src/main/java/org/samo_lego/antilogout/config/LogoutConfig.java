package org.samo_lego.antilogout.config;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;
import org.samo_lego.config2brigadier.IBrigadierConfigurator;

import java.io.File;

public class LogoutConfig implements IBrigadierConfigurator {
    public static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("antilogout.json").toFile();

    @Override
    public void save() {
        this.writeToFile(CONFIG_FILE);
    }

    public Afk afk = new Afk();

    public static class Afk {

        @SerializedName("afk_kick_message")
        public String afkMessage = "You are now AFK.";
        @SerializedName("// Permission level for /afk command. 0 := everyone, 4 := op, 2 := permission level 2")
        public final String _comment_permissionLevel0 = "";

        @SerializedName("// Used only if permission is not set by e.g. luckperms.")
        public final String _comment_permissionLevel1 = "";
        @SerializedName("permission_level")
        public int permissionLevel = 2;

        @SerializedName("// Max time limit for /afk command. Can be -1 for unlimited.")
        public final String _comment_maxAfkTime = "";
        @SerializedName("max_afk_time")
        public int maxAfkTime = -1;
    }

    public CombatLog combatLog = new CombatLog();

    public static class CombatLog {
        @SerializedName("// After how many milliseconds player leaves 'combat' mode and can disconnect, in milliseconds.")
        public final String _comment_combatTimeout = "";

        @SerializedName("combat_timeout")
        public long combatTimeout = 1000 * 30;
        @SerializedName("// If true, player will only be affected after being hurt by another player.")
        public final String _comment_playerHurtOnly0 = "";
        @SerializedName("// Otherwise, all damage sources will count.")
        public final String _comment_playerHurtOnly1 = "";
        @SerializedName("player_hurt_only")
        public boolean playerHurtOnly = true;

        @SerializedName("// Which permission level is required to bypass combat log.")
        public final String _comment_bypassPermissionLevel0 = "";

        @SerializedName("// Only active if not set by a permission mod.")
        public final String _comment_bypassPermissionLevel1 = "";
        @SerializedName("required_bypass_permission_level")
        public int bypassPermissionLevel = 4;
    }
}
