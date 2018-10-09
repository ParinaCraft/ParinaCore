package fi.joniaromaa.parinacorelibrary.bukkit.builder;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemStackBuilder
{
	private MaterialData materialData;
	
	private int amount;
	
	private boolean unbrekable;
	private String displayName;
	private HashMap<Enchantment, Integer> enchantments;
	
	ItemStackBuilder()
	{
		this.materialData = new MaterialData(Material.AIR);
		this.amount = 1;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStackBuilder type(Material material)
	{
		this.materialData = new MaterialData(material, this.materialData.getData());
		
		return this;
	}
	
	@Deprecated
	public ItemStackBuilder data(byte data)
	{
		this.materialData.setData(data);
		
		return this;
	}
	
	public ItemStackBuilder materialData(MaterialData materialData)
	{
		this.materialData = materialData;
		
		return this;
	}
	
	public ItemStackBuilder amount(int amount)
	{
		this.amount = amount;
		
		return this;
	}
	
	public ItemStackBuilder unbrekable(boolean unbrekable)
	{
		this.unbrekable = unbrekable;
		
		return this;
	}
	
	public ItemStackBuilder addEnchantment(Enchantment ench, int level)
	{
		if (this.enchantments == null)
		{
			this.enchantments = new HashMap<>();
		}
		
		this.enchantments.put(ench, level);
		
		return this;
	}
	
	public ItemStackBuilder displayName(String displayName)
	{
		this.displayName = displayName;
		
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack build()
	{
		ItemStack itemStack = new ItemStack(this.materialData.getItemType(), this.amount, (short)0, this.materialData.getData());

		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.spigot().setUnbreakable(this.unbrekable);
		itemMeta.setDisplayName(this.displayName);

		if (this.enchantments != null)
		{
			for(Entry<Enchantment, Integer> ench : this.enchantments.entrySet())
			{
				itemMeta.addEnchant(ench.getKey(), ench.getValue(), true);
			}
		}
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ItemStackBuilder builder()
	{
		return new ItemStackBuilder();
	}
}
