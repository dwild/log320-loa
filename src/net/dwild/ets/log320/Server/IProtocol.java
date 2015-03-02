package Server;

public interface IProtocol {
	public void connect();
	public boolean isConnected();
	public void disconnect();
	public void send(String message);
	public String readLine(int bufferSize);
	public char readCMD();
}
