package udevapp.levelzrpg.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.init.ConfigInit;
import net.levelz.network.PlayerStatsClientPacket;
import net.levelz.network.PlayerStatsServerPacket;
import net.levelz.stats.PlayerStatsManager;
import net.levelz.stats.Skill;
import net.projectile_damage.api.EntityAttributes_ProjectileDamage;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;

public class AddonPlayerStatsClientPacket extends PlayerStatsClientPacket {

    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerStatsServerPacket.STATS_SYNC_PACKET, (client, handler, buf, sender) -> {
            String skillString = buf.readString().toUpperCase();
            int level = buf.readInt();
            int points = buf.readInt();
            client.execute(() -> {
                PlayerStatsManager playerStatsManager = ((PlayerStatsManagerAccess) client.player).getPlayerStatsManager();
                Skill skill = Skill.valueOf(skillString);

                playerStatsManager.setSkillLevel(skill, level);
                playerStatsManager.setSkillPoints(points);

                switch (skill) {
                    case ALCHEMY -> {
                        EntityAttributes_SpellPower.POWER.forEach(
                                (magicSchool, customEntityAttribute) -> {
                                    playerStatsManager.getPlayerEntity().getAttributeInstance(customEntityAttribute)
                                            .setBaseValue(ConfigInit.CONFIG.attackBase + (double) playerStatsManager.getSkillLevel(Skill.ALCHEMY) * ConfigInit.CONFIG.attackBonus);
                                }
                        );
                    }
                    case ARCHERY -> {
                        playerStatsManager.getPlayerEntity().getAttributeInstance(EntityAttributes_ProjectileDamage.GENERIC_PROJECTILE_DAMAGE)
                                .setBaseValue(ConfigInit.CONFIG.attackBase + (double) playerStatsManager.getSkillLevel(Skill.ARCHERY) * ConfigInit.CONFIG.attackBonus);
                    }
                    default -> {
                    }
                }
            });
        });
    }
}
