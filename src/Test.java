import java.lang.reflect.Field;

class Test {
	
	/**
	 * Tests the functionality of Sokoban (and as a result the Game API)
	 */
	@org.junit.jupiter.api.Test
	public void testSokoban() {
		try {
			Sokoban game = new Sokoban(1);
			Class<Sokoban> c = (Class<Sokoban>) game.getClass();
			
			Field playerPosX = c.getDeclaredField("playerPosX");
			Field playerPosY = c.getDeclaredField("playerPosY");
			Field placedMarkedTiles = c.getDeclaredField("placedMarkedTiles");
			playerPosX.setAccessible(true);
			playerPosY.setAccessible(true);
			placedMarkedTiles.setAccessible(true);
			
			int oldPlayerX = (int) playerPosX.get(game);
			int oldPlayerY = (int) playerPosY.get(game);
			
			//Move left and check coordinates
			game.onLeft();
			assert(oldPlayerX == (int) playerPosX.get(game));
			
			//Move up and check coordinates
			game.onUp();
			assert(oldPlayerY == (int) playerPosY.get(game)+1);
			
			//Move right and check coordinates
			game.onRight();
			assert(oldPlayerX == (int) playerPosX.get(game)-1);
			
			oldPlayerX = (int) playerPosX.get(game);
			oldPlayerY = (int) playerPosY.get(game);
			
			//Check content of Tile above
			Field CRATE = c.getDeclaredField("CRATE");
			CRATE.setAccessible(true);
			
			assert(game.tiles[oldPlayerX][oldPlayerY-1].getContent().equals(CRATE.get(game)));
			
			//Save the game and subsequently move and load 50 times, overloading test
			game.save();
			for(int i = 0; i < 50; i++) {
				game.onLeft();
				game.onUp();
				game.load();
			}
			
			//Solution to level 1
			game.onUp();game.onUp();game.onRight();game.onUp();game.onUp();game.onLeft();game.onDown();game.onDown();game.onDown();game.onUp();game.onUp();game.onLeft();game.onLeft();game.onRight();game.onRight();game.onDown();game.onDown();game.onLeft();game.onLeft();game.onLeft();game.onUp();game.onUp();game.onUp();game.onUp();game.onLeft();game.onUp();game.onRight();game.onDown();game.onDown();game.onDown();game.onRight();game.onRight();game.onRight();game.onRight();game.onUp();game.onRight();game.onRight();game.onDown();game.onLeft();game.onLeft();game.onLeft();game.onLeft();game.onLeft();game.onRight();game.onRight();game.onDown();game.onDown();game.onLeft();game.onLeft();game.onLeft();game.onUp();game.onUp();game.onUp();game.onUp();game.onDown();game.onDown();game.onRight();game.onRight();game.onRight();game.onUp();game.onUp();game.onUp();game.onRight();game.onDown();game.onDown();game.onRight();game.onDown();game.onLeft();game.onLeft();game.onLeft();game.onLeft();game.onRight();game.onRight();game.onDown();game.onDown();game.onLeft();game.onLeft();game.onLeft();game.onUp();game.onUp();game.onUp();game.onLeft();game.onUp();game.onRight();game.onDown();game.onDown();game.onDown();game.onDown();game.onRight();game.onRight();game.onDown();game.onDown();game.onRight();game.onUp();game.onUp();game.onUp();game.onRight();game.onUp();game.onLeft();game.onLeft();game.onLeft();game.onRight();game.onRight();game.onDown();game.onDown();game.onLeft();game.onLeft();game.onLeft();game.onUp();game.onUp();
			
			//Check marked boxes
			assert((int) placedMarkedTiles.get(game) == 3);
			
			game.onUp();
			
			//Check marked condition/win condition
			assert((int) placedMarkedTiles.get(game) == 4);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
