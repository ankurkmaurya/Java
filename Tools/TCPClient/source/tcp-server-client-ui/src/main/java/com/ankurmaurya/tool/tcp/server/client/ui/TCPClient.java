
package com.ankurmaurya.tool.tcp.server.client.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TCPClient {

	private JFrame frmEpsTcpClient;
	private JTextField txtServerIP;
	private JTextField txtServerPort;
	private JTextArea textArea;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TCPClient window = new TCPClient();
					window.frmEpsTcpClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public TCPClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEpsTcpClient = new JFrame();
		frmEpsTcpClient.setTitle("EPS TCP Client");
		frmEpsTcpClient.setIconImage(Toolkit.getDefaultToolkit().getImage(TCPClient.class.getResource("/com/ankurmaurya/tool/tcp/server/client/ui/eps-logo.png")));
		frmEpsTcpClient.setBounds(100, 100, 756, 519);
		frmEpsTcpClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEpsTcpClient.getContentPane().setLayout(null);
		
		JLabel lblServerIP = new JLabel("Server IP");
		lblServerIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServerIP.setBounds(10, 27, 68, 17);
		frmEpsTcpClient.getContentPane().add(lblServerIP);
		
		txtServerIP = new JTextField();
		txtServerIP.setBounds(88, 24, 154, 20);
		frmEpsTcpClient.getContentPane().add(txtServerIP);
		txtServerIP.setColumns(20);
		txtServerIP.setText("127.0.0.1");
		
		JLabel lblServerPort = new JLabel("Port No.");
		lblServerPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServerPort.setBounds(10, 51, 68, 20);
		frmEpsTcpClient.getContentPane().add(lblServerPort);
		
		txtServerPort = new JTextField();
		txtServerPort.setBounds(88, 51, 154, 20);
		frmEpsTcpClient.getContentPane().add(txtServerPort);
		txtServerPort.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(174, 82, 68, 26);
		
		frmEpsTcpClient.getContentPane().add(btnSend);
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 122, 699, 337);
		frmEpsTcpClient.getContentPane().add(scrollPane);
		

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true);
		textArea.setTabSize(3);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Server Message", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(259, 21, 470, 93);
		frmEpsTcpClient.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 458, 66);
		panel.add(scrollPane_1);
		
		JTextArea textAreaServerMsg = new JTextArea();
		scrollPane_1.setViewportView(textAreaServerMsg);
		textAreaServerMsg.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textAreaServerMsg.setLineWrap(true);
		textAreaServerMsg.setTabSize(1);
		textAreaServerMsg.setText("");
		
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(88, 82, 68, 26);
		frmEpsTcpClient.getContentPane().add(btnClear);
		
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				textAreaServerMsg.setText("");
			}
		});
		
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String serverIP = txtServerIP.getText();
				String serverPort = txtServerPort.getText();
				textArea.append(System.lineSeparator());
				textArea.append("Connecting to IP '");
				textArea.append(serverIP);
				textArea.append("' with Port '");
				textArea.append(serverPort + "'");
				textArea.append(System.lineSeparator());
				textArea.repaint();
				
				ServerClient sc = new ServerClient(
						serverIP, 
						Integer.parseInt(serverPort), 
						textArea, 
						textAreaServerMsg.getText());
				
				Thread clientThread = new Thread(sc);
				clientThread.start();
			}
		});


	}
}







