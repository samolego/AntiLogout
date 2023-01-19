package org.samo_lego.antilogout.config;

import com.google.gson.annotations.SerializedName;

public class LogoutConfig {
    @SerializedName("// After how many milliseconds player leaves 'combat' mode and can disconnect.")
    public final String _comment_disconnectTimeout = "";

    @SerializedName("combat_timeout")
    public long combatTimeout = 1000 * 30;

    @SerializedName("// Max time limit for /afk command. Can be -1 for unlimited.")
    public final String _comment_maxAfkTime = "";
    @SerializedName("max_afk_time")
    public int maxAfkTime = -1;
    public CombatLog combatLog = new CombatLog();

    public static class CombatLog {
        @SerializedName("// If true, player will only be affected after being hurt by another player.")
        public final String _comment_playerHurtOnly0 = "";
        @SerializedName("// Otherwise, all damage sources will count.")
        public final String _comment_playerHurtOnly1 = "";
        @SerializedName("player_hurt_only")
        public boolean playerHurtOnly = true;
    }
}
