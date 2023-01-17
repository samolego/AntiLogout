package org.samo_lego.antilogout.config;

import com.google.gson.annotations.SerializedName;

public class LogoutConfig {
    @SerializedName("// After how many milliseconds player leaves 'combat' mode and can disconnect.")
    public final String _comment_disconnectTimeout = "";
    @SerializedName("combat_timeout")
    public long disconnectTimeout = 1000 * 30;
}
