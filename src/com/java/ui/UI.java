package com.java.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.java.image.XOR;
import com.java.text.AESText;
import com.java.text.BlowfishText;
import com.java.text.RSAText;
import com.java.text.XORText;
import com.java.image.*;

public class UI extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private String filepath;
	private String dPath;
	int returnVal;

	private String originalString;
	private byte[] encrypted;
	private byte[] decrypted;
	private long startTime, stopTime, elapsedTime1, elapsedTime2;

	JButton openButton, saveButton;

	JFileChooser fc;
	private JComboBox<String> comboBox;
	final JLabel lblEncryptionTime = new JLabel("Encryption Time");

	final JLabel lblDecryptioonTime = new JLabel("Decryptioon Time");

	final JLabel lblOriginalSize = new JLabel("Original Size");

	final JLabel lblEncryptedSize = new JLabel("Encrypted Size");

	final JLabel lblDecryptedSize = new JLabel("Decrypted Size");
	private final JLabel lblSelect = new JLabel("file is not selected!");
	private final JLabel lbldPath = new JLabel("Target");

	public UI() {

		// Create a file chooser
		fc = new JFileChooser();

		openButton = new JButton("Open a File...");
		openButton.addActionListener(this);

		saveButton = new JButton("Start");
		saveButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel();

		comboBox = new JComboBox<String>();
		buttonPanel.add(comboBox);
		comboBox.addItem("Blowfish");
		comboBox.addItem("AES");
		comboBox.addItem("XOR");
		comboBox.addItem("RSA");
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.LEADING)
				.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 450,
						GroupLayout.PREFERRED_SIZE)
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lblDecryptedSize))
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lblEncryptedSize))
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lblOriginalSize))
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lblSelect))
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lbldPath))
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lblDecryptioonTime))
				.addGroup(
						groupLayout.createSequentialGroup().addContainerGap()
								.addComponent(lblEncryptionTime)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(lblSelect)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lbldPath)
						.addPreferredGap(ComponentPlacement.RELATED, 26,
								Short.MAX_VALUE)
						.addComponent(lblEncryptionTime)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblDecryptioonTime).addGap(32)
						.addComponent(lblOriginalSize).addGap(18)
						.addComponent(lblEncryptedSize).addGap(18)
						.addComponent(lblDecryptedSize).addGap(51)));
		setLayout(groupLayout);
	}

	public boolean writeText(String string, String name) {
		try {
			String path = "/media/martuza/Application/" + name + ".txt";

			File file = new File(path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(string);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			returnVal = fc.showOpenDialog(UI.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filepath = fc.getSelectedFile().getAbsolutePath().toString();
				lblSelect.setText(filepath + " selected!");
			}

		} else if (e.getSource() == saveButton) {
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String algo = comboBox.getSelectedItem().toString();
				filepath = fc.getSelectedFile().getAbsolutePath().toString();
				int l = filepath.length();
				if (filepath.charAt(l - 1) == 't'
						&& filepath.charAt(l - 2) == 'x'
						&& filepath.charAt(l - 3) == 't') {
					BufferedReader br = null;

					try {

						String sCurrentLine;
						originalString = "";

						br = new BufferedReader(new FileReader(filepath));

						while ((sCurrentLine = br.readLine()) != null) {
							originalString += sCurrentLine;
						}
						br.close();
						if (algo == "RSA") {
							try {
								RSAText rsa1 = new RSAText();
								
								
								br = new BufferedReader(
										new FileReader(filepath));
								String path1 = "/media/martuza/Application/rsa-encrypted.txt";
								String path2 = "/media/martuza/Application/rsa-decrypted.txt";

								File file1 = new File(path1);
								File file2 = new File(path2);

								// if file doesnt exists, then create it
								if (!file1.exists()) {
									file1.createNewFile();
								}
								if (!file2.exists()) {
									file2.createNewFile();
								}

								FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
								BufferedWriter bw1 = new BufferedWriter(fw1);
								FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
								BufferedWriter bw2 = new BufferedWriter(fw2);

								elapsedTime1=0;
								elapsedTime2=0;
								while ((sCurrentLine = br.readLine()) != null) {
									if(sCurrentLine.length()>1) {
										startTime = System.currentTimeMillis();
										encrypted = rsa1.encrypt(sCurrentLine
												.getBytes());
										stopTime = System.currentTimeMillis();
										elapsedTime1+=(stopTime-startTime);
										bw1.write(new String(encrypted));
									
										startTime = System.currentTimeMillis();
										decrypted = rsa1.decrypt(encrypted);
										stopTime = System.currentTimeMillis();
										elapsedTime2+=(stopTime-startTime);
										bw2.write(new String(decrypted));
										
										System.out.println(new String(decrypted));
									}
								}
								bw1.close();
								bw2.close();
								br.close();

							} catch (Exception e2) {
								e2.printStackTrace();
							}
						} else if (algo == "Blowfish") {
							try {
								BlowfishText blowfish1 = new BlowfishText();
								startTime = System.currentTimeMillis();
								encrypted = blowfish1.encrypt(originalString
										.getBytes());
								stopTime = System.currentTimeMillis();
								writeText(new String(encrypted),
										"blowfish-encrypted");
								elapsedTime1 = stopTime - startTime;
								startTime = System.currentTimeMillis();
								decrypted = blowfish1.decrypt(encrypted);
								stopTime = System.currentTimeMillis();
								writeText(new String(decrypted),
										"blowfish-decrypted");
								elapsedTime2 = stopTime - startTime;
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						} else if (algo == "AES") {
							try {
								AESText aes1 = new AESText();
								startTime = System.currentTimeMillis();
								encrypted = aes1.encrypt(originalString
										.getBytes());
								stopTime = System.currentTimeMillis();
								writeText(new String(encrypted),
										"aes-encrypted");
								elapsedTime1 = stopTime - startTime;
								startTime = System.currentTimeMillis();
								decrypted = aes1.decrypt(encrypted);
								stopTime = System.currentTimeMillis();
								writeText(new String(decrypted),
										"aes-decrypted");
								System.out.println(new String(decrypted));
								elapsedTime2 = stopTime - startTime;
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						} else if (algo == "XOR") {
							try {
								XORText xor1 = new XORText("martuza");
								startTime = System.currentTimeMillis();
								String st = xor1.xorMessage(originalString);
								System.out.println(st);
								stopTime = System.currentTimeMillis();
								elapsedTime1 = stopTime - startTime;
								encrypted = st.getBytes();
								startTime = System.currentTimeMillis();
								writeText(new String(encrypted),
										"xor-encrypted");
								String stt = xor1.xorMessage(st);
								stopTime = System.currentTimeMillis();
								writeText(new String(decrypted),
										"xor-decrypted");
								elapsedTime2 = stopTime - startTime;
								decrypted = stt.getBytes();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
						lblEncryptionTime.setText("Encryption time = "
								+ elapsedTime1 + " milli-seconds");
						lblDecryptioonTime.setText("Decryption time = "
								+ elapsedTime2 + " milli-seconds");
						lblOriginalSize.setText("Original Text size: "
								+ originalString.getBytes().length / 1024
								+ " kb");
						lblEncryptedSize.setText("Encrypted Text size: "
								+ encrypted.length / 1024 + " kb");
						lblDecryptedSize.setText("Decrypted Text size: "
								+ decrypted.length / 1024 + " kb");

					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						try {
							if (br != null)
								br.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} else {
					if (algo == "Blowfish") {
						Blowfish blowfish = new Blowfish(filepath);
						lblEncryptionTime.setText("Encryption time = "
								+ blowfish.getEncryptTime() + " milli-seconds");
						lblDecryptioonTime.setText("Decryption time = "
								+ blowfish.getDecryptTime() + " milli-seconds");
						lblOriginalSize.setText("Original Text size: "
								+ blowfish.getOriginalSize() / 1000 + " kb");
						lblEncryptedSize.setText("Encrypted Text size: "
								+ blowfish.getEncryptedSize() / 1000 + " kb");
						lblDecryptedSize.setText("Decrypted Text size: "
								+ blowfish.getDecryptedSize() / 1000 + " kb");
					} else if (algo == "AES") {
						AES aes = new AES(filepath);
						lblEncryptionTime.setText("Encryption time = "
								+ aes.getEncryptTime() + " milli-seconds");
						lblDecryptioonTime.setText("Decryption time = "
								+ aes.getDecryptTime() + " milli-seconds");
						lblOriginalSize.setText("Original Text size: "
								+ aes.getOriginalSize() / 1000 + " kb");
						lblEncryptedSize.setText("Encrypted Text size: "
								+ aes.getEncryptedSize() / 1000 + " kb");
						lblDecryptedSize.setText("Decrypted Text size: "
								+ aes.getDecryptedSize() / 1000 + " kb");
					} else if (algo == "XOR") {
						XOR xor = new XOR(filepath);
						lblEncryptionTime.setText("Encryption time = "
								+ xor.getDuration() + " milli-seconds");
						lblDecryptioonTime.setText("Decryption time = "
								+ xor.getDuration() + " milli-seconds");
						lblOriginalSize.setText("Original Text size: "
								+ xor.getOriginalSize() / 1000 + " kb");
						lblEncryptedSize.setText("Encrypted Text size: "
								+ xor.getXORedSize() / 1000 + " kb");
						lblDecryptedSize.setText("Decrypted Text size: "
								+ xor.getOriginalSize() / 1000 + " kb");
					}
					else if(algo == "RSA") {
						RSA rsa = new RSA(filepath);
						lblEncryptionTime.setText("Encryption time = "
								+ rsa.getEncryptTime() + " milli-seconds");
						lblDecryptioonTime.setText("Decryption time = "
								+ rsa.getDecryptTime() + " milli-seconds");
						lblOriginalSize.setText("Original Text size: "
								+ rsa.getOriginalSize() / 1000 + " kb");
						lblEncryptedSize.setText("Encrypted Text size: "
								+ rsa.getEncryptedSize() / 1000 + " kb");
						lblDecryptedSize.setText("Decrypted Text size: "
								+ rsa.getDecryptedSize() / 1000 + " kb");
					}
					dPath = "/media/martuza/Application/" + algo
							+ "-decrypted.bmp";
					lbldPath.setText("Wrote to: " + dPath);
					System.out.println(filepath);
				}
			}
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Cryptography Algorithms");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new UI();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}