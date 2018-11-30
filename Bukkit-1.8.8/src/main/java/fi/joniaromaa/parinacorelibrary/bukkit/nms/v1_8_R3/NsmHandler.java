package fi.joniaromaa.parinacorelibrary.bukkit.nms.v1_8_R3;

public class NsmHandler implements fi.joniaromaa.parinacorelibrary.bukkit.nms.handlers.NsmHandler
{
	private static final NmsHandlerServer SERVER_HANDLER = new NmsHandlerServer();
	private static final NmsHandlerEntity ENTITY_HANDLER = new NmsHandlerEntity();
	
	private static final NsmHandlerExploit EXPLOIT_HANDLER = new NsmHandlerExploit();
	private static final NmsHandlerNick NICK_HANDLER = new NmsHandlerNick();
	
	@Override
	public NmsHandlerServer getServerHandler()
	{
		return NsmHandler.SERVER_HANDLER;
	}

	@Override
	public NmsHandlerEntity getEntityHandler()
	{
		return NsmHandler.ENTITY_HANDLER;
	}

	@Override
	public NsmHandlerExploit getExploitHandler()
	{
		return NsmHandler.EXPLOIT_HANDLER;
	}

	@Override
	public NmsHandlerNick getNickHandler()
	{
		return NsmHandler.NICK_HANDLER;
	}
}
