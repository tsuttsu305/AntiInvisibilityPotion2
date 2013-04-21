package net.tsuttsu305.AntiInvPotion.Listener;


import net.tsuttsu305.AntiInvPotion.AntiInvisibilityPotion;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class PotionListener implements Listener{
	private AntiInvisibilityPotion plugin;;
	
	public PotionListener(AntiInvisibilityPotion plugin) {
		this.plugin = plugin;
	}
	
	//Permission: invisibility.allow
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDrinkPotion(PlayerItemConsumeEvent event){
		//AllowOP setting == true
		if (plugin.isAllowOP() && event.getPlayer().isOp()){
			return;
		}
		
		//Item is Potion
		if (event.getItem().getType() == Material.POTION){
			if (event.getItem().getDurability() != 0){ //Water Bottles is throw Exception.
				
				Potion potion = Potion.fromItemStack(event.getItem());
				if (potion.getType() == PotionType.INVISIBILITY){
					
					Player player = event.getPlayer();
					if (player.hasPermission("invisibility.allow") == false){
						player.sendMessage(ChatColor.RED + plugin.getMsg());
						event.setCancelled(true);
						
						//Logging
						if (plugin.islogging()){
							plugin.getlog().info(player.getName() + " tried to use the InvisibilityPotion.");
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDispense(BlockDispenseEvent event){
		if (event.getItem().getType() == Material.POTION){
			if (event.getItem().getDurability() != 0){ //Water Bottles is throw Exception.
				
				Potion potion = Potion.fromItemStack(event.getItem());
				if (potion.getType() == PotionType.INVISIBILITY && potion.isSplash()){
					//hold Entity. not Splash
					Entity sp = event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), event.getItem());
					sp.setVelocity(event.getVelocity());
					Inventory inv = ((Dispenser)event.getBlock().getState()).getInventory();
					int fi = inv.first(event.getItem());
					inv.setItem(fi, new ItemStack(0));
					
					event.setCancelled(true);
					
					//Logging
					if (plugin.islogging()){
						Location loc = event.getBlock().getLocation();
						plugin.getlog().info("x:" + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ() + " Dispenser was trying to throw the InvisibilityPotion.");
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void hitSplashPotion(PotionSplashEvent event){
		if (Potion.fromItemStack(event.getEntity().getItem()) == null){
			return;
		}
		
		if (Potion.fromItemStack(event.getEntity().getItem()).getType() == PotionType.INVISIBILITY){
			if (event.getEntity().getShooter() instanceof Player){
				Player player = (Player) event.getEntity().getShooter();
				if (player.hasPermission("invisibility.allow") || (plugin.isAllowOP() && player.isOp())){
					return;
				}else{
					player.sendMessage(ChatColor.RED + plugin.getMsg());
					event.setCancelled(true);
					
					event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), event.getPotion().getItem());
					
					//Logging
					if (plugin.islogging()){
						plugin.getlog().info(player.getName() + " tried to throw the InvisibilitySplashPotion.");
					}
				}
			}
		}
	}
}