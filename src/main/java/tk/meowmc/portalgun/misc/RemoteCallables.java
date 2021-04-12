package tk.meowmc.portalgun.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import tk.meowmc.portalgun.Portalgun;
import tk.meowmc.portalgun.items.PortalGunItem;

import static tk.meowmc.portalgun.Portalgun.PORTALGUN;
import static tk.meowmc.portalgun.client.PortalgunClient.delay;
import static tk.meowmc.portalgun.items.PortalGunItem.*;

public class RemoteCallables {

    static MinecraftClient client = MinecraftClient.getInstance();

    public static void removeOldPortal1(ServerPlayerEntity user) {
        if (newPortal1 != null) {
            String key = user.getUuidAsString() + "-portalGunPortal0";
            boolean portalGunActive = user.isHolding(PORTALGUN);
            if (portalGunActive && portal1Exists) {
                newPortal1.kill();
                World world = user.world;
                world.playSound(null,
                        newPortal1.getX(),
                        newPortal1.getY(),
                        newPortal1.getZ(),
                        Portalgun.PORTAL_CLOSE_EVENT,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1F);
                portalsTag.remove("Left" + "Portal");
                waitPortal = false;
            }
        }
    }

    public static void removeOldPortal2(ServerPlayerEntity user) {
        if (newPortal2 != null) {
            boolean portalGunActive = user.isHolding(PORTALGUN);
            if (portalGunActive && portal2Exists) {
                newPortal2.kill();
                World world = user.world;
                world.playSound(null,
                        newPortal2.getX(),
                        newPortal2.getY(),
                        newPortal2.getZ(),
                        Portalgun.PORTAL_CLOSE_EVENT,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1F);
                portalsTag.remove("Right" + "Portal");
                waitPortal = false;
            }
        }
    }

    public static void portal1Place(ServerPlayerEntity user) {
        Item gunItem = PORTALGUN;
        boolean portalGunActive = user.isHolding(PORTALGUN);
        if (delay && newPortal1 != null && portalGunActive) {
            if (newPortal1.age >= 2 && newPortal1.isAlive()) {
                ((PortalGunItem) gunItem).portal1Spawn(user.world, user, user.getActiveHand());
                client.attackCooldown = 10;
                client.gameRenderer.firstPersonRenderer.resetEquipProgress(user.getActiveHand());
            } else if (!newPortal1.isAlive()) {
                ((PortalGunItem) gunItem).portal1Spawn(user.world, user, user.getActiveHand());
                newPortal1.removed = false;
                client.attackCooldown = 10;
                client.gameRenderer.firstPersonRenderer.resetEquipProgress(user.getActiveHand());
            }

        } else if (delay && newPortal1 == null && portalGunActive) {
            ((PortalGunItem) gunItem).portal1Spawn(user.world, user, user.getActiveHand());
            client.attackCooldown = 10;
            client.gameRenderer.firstPersonRenderer.resetEquipProgress(user.getActiveHand());
        }
    }

    public static void resetWaits(ServerPlayerEntity user) {
        waitPortal = false;
    }

}
