package fi.joniaromaa.parinacorelibrary.bukkit.listeners.protocollib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Lists;

import fi.joniaromaa.parinacorelibrary.bukkit.ParinaCoreBukkitPlugin;
import fi.joniaromaa.parinacorelibrary.bukkit.nick.NickEntry;
import fi.joniaromaa.parinacorelibrary.bukkit.nms.NmsManager;

public class ServerPacketListenerNick extends PacketAdapter
{
	private final static ArrayList<PacketType> PACKETS = Lists.newArrayList(PacketType.Play.Server.NAMED_ENTITY_SPAWN,
			PacketType.Play.Server.ENTITY_DESTROY);
	
	private final ParinaCoreBukkitPlugin plugin;
	
	//Key: Watcher, Value: Key: The entity id
	private Map<UUID, Map<Integer, NickEntry>> nickEntryByEntity;
	
	public ServerPacketListenerNick(ParinaCoreBukkitPlugin plugin)
	{
		super(plugin.getLoader(), ListenerPriority.MONITOR, ServerPacketListenerNick.PACKETS);
		
		this.plugin = plugin;
		
		this.nickEntryByEntity = new HashMap<>();
	}

	@Override
	public void onPacketSending(PacketEvent event)
	{
		if (!event.isCancelled())
		{
			NmsManager.getNmsHandler().getNickHandler().handleDeadmau5(this.plugin, this.nickEntryByEntity, event);
			
		}
	}
	
	public void onQuit(Player player)
	{
		this.nickEntryByEntity.remove(player.getUniqueId());
	}
}
