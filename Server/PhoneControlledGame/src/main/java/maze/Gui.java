package maze;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Gui extends JFrame {
	
	private static final long serialVersionUID = -8195202833752943099L;
	
	private final Logic logic;
	
	private final MazePanel panel;
	
	public Gui(Logic logic) {
		this.logic = logic;
		this.panel = new MazePanel(this.logic);
	}
	
	public void refresh() {
		
		paintGame();
	}

	public void init() {
		initGui();
		paintGame();
		showGui();
	}
	
	private void showGui() {
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				setVisible(true);
				
			}
			
		});
	}
	
	private void initGui() {
		
		setTitle("Maze");
		setSize(800, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(panel);
	}
	
	private void paintGame() {
		panel.paintGame();
	}
	
}
