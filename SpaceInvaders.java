import java.awt.*;
import java.util.*;

public class SpaceInvaders {
	
	public static final int PATROL_Y = 250;
	
	public static final int PATROL_SIZE = 30;
	
	public static final int ENEMY_Y = 20;
	
	public static final int ENEMY_SIZE = 20;
	
	public static final int PANEL_SIZE = 300;
	
	public static final int RIGHT_ARROW = 39;
	
	public static final int LEFT_ARROW = 37;
	
	public static final int UP_ARROW = 38;
	
	public static final int PATROL_MISSILE_LENGTH = 10;
	
	public static final int ENEMY_MISSILE_LENGTH = 10;
	
	public static final int PATROL_MISSILE_MAX = 5;
	
	public static int patrolX = 270;
	
	public static int enemyX = 0;
	
	public static int deltaX = 1;
	
	public static Random rand = new Random();
	
	public static int enemyMissileX;
	
	public static int enemyMissileY = 0;
	
	public static int[] patrolMissilesX = new int[PATROL_MISSILE_MAX];
	
	public static int[] patrolMissilesY = new int[PATROL_MISSILE_MAX];
	
	public static boolean hit = false;
	
	public static boolean enemyHit = false;
	
	public static boolean game = true;
	
	public static DrawingPanel panel = new DrawingPanel(PANEL_SIZE, PANEL_SIZE);
	
	public static Graphics g = panel.getGraphics( );
	
	public static void main(String[] args) {
	
		startGame();
		
	} // end main
	
	public static void startGame() {
		
		drawPatrolShip(Color.green); // draws patrol ship
		
		while (game) {
			
			movePatrolMissileAndDraw();
			
			panel.sleep(50);
			
			handleKeys();
			
			if (detectHit()) {
				
				hit = true;
				
			} else if (enemyHit()) {
				
				enemyHit = true;
				
				System.out.println("Player Ship Hit! Enemy Wins!");
				
				game = false;
				
			}
			
			moveEnemyShipAndDraw();
			
		} // end while loop
		
	} // end method
	
	public static void drawPatrolShip(Color color) {
		
		g.setColor(color);
		
		g.fillRect(patrolX, PATROL_Y, PATROL_SIZE, PATROL_SIZE / 3);
		
	} // end method
	
	public static void moveEnemyShipAndDraw() {
		
		int border = PANEL_SIZE - ENEMY_SIZE; // equals 280, preventing x + enemy size from being greater than panel size
		
		if ( hit ) {
			
			g.setColor(Color.black);
			
			g.fillRect(enemyX, ENEMY_Y, ENEMY_SIZE, ENEMY_SIZE / 2);
			
			g.setColor(Color.green);
			
//			g.drawString("Enemy Ship Hit!", patrolX - PATROL_SIZE, PATROL_Y + 25);
			
			System.out.println("Enemy Ship Hit! Player Wins!");
			
			game = false;
			
		} else {
			
			g.setColor(Color.WHITE); // erases ship
			
			g.fillRect(enemyX, ENEMY_Y, ENEMY_SIZE, ENEMY_SIZE / 2);
			
			g.setColor(Color.red);
			
			if ( enemyX > border || enemyX < 0) {
				
				deltaX = -deltaX; // bounces ship the opposite way
				
			} else if ( rand.nextInt(50) == 0 ){
				
				deltaX = -deltaX;
				
			} // end if
			
			enemyX += deltaX;
			
			g.fillRect(enemyX, ENEMY_Y, ENEMY_SIZE, ENEMY_SIZE / 2);
			
			fireEnemyMissile();
			
		} // end if
				
	} // end method
	
	public static void handleKeys() {
		
		/* 
				
		   panel (300px) - patrol ship (20px) = 280px
		
		   patrolX increments 3 every time. 258, 261, 264, 267, 270, ... etc.
		
		   choosing 270 as cut off, if less both arrows enabled, but if more (which should 
		
		   only happen once, at 270, right arrow disabled
		   
		*/
		
		int key = panel.getKeyCode(); // every time start game loop runs, this variable resets
		
		drawPatrolShip(Color.white);
		
		if ( key != 0 ) {
			
			if ( key == RIGHT_ARROW ) {
				
//				System.out.println("RIGHT clicked");
				
				patrolX += 3;
				
			} else if ( key == LEFT_ARROW ) {
				
//				System.out.println("LEFT clicked");
				
				patrolX -= 3;
				
			} else if ( key == UP_ARROW ) {
				
				for (int i = 0; i < PATROL_MISSILE_MAX; i++) {
					
					if ( patrolMissilesY[i] <= 0 ) {
					
						patrolMissilesX[i] = patrolX + PATROL_SIZE / 2;
					
						patrolMissilesY[i] = PATROL_Y - 1;
						
						break;
					
					} // end if
						
				} // end for
				
			} // end if
			
		} // end if
		
		drawPatrolShip(Color.green);			
		
	} // end method
	
	public static void movePatrolMissileAndDraw() {
		
		for (int i = 0; i < PATROL_MISSILE_MAX; i++) {
			
			if ( patrolMissilesY[i] <= 0 ) {
				
				patrolMissilesY[i] = 0;
				
				g.setColor(Color.WHITE); // erases missile
				
				g.drawLine(patrolMissilesX[i], patrolMissilesY[i], patrolMissilesX[i], PATROL_MISSILE_LENGTH);
				
			} else {
			
				g.setColor(Color.WHITE); // erases missile
				
				g.drawLine(patrolMissilesX[i], patrolMissilesY[i], patrolMissilesX[i], patrolMissilesY[i] - PATROL_MISSILE_LENGTH);
				
				g.setColor(Color.black);
				
				patrolMissilesY[i] -= 5;
				
				g.drawLine(patrolMissilesX[i], patrolMissilesY[i], patrolMissilesX[i], patrolMissilesY[i] - PATROL_MISSILE_LENGTH);
			
			} // end if

			
		} // end for
		
				
	} // end method
	
	public static void fireEnemyMissile() {
		
		if ( enemyMissileY >= PANEL_SIZE ) {
			
			enemyMissileX = enemyX + ENEMY_SIZE / 2;
			
			enemyMissileY = ENEMY_Y + ENEMY_SIZE / 3 + 1;
		
		}
			
		if (enemyMissileY >= PANEL_SIZE) {
			
			enemyMissileY = 0;
			
			g.setColor(Color.WHITE); // erases missile
			
			g.drawLine(enemyMissileX, enemyMissileY, enemyMissileX, PATROL_MISSILE_LENGTH);
			
		} else {
			
			g.setColor(Color.WHITE); // erases missile
			
			g.drawLine(enemyMissileX, enemyMissileY, enemyMissileX, enemyMissileY + PATROL_MISSILE_LENGTH);
			
			g.setColor(Color.red);
			
			enemyMissileY += 5;
			
			g.drawLine(enemyMissileX, enemyMissileY, enemyMissileX, enemyMissileY + PATROL_MISSILE_LENGTH);
			
		} // end if
		
	} // end method
	
	public static boolean detectHit() {
		
		boolean flag = false;
		
		for (int i = 0; i < PATROL_MISSILE_MAX; i++) {
		
			if ( (patrolMissilesX[i] >= enemyX && patrolMissilesX[i] <= enemyX + ENEMY_SIZE) && (patrolMissilesY[i] >= ENEMY_Y && patrolMissilesY[i] <= ENEMY_Y + ENEMY_SIZE / 3) ) { 
				
				flag = true;
				
				break;
				
			} else {
				
				flag = false;
				
			} // end if
		
		} // end for
		
		return flag;
		
	} // end method
	
	public static boolean enemyHit() {
		
		boolean enemyFlag = false;
		
		if ( (enemyMissileX >= patrolX && enemyMissileX <= patrolX + ENEMY_SIZE) && (enemyMissileY >= PATROL_Y && enemyMissileY <= PATROL_Y + PATROL_SIZE / 3) ) { 
			
			enemyFlag = true;
			
		} else {
			
			enemyFlag = false;
			
		} // end if
		
		return enemyFlag;
		
	}
	
} // end class