package filter.basic;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import sensor.math.Vector3d;
import sensor.math.Vector3s;

public class BasicFilterGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SensorZero> listener = new ArrayList<>();

	JTextArea gyroRO = new JTextArea(), acceRO = new JTextArea();
	
	JTextArea gyro = new JTextArea(), acce = new JTextArea();

	JButton set = new JButton("set acce");
	
	public BasicFilterGUI(){
		set.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try{
					Vector3d vAcce = Vector3d.fromString(acce.getText());
					acce.setBackground(Color.white);
					for (SensorZero a : listener){
						a.setAcceZero(vAcce);
					}
				}catch(Exception e){
					acce.setBackground(Color.red);
					e.printStackTrace();
					return;
				}
			}
		});
		
		this.setLayout(new GridLayout(5, 2));
		this.add( new JLabel("zero gyro actual") );
		this.add( gyroRO );
		
		this.add( new JLabel("zero acce actual") );
		this.add( acceRO );
		
		this.add( new JLabel("set zero gyro") );
		this.add( gyro );
		
		this.add( new JLabel("set zero acce") );
		this.add( acce );
		
		this.add( new JLabel("set zeros") );
		this.add( set );
		
		this.setSize(200, 200);
	}

	public void addListener(SensorZero zero) {
		listener.add(zero);
	}

	public void setGyroZero(final Vector3s mid) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Here, we can safely update the GUI
				// because we'll be called from the
				// event dispatch thread
				gyroRO.setText(mid.toString());
			}
		});
	}

	public void setAcceZero(final Vector3d mid) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Here, we can safely update the GUI
				// because we'll be called from the
				// event dispatch thread
				acceRO.setText(mid.toString());
			}
		});
	}

}
