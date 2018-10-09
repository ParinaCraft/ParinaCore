package fi.joniaromaa.parinacorelibrary.bukkit.builder;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemStackBuilder
{
	private Color color;
	
	LeatherArmorBuilder()
	{
	}
	
	@Override
	public LeatherArmorBuilder type(Material material)
	{
		if (material != Material.LEATHER_HELMET && material != Material.LEATHER_CHESTPLATE && material != Material.LEATHER_LEGGINGS && material != Material.LEATHER_BOOTS)
		{
			throw new UnsupportedOperationException();
		}
		
		super.type(material);
		
		return this;
	}
	
	public LeatherArmorBuilder color(Color color)
	{
		this.color = color;
		
		return this;
	}
	
	public ItemStack build()
	{
		ItemStack itemStack = super.build();
		
		if (this.color != null)
		{
			LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
			meta.setColor(this.color);
			
			itemStack.setItemMeta(meta);
		}
		
		return itemStack;
	}
	
	public static LeatherArmorBuilder builder()
	{
		return new LeatherArmorBuilder();
	}
}
