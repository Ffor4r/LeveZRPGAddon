package udevapp.levelzrpg;

import net.fabricmc.api.ClientModInitializer;
import udevapp.levelzrpg.network.AddonPlayerStatsClientPacket;

public class LeveZRPGaddonClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AddonPlayerStatsClientPacket.initialize();
	}
}