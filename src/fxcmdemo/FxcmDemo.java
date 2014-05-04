package fxcmdemo;

import com.fxcm.external.api.transport.FXCMLoginProperties;
import com.fxcm.external.api.transport.GatewayFactory;
import com.fxcm.external.api.transport.IGateway;
import com.fxcm.fix.pretrade.TradingSessionStatus;


public class FxcmDemo {
	
	private static final String TERMINAL = "Demo";
	private static final String SERVER = "http://www.fxcorporate.com/Hosts.jsp";
	
	
	public static void main(String[] args) throws Throwable {
		
		String username = args[0];
		String password = args[1];
		
		final FXCMLoginProperties login = new FXCMLoginProperties(username, password, TERMINAL, SERVER);
		final IGateway gateway = GatewayFactory.createGateway();
		
		final GatewayListener gatewayListener = new GatewayListener(gateway);
		
		log("Logging in...");
		gateway.login(login);
		
		log("Requesting trading session status...");
		String statusRequestId = gateway.requestTradingSessionStatus();
		TradingSessionStatus status = gatewayListener.waitForResponse(statusRequestId);
		log("Trading session ID is '%s'.", status.getTradingSessionID());
		
		Thread.sleep(5000);
		
		log("Logging out...");
		gateway.logout();
		
		gatewayListener.dispose();
	}
	
	
	
	private static void log(String message)
	{
		System.out.println(message);
	}
	
	
	private static void log(String message, Object... args)
	{
		System.out.println(String.format(message, args));
	}
}