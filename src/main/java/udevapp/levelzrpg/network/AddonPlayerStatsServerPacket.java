package udevapp.levelzrpg.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.access.PlayerSyncAccess;
import net.levelz.init.ConfigInit;
import net.levelz.init.CriteriaInit;
import net.levelz.network.PlayerStatsServerPacket;
import net.levelz.stats.PlayerStatsManager;
import net.levelz.stats.Skill;
import net.minecraft.entity.attribute.EntityAttributes;
import net.projectile_damage.api.EntityAttributes_ProjectileDamage;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;

public class AddonPlayerStatsServerPacket extends PlayerStatsServerPacket {

    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(STATS_INCREASE_PACKET, (server, player, handler, buffer, sender) -> {
            String skillString = buffer.readString().toUpperCase();
            int level = buffer.readInt();
            server.execute(() -> {
                PlayerStatsManager playerStatsManager = ((PlayerStatsManagerAccess) player).getPlayerStatsManager();
                if (playerStatsManager.getSkillPoints() - level >= 0) {
                    Skill skill = Skill.valueOf(skillString);
                    switch (skill) {
                        case ALCHEMY -> {
                            EntityAttributes_SpellPower.POWER.forEach(
                                    (magicSchool, customEntityAttribute) -> {
                                        player.getAttributeInstance(customEntityAttribute)
                                                .setBaseValue(player.getAttributeBaseValue(customEntityAttribute) + ConfigInit.CONFIG.attackBonus * level);
                                    }
                            );
                        }
                        case ARCHERY -> {
                            player.getAttributeInstance(EntityAttributes_ProjectileDamage.GENERIC_PROJECTILE_DAMAGE)
                                    .setBaseValue(player.getAttributeBaseValue(EntityAttributes_ProjectileDamage.GENERIC_PROJECTILE_DAMAGE) + ConfigInit.CONFIG.attackBonus * level);
                        }
                        default -> {
                        }
                    }
                }

            });
        });
    }
}
