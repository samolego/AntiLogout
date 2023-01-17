package org.samo_lego.antilogout;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.samo_lego.antilogout.config.LogoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiLogout implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("antilogout");

    public static final LogoutConfig config;

    static {
        config = new LogoutConfig();
    }

    @Override
    public void onInitializeServer() {

    }
}