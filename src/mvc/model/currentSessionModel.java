package mvc.model;

public class currentSessionModel {
	
	private String listenPort;
	private String remoteHost;
	private String remotePort;
	
	public currentSessionModel(String listenPort, String remoteHost, String remotePort){
		this.setListenPort(listenPort);
		this.setRemoteHost(remoteHost);
		this.setRemotePort(remotePort);
	}

	private String getListenPort() {
		return listenPort;
	}

	private void setListenPort(String listenPort) {
		this.listenPort = listenPort;
	}

	private String getRemoteHost() {
		return remoteHost;
	}

	private void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	private String getRemotePort() {
		return remotePort;
	}

	private void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

}
