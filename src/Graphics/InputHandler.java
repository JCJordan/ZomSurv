package Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
	
	public InputHandler(GraphicsManager gm) {
		gm.addKeyListener(this);
	}
	
	//Data about keys
	public class Key {
		public int numTimesPressed = 0;
		public boolean pressed = false;
		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		public void toggle(boolean isPressed) {
			pressed = isPressed;
			if(isPressed) numTimesPressed++;
		}
	}
	
	//public List<Key>keys = new ArrayList<Key>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key up2 = new Key();
	public Key down2 = new Key();
	public Key left2 = new Key();
	public Key right2 = new Key();	
	public Key space = new Key();
	public Key a = new Key();
	public Key b = new Key();
	public Key c = new Key();
	
	//Key events
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);		
	}
	
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);		
	}
	
	public void keyTyped(KeyEvent e) {
				
	}
	
	//Keys used/toggles
	public void toggleKey(int keyCode, boolean isPressed) {
		if( keyCode == KeyEvent.VK_UP) { 
			up.toggle(isPressed); }		
		if( keyCode == KeyEvent.VK_DOWN) { 
			down.toggle(isPressed); }		
		if( keyCode == KeyEvent.VK_LEFT) { 
			left.toggle(isPressed); }		
		if( keyCode == KeyEvent.VK_RIGHT) { 
			right.toggle(isPressed); }		
		if(keyCode == KeyEvent.VK_W ) { 
			up2.toggle(isPressed); }		
		if(keyCode == KeyEvent.VK_S ) { 
			down2.toggle(isPressed); }		
		if(keyCode == KeyEvent.VK_A ) { 
			left2.toggle(isPressed); }		
		if(keyCode == KeyEvent.VK_D ) { 
			right2.toggle(isPressed); }		
		if(keyCode == KeyEvent.VK_SPACE ) { 
			space.toggle(isPressed); }	
		if(keyCode == KeyEvent.VK_A ) { 
			a.toggle(isPressed); }	
		if(keyCode == KeyEvent.VK_B ) { 
			b.toggle(isPressed); }	
		if(keyCode == KeyEvent.VK_C ) { 
			c.toggle(isPressed); }	
	}	
}
