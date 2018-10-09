package fi.joniaromaa.parinacorelibrary.bukkit.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import lombok.Getter;

public class BorderVector implements ConfigurationSerializable
{
	@Getter private final BlockVector min;
	@Getter private final BlockVector max;
	
	public BorderVector(BlockVector min, BlockVector max)
	{
		this.min = min;
		this.max = max;
	}
	
	public boolean contains(Vector vector)
	{
		return this.contains(vector.getX(), vector.getY(), vector.getZ());
	}
	
	public boolean contains(double x, double y, double z)
	{
		return x >= this.min.getX() && x <= this.max.getX() ? (y >= this.min.getY() && y <= this.max.getY() ? z >= this.min.getZ() && z <= this.max.getZ() : false) : false;
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<>();
		data.put("min", this.min);
		data.put("max", this.max);
		return data;
	}
	
	public static BorderVector deserialize(Map<String, Object> data)
	{
		BlockVector min = (BlockVector)data.get("min");
		BlockVector max = (BlockVector)data.get("max");
		
		return new BorderVector(min, max);
	}
}
