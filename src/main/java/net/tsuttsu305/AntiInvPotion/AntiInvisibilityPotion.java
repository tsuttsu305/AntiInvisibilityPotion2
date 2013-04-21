package net.tsuttsu305.AntiInvPotion;

import java.util.logging.Logger;


import net.tsuttsu305.AntiInvPotion.Listener.PotionListener;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * 
 * @author tsuttsu305
 *
 */
public class AntiInvisibilityPotion extends JavaPlugin{
	
	private AntiInvisibilityPotion plugin;
	private Logger logger;
	private PluginDescriptionFile pdf;
	
	
	@Override
	public void onEnable(){
		plugin = this;
		logger = getLogger();
		pdf = getDescription();
		
		//Register Events
		getServer().getPluginManager().registerEvents(new PotionListener(plugin), plugin);
		
		//LoadConfig
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@Override
	public void onDisable(){
		logger.info("[" + pdf.getName() + "] " + pdf.getName() + "is Disabled.");
	}
	
	public boolean isAllowOP(){
		return getConfig().getBoolean("allowOP");
	}
	
	public boolean islogging(){
		return getConfig().getBoolean("logging");
	}
	
	public String getMsg(){
		return getConfig().getString("block_potion_use");
	}
	
	public Logger getlog(){
		return logger;
	}
}
