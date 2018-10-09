package fi.joniaromaa.parinacorelibrary.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fi.joniaromaa.parinacorelibrary.bukkit.inventory.PlayerInterfaceInventory;

public class InventoryListener implements Listener
{
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		Inventory inventory = event.getInventory();
		
		InventoryHolder inventoryHolder = inventory.getHolder();
		if (inventoryHolder instanceof PlayerInterfaceInventory)
		{
			((PlayerInterfaceInventory)inventoryHolder).onInventoryClick(event);
		}
	}
}
