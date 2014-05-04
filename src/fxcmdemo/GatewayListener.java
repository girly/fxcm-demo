package fxcmdemo;

import java.util.HashMap;
import java.util.Map;

import com.fxcm.external.api.transport.IGateway;
import com.fxcm.external.api.transport.listeners.IGenericMessageListener;
import com.fxcm.messaging.ITransportable;


public class GatewayListener implements IGenericMessageListener {

	private final IGateway gateway;	
	private final Map<String,ITransportable> responses = new HashMap<>();
	
	
	public GatewayListener(IGateway gateway) {
		this.gateway = gateway;
		this.gateway.registerGenericMessageListener(this);
	}
	
	
	public void dispose() {
		this.gateway.removeGenericMessageListener(this);
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends ITransportable> T waitForResponse(String requestId) throws InterruptedException {		
		if (!this.responses.containsKey(requestId)) {
			this.wait();
		}
		return (T)this.responses.get(requestId);
	}
	
	
	@Override
	public synchronized void messageArrived(ITransportable message) {
		logMessage(message);		
		if (message.getRequestID() != null) {
			this.responses.put(message.getRequestID(), message);
			this.notify();
		}
	}
	
	
	
	private void logMessage(ITransportable message) {
		System.out.println(message);
	}
}
