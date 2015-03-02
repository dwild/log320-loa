package Server;
 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ProtocolTCP implements IProtocol {
	
    private Socket socket = null;
    private BufferedInputStream input = null;
    private BufferedOutputStream output = null;
    private String host;
    private int port;

	public ProtocolTCP(String nomHote, int port){	
		this.host = nomHote;
		this.port = port;	
	}
	
	public void connect() {
        try {
        	socket = new Socket(host, port);
    	   	input = new BufferedInputStream(socket.getInputStream());
    		output = new BufferedOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.out.println("Le serveur «" + host + "» est non valable");
        } catch (IOException e) {
        	System.out.println("Le serveur «" + host + "» ne répond pas sur le port " + port);
        } catch (IllegalArgumentException e) {
        	System.out.println("Les arguments fournis au serveur sont invalides.");   	
        }
	}	
	
	public boolean isConnected() {
		return socket != null && socket.isConnected() && !socket.isClosed();
	}
	
	public void disconnect() {
		try {
			if (socket.isConnected()) {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("Impossible de fermer la connexion avec le serveur «" + host + "»");   	
        }
	}
	
	public void send(String message) {
		try {
			output.write(message.getBytes(), 0, message.length());
			output.flush();
		} catch (IOException e) {
			System.out.println("Déconnexion imprévue avec le serveur «" + host + "»");  
			disconnect();
		}
	}

	public String readLine(int bufferSize) {
		String reponseServeur = "";
		try {
			byte[] aBuffer = new byte[bufferSize];			
			int size = input.available();
			input.read(aBuffer, 0, size);
			reponseServeur = new String(aBuffer).trim();
		} catch (IOException e) {
			System.out.println("Déconnexion imprévue avec le serveur «" + host + "»");   	
            disconnect();
        }
		return reponseServeur;
	}
	
	public char readCMD() {
		char cmd = 0; 
        try {		
            cmd = (char)input.read();
		} catch (IOException e) {
			System.out.println("Déconnexion imprévue avec le serveur «" + host + "»");   	
            disconnect();
        }
        return cmd;
	}
}