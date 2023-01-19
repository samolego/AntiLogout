package org.samo_lego.antilogout.config;

import com.google.gson.annotations.SerializedName;

public class LogoutConfig {
    @SerializedName("// After how many milliseconds player leaves 'combat' mode and can disconnect.")
    public final String _comment_disconnectTimeout = "";
    @SerializedName("// Max time limit for /afk command. Can be -1 for unlimited.")
    public final String _comment_maxAfkTime = "";
    @SerializedName("combat_timeout")
    public long combatTimeout = 1000 * 5;  //* 30;
    @SerializedName("max_afk_time")
    public int maxAfkTime = 1000 * 60;
}
