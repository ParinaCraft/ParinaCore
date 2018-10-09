package fi.joniaromaa.parinacorelibrary.bukkit.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class PlayerInterfaceInventory implements InventoryHolder
{
	private final Plugin plugin;
	private final Inventory inventory;
	
	private final Map<Integer, Consumer<InventoryClickEvent>> actions;
	
	public PlayerInterfaceInventory(Plugin plugin, int size, String title)
	{
		this.plugin = plugin;
		this.inventory = plugin.getServer().createInventory(this, size, title);
		
		this.actions = new HashMap<>();
	}
	
	public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action)
	{
		this.inventory.setItem(slot, item);
		this.actions.put(slot, action);
	}
	
	public void onInventoryClick(InventoryClickEvent event)
	{
		event.setCancelled(true);
		
		if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() == this)
		{
			Consumer<InventoryClickEvent> consumer = this.actions.get(event.getSlot());
			if (consumer != null)
			{
				consumer.accept(event);
			}
		}
	}

	@Override
	public Inventory getInventory()
	{
		return this.inventory;
	}
	
	public void close()
	{
		this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
		{
			PlayerInterfaceInventory.this.inventory.getViewers().forEach(HumanEntity::closeInventory);
		});
	}
	
	public void close(HumanEntity humanEntity)
	{
		this.plugin.getServer().getScheduler().runTask(this.plugin, () ->
		{
			if (PlayerInterfaceInventory.this.inventory.getViewers().contains(humanEntity))
			{
				humanEntity.closeInventory();
			}
		});
	}
}
