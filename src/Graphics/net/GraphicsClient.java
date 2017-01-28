package Graphics.net;

import Graphics.GraphicsManager;
import Graphics.entities.PlayerMP;
import Graphics.net.packets.Packet;
import Graphics.net.packets.Packet.PacketTypes;
import Graphics.net.packets.Packet00Login;
import Graphics.net.packets.Packet01Disconnect;
import Graphics.net.packets.Packet02Move;

import java.io.IOException;
import java.net.*;

public class GraphicsClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private GraphicsManager gm;
	
	public GraphicsClient(GraphicsManager gm, String ipAddress) {
		this.gm = gm;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {			
			e.printStackTrace();
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while (true) {
			byte[] data = new byte [1024];
			DatagramPacket packet =  new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {				
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			//System.out.println("SERVER > " + new String(packet.getData()));
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login)packet, address, port);		
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left the world...");
			gm.level.removePlayerMP(((Packet01Disconnect)packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			handleMove((Packet02Move)packet);
		}
	}	

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			this.socket.send(packet);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {		
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the gm...");
		PlayerMP player = new PlayerMP(gm.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
		gm.level.addEntity(player);		
	}

	private void handleMove(Packet02Move packet) {
		this.gm.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(), packet.isMoving(), packet.getMovingDir());
	}
	
}
