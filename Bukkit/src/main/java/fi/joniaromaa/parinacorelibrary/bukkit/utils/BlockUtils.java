package fi.joniaromaa.parinacorelibrary.bukkit.utils;

import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class BlockUtils
{
	@SuppressWarnings("deprecation")
	public static void setBlockData(Block block, MaterialData data)
	{
		block.setTypeIdAndData(data.getItemTypeId(), data.getData(), true);
	}
}
