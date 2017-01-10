package de.simonsator.partyandfriends.status.spigot;

import com.google.gson.JsonElement;
import de.simonsator.partyandfriendsgui.api.events.creation.HeadCreationEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author simonbrungs
 * @version 1.0.0 09.01.17
 */
public class StatusSpigot extends JavaPlugin implements Listener {
	private Matcher statusMatcher;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		for (String path : getConfig().getKeys(true))
			if (getConfig().isString(path))
				getConfig().set(path, ChatColor.translateAlternateColorCodes('&', getConfig().getString(path)));
		statusMatcher = Pattern.compile("[STATUS]", Pattern.LITERAL).matcher(getConfig().getString("Status"));
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onHeadCreation(HeadCreationEvent pEvent) {
		JsonElement status = pEvent.get("status");
		if (status != null) {
			ItemMeta meta = pEvent.getHead().getItemMeta();
			List<String> lore = meta.getLore();
			lore.add(statusMatcher.replaceFirst(status.getAsString()));
			meta.setLore(lore);
			pEvent.getHead().setItemMeta(meta);
		}
	}
}
