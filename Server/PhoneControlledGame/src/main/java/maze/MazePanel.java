package maze;

import game.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MazePanel extends JPanel {
	
	private static final long serialVersionUID = 3770487044303435120L;
	
	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private final Logic logic;
	
	private final Image bushTexture;
	
	private boolean isBushTexturePresent = true;
	
	private final Image pathTexture;
	
	private boolean isPathTexturePresent = true;
	
	private final Map<Integer, Color> fieldColor = new HashMap<Integer, Color>() {

		private static final long serialVersionUID = 4753540307751746628L;

	{
		put(0, Color.WHITE);
		put(1, Color.GREEN);
		put(2, Color.RED);
		put(3, Color.BLUE);
	}};
	
	final static Color[] playerColors = new Color[]{Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.PINK};
	
	public MazePanel(Logic logic) {
		this.logic = logic;
		
		Image tmp = null;
		try {
			tmp = ImageIO.read(new File("bush.jpg"));
		} catch (IOException e) {
			isBushTexturePresent = false;
		}
		bushTexture = tmp;

		tmp = null;
		try {
			tmp = ImageIO.read(new File("path.jpg"));
		} catch (IOException e) {
			isPathTexturePresent = false;
		}
		pathTexture = tmp;
	}
	
	public void paintGame() {
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		int fieldH = fieldHeight();
		int fieldW = fieldWidth();
		
		paintMaze(fieldW, fieldH, g2d);
		paintPlayers(fieldW, fieldH, g2d);
	}
	
	private int fieldHeight() {
		return getHeight() / logic.getMaze().length;
	}
	
	private int fieldWidth() {
		return getWidth() / logic.getMaze()[0].length;
	}
	
	private void paintMaze(int width, int height, Graphics2D g2d) {
		
		int[][] maze = logic.getMaze();
		
		for (int i = 0; i < maze.length; i++) {
			int[] row = maze[i];
			
			for (int j = 0; j < row.length; j++) {
				
				if ((maze[i][j] == 0 || maze[i][j] > 1) && isPathTexturePresent) {
					g2d.drawImage(pathTexture, j * width, i * height, (j + 1) * width, (i + 1) * height, 0, 0,
							pathTexture.getWidth(null), pathTexture.getHeight(null), null);
				} else if (maze[i][j] == 1 && isBushTexturePresent) {
					g2d.drawImage(bushTexture, j * width, i * height, (j + 1) * width, (i + 1) * height, 0, 0,
							bushTexture.getWidth(null), bushTexture.getHeight(null), null);
				} else if (maze[i][j] <= 1) {
					g2d.setColor(fieldColor.get(maze[i][j]));
					g2d.fillRect(j * width, i * height, width, height);
				} 
				
				if (maze[i][j] > 1) {
					g2d.setColor(semiTransparent(0.3f, fieldColor.get(maze[i][j])));
					g2d.fillRect(j * width, i * height, width, height);
				}
				
			}
		}
	}
	
	private Color semiTransparent(float transparency, Color color) {
		return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, transparency);
	}
	
	private void paintPlayers(int fieldWidth, int fieldHeight, Graphics2D g2d) {

		for (int i = 0; i < logic.getPlayers().length; i++) {
			
			Player p = logic.getPlayers()[i];
			if (p != null) {

				int playerX = (int)(logic.getPlayerPosition().get(p).getX() * fieldWidth);
				int playerY = (int)(logic.getPlayerPosition().get(p).getY() * fieldHeight);

				g2d.setColor(playerColors[i]);
				g2d.fillOval(playerX - (fieldWidth / 4), playerY - (fieldHeight / 4), fieldWidth / 2, fieldHeight / 2);
			}
		}
	}

}
