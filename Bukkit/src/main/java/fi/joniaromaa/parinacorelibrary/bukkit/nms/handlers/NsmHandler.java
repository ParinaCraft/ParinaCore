package fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers;

public interface NsmHandler
{
	public NmsHandlerServer getServerHandler();
	public NmsHandlerEntity getEntityHandler();
	
	public NsmHandlerExploit getExploitHandler();
	public NmsHandlerNick getNickHandler();
}
