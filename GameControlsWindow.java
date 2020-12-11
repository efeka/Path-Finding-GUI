import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class GameControlsWindow {

	public JFrame frame;
	private int width = 350, height = 250;

	private JButton apply, random, solve;

	public static int selected_type = Cell.BARRIER;

	JLabel lblSelectType, lblSize, lblRowCount, lblColumnCount;
	ButtonGroup group;
	JRadioButton rbBarrier, rbEntrance, rbExit;
	JComboBox rows, columns;


	public GameControlsWindow(int mainX, int mainY, int mainWidth, int mainHeight) {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.setTitle("Controls");
		frame.setResizable(false);
		frame.getContentPane().setBackground(SystemColor.controlHighlight);
		frame.setBounds(100, 100, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocation(mainX + mainWidth + 5, mainY + (mainHeight - height) / 2);

		group = new ButtonGroup();

		lblSelectType = new JLabel("Select Block Type");
		lblSelectType.setVisible(true);
		lblSelectType.setFont(new Font("", Font.BOLD, 13));
		lblSelectType.setBounds(20, 20, 150, 20);
		frame.add(lblSelectType);

		rbBarrier = new JRadioButton("Barrier       ");
		rbBarrier.setHorizontalTextPosition(SwingConstants.LEFT);
		rbBarrier.setBounds(30, 60, 90, 20);
		rbBarrier.setVisible(true);
		rbBarrier.setSelected(true);

		rbEntrance = new JRadioButton("Entrance   ");
		rbEntrance.setHorizontalTextPosition(SwingConstants.LEFT);
		rbEntrance.setBounds(30, 80, 90, 20);
		rbEntrance.setVisible(true);

		rbExit = new JRadioButton("Exit             ");
		rbExit.setHorizontalTextPosition(SwingConstants.LEFT);
		rbExit.setBounds(30, 100, 90, 20);
		rbExit.setVisible(true);

		rbBarrier.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selected_type = Cell.BARRIER;
			}
		});
		rbEntrance.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selected_type = Cell.ENTRANCE;
			}
		});
		rbExit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selected_type = Cell.EXIT;
			}
		});
		group.add(rbBarrier);
		group.add(rbEntrance);
		group.add(rbExit);

		frame.add(rbBarrier);
		frame.add(rbEntrance);
		frame.add(rbExit);
		
		lblSize = new JLabel("Change Labyrinth Size");
		lblSize.setVisible(true);
		lblSize.setFont(new Font("", Font.BOLD, 13));
		lblSize.setBounds(170, 20, 150, 20);
		frame.add(lblSize);
		
		lblRowCount = new JLabel("Row count:");
		lblRowCount.setVisible(true);
		lblRowCount.setBounds(190, 50, 100, 20);
		frame.add(lblRowCount);
		
		rows = new JComboBox();
		for (int i = 10; i <= 50; i++)
			rows.addItem(i);
		rows.setBounds(260, 51, 40, 20);
		rows.setSelectedItem(20);
		rows.setVisible(true);
		frame.add(rows);
		
		lblColumnCount = new JLabel("Column count:");
		lblColumnCount.setVisible(true);
		lblColumnCount.setBounds(170, 80, 100, 20);
		frame.add(lblColumnCount);
		
		columns = new JComboBox();
		for (int i = 10; i <= 50; i++)
			columns.addItem(i);
		columns.setBounds(260, 81, 40, 20);
		columns.setSelectedItem(20);
		columns.setVisible(true);
		frame.add(columns);
		
		apply = new JButton("Apply");
		apply.setBounds(210, 115, 70, 25);
		apply.setFont(new Font("", Font.BOLD, 11));
		apply.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				new GameMain((int) rows.getSelectedItem(), (int) columns.getSelectedItem());
			}
		});
		frame.add(apply);
		
		JSeparator horiz = new JSeparator();
		horiz.setBounds(0, 155, width, 2);
		horiz.setVisible(true);
		frame.add(horiz);
	
		random = new JButton("Generate randomly");
		random.setBounds(10, 170, 150, 25);
		random.setVisible(true);
		random.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				GameMain.randomLabyrinth();
			}
		});
		frame.add(random);
		
		solve = new JButton("Find solution");
		solve.setBounds(170, 170, 150, 25);
		solve.setVisible(true);
		frame.add(solve);
		
		frame.setVisible(true);
	}


}