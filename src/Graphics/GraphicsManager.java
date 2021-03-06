package Graphics;

import Graphics.entities.Player;
import Graphics.entities.Player2;
import Graphics.entities.PlayerMP;
import Graphics.gfx.Screen;
import Graphics.gfx.SpriteSheet;
import Graphics.level.Level;
import Graphics.level.tiles.Tile;
import Graphics.net.GraphicsClient;
import Graphics.net.GraphicsServer;
import Graphics.net.packets.Packet00Login;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

public class GraphicsManager extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 400;
	public static final int HEIGHT = WIDTH/4*3;
	public static final int SCALE = 3;
	public static final String NAME = "Game";
	public static GraphicsManager gm;
	
	public JFrame frame;
	
	public boolean running = false;
	public boolean loaded = false;
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colours = new int[6 * 6 * 6];
		
	private Screen screen;
	public InputHandler input;
	public WindowHandler windowHandler;
	public Level level;
	public Player player;
	public Player2 player2;
	
	public GraphicsClient socketClient;
	public GraphicsServer socketServer;
	
	public GraphicsManager() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public Screen getScreen(){
	    return screen;
    }

	public void init() {
		this.gm = this;
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255/5);
					int gg = (g * 255/5);
					int bb = (b * 255/5);
					
					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
				
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("resources/sprite_sheet.png"));
		input = new InputHandler(this);
		windowHandler = new WindowHandler(this);
		level = new Level("resources/levels/accessibility.png");
		player = new PlayerMP(level, (200), (310), input, "JohnCena", null, -1);
		//player2 = new Player2(level, 0, 0, input, JOptionPane.showInputDialog(this, "Please enter a username for Player2"));
		level.addEntity(player);
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
		if (socketServer != null) {
			socketServer.addConnection((PlayerMP)player, loginPacket);
		}
		//level.addEntity(player2);
	//	socketClient.sendData("ping".getBytes());		
		loginPacket.writeData(socketClient);
		this.loaded = true;
	}
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();	

		/*
		if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
			socketServer = new GraphicsServer(this);
			socketServer.start();
		}
		**/
		
		socketClient = new GraphicsClient(this, "192.168.0.8");
		socketClient.start();
	}	
	
	public synchronized void stop() {
		running = false;
	}	
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				frame.setTitle(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
		loaded = false;
	 			
	}

	public void tick() {
		tickCount++;							
		level.tick();
		
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}		
				
		//String msg = "Hello World 6756";
		//Font.render(msg, screen, screen.xOffset + screen.width / 2 - (msg.length() * 8) / 2, screen.yOffset + screen.height/2, Colours.get(-1, -1, -1, 000));
		
		int xOffset = player.x - screen.width / 2;
		int yOffset = player.y - screen.height / 2;
		
		level.renderTile(screen, xOffset, yOffset);
								
		level.renderEntities(screen);
		
		for(int y = 0; y < screen.height; y++) {
			for(int x = 0; x < screen.width; x++) {
				int colourCode = screen.pixels[x + y * screen.width];
				if(colourCode < 255) pixels[x + y * WIDTH] = colours[colourCode];
			}			
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawRect(0, 0, getWidth(), getHeight());				
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void overlayMap(int[][][] map, int height, int width){
        for(int x = 0; x < height; x++){
            for(int y = 0; y < width; y++){
                //System.out.println("X: " + x + " Y: " + y);

                if(!(map[x][y][0] == 255 && map[x][y][1] == 255 && map[x][y][2] == 255)){
                    if(map[x][y][0] == 0 && map[x][y][1] == 255 && map[x][y][2] == 0){
                        level.setTile(x,y,Tile.GREEN);
                    } else if(map[x][y][0] == 255 && map[x][y][1] > 150 && map[x][y][2] == 0){
                        level.setTile(x,y,Tile.RED);
                    } else {
                        level.setTile(x,y,Tile.YELLOW);
                    }
                }
            }
        }
        level.renderTile(screen,screen.getXOffset(), screen.getYOffset());
        //gm.render();
    }

	/*
	public void addNewArea(int x, int y, String type, int radius){
		Tile newTile = Tile.VOID;
		if(type.equalsIgnoreCase("Building")){
			newTile = Tile.BUILDING;
		}
		for(int xDir = -radius; xDir < radius; xDir++){
			for(int yDir = -radius; yDir < radius; yDir++){
				//System.out.println("X: " + (x+xDir) + " + Y: " + (y+yDir) + " + Tile: " + newTile.toString());
				level.alterTile(x+xDir, y+yDir, newTile);
			}
		}
	}
	**/

	/*
	public static void main(String[] args) throws IOException {
		
		new Game().start();
		
	}
	**/
	
	
}
