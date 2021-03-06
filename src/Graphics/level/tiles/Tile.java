package Graphics.level.tiles;

import Graphics.gfx.Colours;
import Graphics.gfx.Screen;
import Graphics.level.Level;

public abstract class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(000, -1, -1, -1), 0xFF000000);
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colours.get(-1, 333, -1, -1), 0xFF555555);
	public static final Tile GRASS = new BasicTile(2, 2, 0, Colours.get(-1, 131, 141, -1), 0xFF000000);
	public static final Tile GREEN = new BasicTile(3, 1, 0, Colours.get(-1, 50, -1, -1), 0xFF000000);
	public static final Tile YELLOW = new BasicTile(4, 1, 0, Colours.get(-1, 550, -1, -1), 0xFF000000);
	public static final Tile RED = new BasicTile(5, 1, 0, Colours.get(-1, 500, -1, -1), 0xFF000000);
	//public static final Tile BUILDING = new BasicTile(3, 3, 0, Colours.get(-1, 200, 50, -1), 0xFF88848F);
	//public static final Tile SPIRAL = new BasicTile(3, 3, 0, Colours.get(-1, 131, 141, -1), 0xFF009600);
	//public static final Tile CORNER = new BasicTile(4, 4, 0, Colours.get(-1, 131, 141, -1), 0xFF006400);
	//public static final Tile WATER = new AnimatedTile(5, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colours.get(-1, 004, 115, -1), 0xFF0000FF, 500);
	
	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	private int levelColour;
	
	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColour) {
		this.id = (byte) id;
		if(tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColour = levelColour;
		tiles[id] = this;
	}
	
	public byte getId() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isEmitter() {
		return emitter;
	}
	
	public int getLevelColour() {
		return levelColour;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen, Level level, int x, int y);

}
