package org.satellite.dev.progiple.satecustomitems.items.realized;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.novasparkle.lunaspring.API.items.RealizedComponent;
import org.novasparkle.lunaspring.API.items.secondary.PickupItemComponent;
import org.novasparkle.lunaspring.API.menus.items.NonMenuItem;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.service.managers.NBTManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaMath;
import org.satellite.dev.progiple.satecustomitems.configs.Config;
import org.satellite.dev.progiple.satecustomitems.items.AbsItemComponent;
import org.satellite.dev.progiple.satecustomitems.items.CursoredItemComponent;
import org.satellite.progiple.satejewels.SateJewels;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RealizedComponent
public class DragonEggItemComponent extends AbsItemComponent implements CursoredItemComponent, PickupItemComponent {
    private int heads_limit;
    private String give_per_head;
    private String give_after_first_pickup;
    private String give_after_last_head;
    public DragonEggItemComponent() {
        super("dragon_egg");
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack cursor = e.getCursor();

        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null || itemStack.getType() != Material.DRAGON_HEAD) return;

        if (e.isShiftClick() || e.getClick() != ClickType.RIGHT) return;

        Player player = (Player) e.getWhoClicked();
        if (this.blacklistedWorlds.contains(player.getWorld())) return;

        InventoryView view = e.getView();
        if (!view.getBottomInventory().equals(e.getClickedInventory())) {
            Inventory topInv = view.getTopInventory();
            if (topInv.getType() != InventoryType.PLAYER
                    && topInv.getType() != InventoryType.CRAFTING
                    && topInv.getType() != InventoryType.CREATIVE) return;
        }

        e.setCancelled(true);
        String prop = "dragon_egg_prop_limit_now";

        int now = NBTManager.hasTag(cursor, prop) ? NBTManager.getInt(cursor, prop) : 0;
        if (now >= this.heads_limit) {
            Config.sendMessage(player, "dragonEgg.limit");
            return;
        }

        int headsToUse = Math.min(this.heads_limit - now, itemStack.getAmount());
        int givedStars = 0;
        for (int i = 0; i < headsToUse; i++) {
            givedStars += LunaMath.getRandomInt(this.give_per_head);
        }

        now += headsToUse;
        NBTManager.setInt(cursor, prop, now);

        itemStack.setAmount(itemStack.getAmount() - headsToUse);
        this.updateInfo(cursor, now);

        Config.sendMessage(player, "dragonEgg.putHead", "amount-%-" + headsToUse, "stars-%-" + givedStars);
        if (now == this.heads_limit) {
            int givedLastStars = LunaMath.getRandomInt(this.give_after_last_head);
            Config.sendMessage(player, "dragonEgg.putLastHead", "stars-%-" + givedLastStars);

            givedStars += givedLastStars;
        }

        SateJewels.getINSTANCE().getAPI().giveJewels(player, givedStars);
    }

    @Override
    public void onPickup(PlayerAttemptPickupItemEvent e, ItemStack itemStack) {
        String prop = "dragon_egg_prop_pickuped";
        if (NBTManager.hasTag(itemStack, prop)) return;

        Player player = e.getPlayer();
        NBTManager.setBool(itemStack, prop, true);

        this.updateInfo(itemStack, 0);

        int givedStars = LunaMath.getRandomInt(this.give_after_first_pickup);
        Config.sendMessage(player, "dragonEgg.firstPickup", "stars-%-" + givedStars);
        SateJewels.getINSTANCE().getAPI().giveJewels(player, givedStars);
    }

    private void updateInfo(ItemStack itemStack, int now_limit) {
        String uuidProp = "dragon_egg_prop_uuid";
        if (!NBTManager.hasTag(itemStack, uuidProp)) NBTManager.setUUID(itemStack, uuidProp, UUID.randomUUID());

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(ColorManager.color(this.itemSection.getString("displayName")));

        List<String> lore = new ArrayList<>(this.itemSection.getStringList("lore"));
        lore.replaceAll(l -> ColorManager.color(l
                .replace("[limit]", String.valueOf(this.heads_limit))
                .replace("[now]", String.valueOf(now_limit))));
        meta.setLore(lore);

        itemStack.setItemMeta(meta);
        if (this.itemSection.getBoolean("enchanted")) {
            itemStack.addUnsafeEnchantment(Enchantment.LURE, 1);
            itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    @Override
    public NonMenuItem createItem() {
        NonMenuItem nonMenuItem = super.createItem();
        nonMenuItem.setMaterial(Material.DRAGON_EGG);
        this.updateInfo(nonMenuItem.getItemStack(), 0);
        return nonMenuItem;
    }

    @Override
    public boolean itemIsComponent(ItemStack itemStack) {
        return itemStack != null && (itemStack.getType() == Material.DRAGON_EGG || super.itemIsComponent(itemStack));
    }
}
