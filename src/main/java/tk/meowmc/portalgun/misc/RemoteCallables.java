package tk.meowmc.portalgun.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.util.GeckoLibUtil;
import tk.meowmc.portalgun.Portalgun;
import tk.meowmc.portalgun.items.PortalGunItem;

import static tk.meowmc.portalgun.Portalgun.PORTALGUN;
import static tk.meowmc.portalgun.items.PortalGunItem.*;

public class RemoteCallables {

    public static void removeOldPortals(ServerPlayerEntity user) {
        if (newPortal1 != null) {
            newPortal1.kill();
            user.world.playSound(null,
                    newPortal1.getX(),
                    newPortal1.getY(),
                    newPortal1.getZ(),
                    Portalgun.PORTAL_CLOSE_EVENT,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    1F);
            portalsTag.remove("PrimaryPortal" + user.getUuidAsString());
            newPortal1 = null;
            portal1Exists = false;
        }
        if (newPortal2 != null) {
            newPortal2.kill();
            user.world.playSound(null,
                    newPortal2.getX(),
                    newPortal2.getY(),
                    newPortal2.getZ(),
                    Portalgun.PORTAL_CLOSE_EVENT,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    1F);
            portalsTag.remove("SecondaryPortal" + user.getUuidAsString());
            newPortal2 = null;
            portal2Exists = false;
        }

        if (portalOutline1 != null) {
            portalOutline1.kill();
            portalsTag.remove("PrimaryOutline" + user.getUuidAsString());
            portalOutline1 = null;
            outline1Exists = false;
        }
        if (portalOutline2 != null) {
            portalOutline2.kill();
            portalsTag.remove("SecondaryOutline" + user.getUuidAsString());
            portalOutline2 = null;
            outline2Exists = false;
        }
        resetWaits(user);
        tag.remove(user.world.getRegistryKey().toString());
    }

    public static void portal1Place(ServerPlayerEntity user) {
        PortalGunItem gunItem = (PortalGunItem) PORTALGUN;
        boolean portalGunActive = user.isHolding(PORTALGUN);
        if (!user.getItemCooldownManager().isCoolingDown(gunItem) && portalGunActive) {
            gunItem.portal1Spawn(user.world, user, user.getActiveHand());
        }
    }

    @Environment(EnvType.CLIENT)
    public static void playAnim() {
        MinecraftClient client = MinecraftClient.getInstance();

        PortalGunItem gunItem = (PortalGunItem) PORTALGUN;
        boolean portalGunActive = client.player.isHolding(PORTALGUN);
        ItemStack itemStack = client.player.getStackInHand(Hand.MAIN_HAND);
        AnimationController animController = GeckoLibUtil.getControllerForStack(gunItem.factory, itemStack, controllerName);
        if (!client.player.getItemCooldownManager().isCoolingDown(gunItem) && portalGunActive) {
            animController.markNeedsReload();
            animController.setAnimation(new AnimationBuilder().addAnimation("portal_shoot", false));
        }
        client.attackCooldown = 0;
    }

    public static void resetWaits(ServerPlayerEntity user) {
        waitPortal = false;
    }

}
