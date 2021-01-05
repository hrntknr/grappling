package net.hrntknr.grappling;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Grappling extends JavaPlugin implements Listener {
  ItemStack grapplingHookItemStack;

  public Grappling() {
    grapplingHookItemStack = new ItemStack(Material.FISHING_ROD);
    ItemMeta meta = grapplingHookItemStack.getItemMeta();
    meta.setDisplayName(ChatColor.GOLD + "Grappling Hook");
    meta.setUnbreakable(true);
    meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    grapplingHookItemStack.setItemMeta(meta);
  }

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    NamespacedKey key = new NamespacedKey(this, "grappling_hook");
    ShapedRecipe recipe = new ShapedRecipe(key, grapplingHookItemStack);
    recipe.shape("III", "IFI", "III");
    recipe.setIngredient('F', Material.FISHING_ROD);
    recipe.setIngredient('I', Material.IRON_INGOT);
    Bukkit.addRecipe(recipe);
  }

  @Override
  public void onDisable() {
  }

  @EventHandler
  public void onFish(PlayerFishEvent event) {
    if (event.getState() == PlayerFishEvent.State.REEL_IN) {
      Player player = event.getPlayer();
      ItemStack mainHandItem = player.getInventory().getItemInMainHand();
      ItemStack offHandItem = player.getInventory().getItemInOffHand();
      if (!mainHandItem.equals(grapplingHookItemStack) && !offHandItem.equals(grapplingHookItemStack)) {
        return;
      }
      Location playerPos = player.getLocation();
      Location hookPos = event.getHook().getLocation();
      Vector vec = hookPos.toVector().subtract(playerPos.toVector());
      vec.setY(vec.getY() * 0.3);
      player.setVelocity(vec);
    }
  }

  @EventHandler
  public void onEntityDamageEvent(EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getEntity();
    if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
      return;
    }
    ItemStack mainHandItem = player.getInventory().getItemInMainHand();
    ItemStack offHandItem = player.getInventory().getItemInOffHand();
    if (!mainHandItem.equals(grapplingHookItemStack) && !offHandItem.equals(grapplingHookItemStack)) {
      return;
    }
    event.setCancelled(true);
  }
}
