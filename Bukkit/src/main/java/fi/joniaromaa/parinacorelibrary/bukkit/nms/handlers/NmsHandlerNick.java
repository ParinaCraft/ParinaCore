package fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers;

import java.util.Map;
import java.util.UUID;

import com.comphenix.protocol.events.PacketEvent;

import fi.joniaromaa.parinacorelibrary.bukkit.ParinaCoreBukkitPlugin;
import fi.joniaromaa.parinacorelibrary.bukkit.nick.NickEntry;

public interface NmsHandlerNick
{
	public void handleDeadmau5(ParinaCoreBukkitPlugin plugin, Map<UUID, Map<Integer, NickEntry>> nickEntryByEntity, PacketEvent event);
}
