package fi.joniaromaa.parinacorelibrary.bukkit.holograms.dynamic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import fi.joniaromaa.parinacorelibrary.api.ParinaCore;
import fi.joniaromaa.parinacorelibrary.api.user.User;
import fi.joniaromaa.parinacorelibrary.bukkit.holograms.StaticHologram;
import me.lucko.luckperms.LuckPerms;
import net.md_5.bungee.api.ChatColor;

public class LeaderboardHologram implements DynamicHologram
{
	private static final long UPDATE_INTERVAL = TimeUnit.MILLISECONDS.toNanos(5000);
	
	protected LinkedHashMap<UUID, Long> cached;
	
	private long lastUpdate;
	
	private String title;
	private Location location;
	
	protected StaticHologram titleHologram;
	protected List<StaticHologram> leaderboardHolograms;
	protected StaticHologram leaderboardOwnScore;
	
	public LeaderboardHologram(String title, Location location)
	{
		this.cached = new LinkedHashMap<>();
		
		this.leaderboardHolograms = new ArrayList<>();
		
		this.title = title;
		this.location = location;
		
		int size = 10; //Hardcored :)
		
		Location startLocation = location.clone().add(0, (size + 1) * 0.4, 0);
		
		this.titleHologram = new StaticHologram(title, startLocation);
		
		for(int i = 1; i <= size; i++)
		{
			this.leaderboardHolograms.add(new StaticHologram("#" + i + " - " + ChatColor.YELLOW + "Loading", startLocation.subtract(0, 0.3, 0)));
		}
		
		this.leaderboardOwnScore = new StaticHologram("%OWN%", startLocation.subtract(0, 0.3, 0));
	}
	
	@Override
	public void tick()
	{
		long now = System.nanoTime();
		if (now - this.lastUpdate >= LeaderboardHologram.UPDATE_INTERVAL)
		{
			this.lastUpdate = now;
			
			this.requestUpdate();
		}
	}
	
	public void requestUpdate()
	{
		int i = 0;
		for (UUID uuid : this.cached.keySet())
		{
			try
			{
				LuckPerms.getApi().getUserManager().loadUser(uuid).get(1, TimeUnit.SECONDS);
			}
			catch (InterruptedException | ExecutionException | TimeoutException e)
			{
				//Ignore
			}
			
			if (++i == 10)
			{
				break;
			}
		}
		
		int j = 0;
        for (Entry<UUID, Long> entry : this.cached.entrySet())
        {
            int thisI = ++j;
            ParinaCore.getApi().getUserManager().loadUser((UUID)entry.getKey()).thenAccept(u ->
            {
                if (u == null)
                {
                    this.leaderboardHolograms.get(thisI - 1).setText(ChatColor.YELLOW + "#" + thisI + " - " + ChatColor.YELLOW + "Loading");
                }
                else
                {
                    this.leaderboardHolograms.get(thisI - 1).setText(ChatColor.YELLOW + "#" + thisI + " - " + u.getFormattedDisplayName() + ChatColor.YELLOW + " - " + this.formatScore(entry.getValue()));
                }
            });
            
            if (thisI == 10)
            {
                break;
            }
        }
		
		this.leaderboardOwnScore.setText("%OWN% - " + System.currentTimeMillis());
	}
	
	protected String formatScore(long score)
	{
		return score + "";
	}

	public void injectOwn(PacketEvent event)
	{
		Player player = event.getPlayer();
		PacketContainer packet = event.getPacket();
		
		int id = packet.getIntegers().read(0);
		
		if (this.leaderboardOwnScore.getEntityId() == id)
		{
			int i = 0;
			Long value = null;
			for(Entry<UUID, Long> cache : this.cached.entrySet())
			{
				i++;
				
				if (cache.getKey().equals(player.getUniqueId()))
				{
					value = cache.getValue();
					break;
				}
			}
			
			List<WrappedWatchableObject> watchables = packet.getWatchableCollectionModifier().read(0);
			
			Iterator<WrappedWatchableObject> iterator = watchables.iterator();
			while (iterator.hasNext())
			{
				WrappedWatchableObject watchable = iterator.next();
				if (watchable.getIndex() == 2 || watchable.getIndex() == 3)
				{
					iterator.remove();
				}
			}
			
			User user = ParinaCore.getApi().getUserManager().getUser(player.getUniqueId());
			if (user != null)
			{
				if (value != null)
				{
					watchables.add(new WrappedWatchableObject(2, ChatColor.YELLOW + ChatColor.BOLD.toString() + "#" + i + " - " + user.getFormattedDisplayName() + " - " + this.formatScore(value)));
				}
				else
				{
					watchables.add(new WrappedWatchableObject(2, ChatColor.RED + ChatColor.BOLD.toString() + "N/A" + ChatColor.YELLOW + " - " + user.getFormattedDisplayName()));
				}
	
				watchables.add(new WrappedWatchableObject(3, i > 10 ? (byte)1 : (byte)0));
			}
			else
			{
				watchables.add(new WrappedWatchableObject(3, (byte)0));
			}
			
			packet.getWatchableCollectionModifier().write(0, watchables);
		}
	}
}
