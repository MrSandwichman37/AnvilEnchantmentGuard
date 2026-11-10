package se.mrsandwich.anvilguard;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class AnvilEnchantmentGuard extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("AnvilEnchantmentGuard har startat!");
    }

    @Override
    public void onDisable() {

        getLogger().info("AnvilEnchantmentGuard har stängts av.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvil(PrepareAnvilEvent event) {

        ItemStack result = event.getResult();

        if (result == null || result.getType().isAir()) {
            return;
        }

        Map<Enchantment, Integer> enchantments =
                result.getEnchantments();

        if (enchantments.isEmpty()) {
            return;
        }

        List<Enchantment> enchantmentList =
                new ArrayList<>(enchantments.keySet());

        for (Enchantment enchantment : enchantmentList) {

            if (!enchantment.canEnchantItem(result)) {

                event.setResult(null);

                getLogger().info(
                        "Blockerade ogiltig enchantment på "
                                + result.getType()
                );

                return;
            }
        }

        for (int i = 0; i < enchantmentList.size(); i++) {

            for (int j = i + 1;
                 j < enchantmentList.size();
                 j++) {

                Enchantment first = enchantmentList.get(i);
                Enchantment second = enchantmentList.get(j);

                if (first.conflictsWith(second)
                        || second.conflictsWith(first)) {

                    event.setResult(null);

                    return;
                }
            }
        }
    }
}