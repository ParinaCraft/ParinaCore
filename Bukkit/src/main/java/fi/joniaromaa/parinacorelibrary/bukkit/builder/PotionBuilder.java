package fi.joniaromaa.parinacorelibrary.bukkit.builder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class PotionBuilder extends ItemStackBuilder
{
	private List<PotionEffect> potionEffects;
	
	PotionBuilder()
	{
		super.type(Material.POTION);
	}
	
	@Override
	public ItemStackBuilder type(Material material)
	{
		throw new UnsupportedOperationException();
	}
	
	public PotionBuilder addPotionEffect(PotionEffect potionEffect)
	{
		if (this.potionEffects == null)
		{
			this.potionEffects = new ArrayList<>();
		}
		
		this.potionEffects.add(potionEffect);
		
		return this;
	}
	
	public ItemStack build()
	{
		ItemStack itemStack = super.build();
		
		if (this.potionEffects != null)
		{
			PotionMeta meta = (PotionMeta)itemStack.getItemMeta();
			
			for(PotionEffect effect : this.potionEffects)
			{
				meta.addCustomEffect(effect, true);
			}
			
			itemStack.setItemMeta(meta);
		}
		
		return itemStack;
	}
	
	public static PotionBuilder builder()
	{
		return new PotionBuilder();
	}
}
