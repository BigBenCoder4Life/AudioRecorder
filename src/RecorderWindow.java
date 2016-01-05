import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;


/**
 * This class is used for UI purposes.
 * 
 * @author Ben Kirtley
 * @version 1.0
 * @date 3/7/14
 * 
 */
public class RecorderWindow extends JFrame
{
    private JFrame mainFrame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu optionMenu;
    private JMenu helpMenu;
    private JPanel panel;
    private JPanel panelImg;
    private JButton playButton;
    static JButton stopButton;
    private JButton recordButton;
    private JLabel picLabel;
    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 425;
    private JMenuItem exitItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem aboutUsItem;
    private JMenuItem helpItem;
    private JCheckBoxMenuItem loopItem;
    private JMenuItem imageItem;
    private JCheckBoxMenuItem stopItem;
    private Audio system = null;
    private boolean running;
    JavaSoundRecorder recorder = null;
    FileNameExtensionFilter filter;
    final Color color = Color.LIGHT_GRAY;
    String fileName = "tiger.jpg";
    Thread recordThread;
    boolean recording = false;

    /*
     * Constructor for the Audio System.
     */
    public RecorderWindow()
    {
        // JPanel background = new JPanel(new BorderLayout());

        // background.setBackground(color);

        mainFrame = new JFrame();

        mainFrame.setLayout(new BorderLayout());

        mainFrame.setTitle("Music Recorder");

        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildMenuBar();

        buildPanel();

        mainFrame.add(panelImg, BorderLayout.NORTH);
        mainFrame.add(panel, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    /*
     * To build the menu bar.
     */
    private void buildMenuBar()
    {
        menuBar = new JMenuBar();

        buildFileMenu();
        buildOptionMenu();
        buildHelpMenu();

        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    /*
     * To build the JFrame panel.
     */
    public void buildPanel()
    {
        playButton = new JButton("Play");
        playButton.addActionListener(new ButtonListener());
        playButton.setToolTipText("Click here to play a sound file");
        playButton.setBackground(Color.LIGHT_GRAY);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ButtonListener());
        stopButton.setToolTipText("Click here to stop playing a sound file.");
        stopButton.setBackground(Color.LIGHT_GRAY);

        recordButton = new JButton("Record");
        recordButton.addActionListener(new ButtonListener());
        recordButton.setToolTipText("Click here to start recording a sound file.");
        recordButton.setBackground(Color.LIGHT_GRAY);


        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Resources/skull.jpg"));

        picLabel = new JLabel(img);

        panelImg = new JPanel();

        panel = new JPanel();
        panel.setBackground(color);

        panelImg.add(picLabel);
        panel.add(recordButton);
        panel.add(playButton);
        panel.add(stopButton);
    }

    /*
     * To build the file menu.
     */
    public void buildFileMenu()
    {
        fileMenu = new JMenu("File");

        exitItem = new JMenuItem("Exit");
        openItem = new JMenuItem("Open File...");
        saveItem = new JMenuItem("Save File...");

        openItem.addActionListener(new openListener());
        saveItem.addActionListener(new saveListener());
        exitItem.addActionListener(new exitListener());

        exitItem.setMnemonic(KeyEvent.VK_X);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
    }

    /*
     * To build the option menu.
     */
    public void buildOptionMenu()
    {
        optionMenu = new JMenu("Options");

        loopItem = new JCheckBoxMenuItem("Loop", false);
        loopItem.addActionListener(new loopListener());

        imageItem = new JMenuItem("Alter Image...");
        imageItem.addActionListener(new imageListener());

        stopItem = new JCheckBoxMenuItem("Auto");
        stopItem.addActionListener(new autoStopListener());

        optionMenu.add(imageItem);
        optionMenu.addSeparator();
        optionMenu.add(stopItem);
        optionMenu.add(loopItem);
    }

    /*
     * To build the help menu
     */
    public void buildHelpMenu()
    {
        helpMenu = new JMenu("Help");

        aboutUsItem = new JMenuItem("About Developer");
        aboutUsItem.addActionListener(new aboutUsListener());

        helpItem = new JMenuItem("Help");
        helpItem.addActionListener(new helpListener());
        
        helpMenu.add(helpItem);
        helpMenu.add(aboutUsItem);

    }

    /*
     * This is a class to handle a exit listener
     */
    private class exitListener implements ActionListener
    {
        /*
         * To perform a action on activation of button.
         */
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    /*
     * This is a private inner class to handle a loop listener
     */
    private class loopListener implements ActionListener
    {
        /*
         * To perform a action on activation of button.
         */
        public void actionPerformed(ActionEvent e)
        {
            if (isavailable())
            {
                System.out.println("Selected Looping for files");

                if (loopItem.isSelected())
                    system.loop();
                else
                {
                    system.stop();
                }
            }
        }
    }

    /*
     * This is a private inner class to handle a stop listener
     */
    private class autoStopListener implements ActionListener
    {
        /*
         * To perform a action on activation of button.
         */
        public void actionPerformed(ActionEvent e)
        {
            if (stopItem.isSelected())
            {
                recordButton.doClick();
                System.out.println("Auto recording started");
            }
            else
            {

                    if (recorder != null)
                    {
                      stopButton.doClick();
                    }
                        
                    if (isavailable())
                        system.stop();
            }
        }
    }

    /*
     * This is a private inner class to handle a image listener
     */
    private class imageListener implements ActionListener
    {
        /*
         * To perform a action on activation of button.
         */
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose an Image file");
            int status = fileChooser.showOpenDialog(null);

            if (status == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getPath();
                ImageIcon img = new ImageIcon(fileName);
                picLabel.setIcon(img);
                mainFrame.revalidate();
                mainFrame.pack();

            }
        }
    }

    /*
     * This is a private inner class to handle opening a file.
     */
    private class openListener implements ActionListener
    {
        /*
         * To perform a action on activation of button.
         */
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);

            filter = new FileNameExtensionFilter(".wav file", "wav");
            fileChooser.addChoosableFileFilter(filter);
            int status = fileChooser.showOpenDialog(null);

            if (status == JFileChooser.APPROVE_OPTION)
            {
                if (running)
                {
                    system.close();
                    system.stop();
                    running = false;
                }

                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getAbsolutePath();
                system = new Audio(fileName);
                running = true;
            }
        }
    }

    /*
     * This is a private inner class to handle saving a file.
     */
    private class saveListener implements ActionListener
    {
        /*
         * To perform a action on activation of button.
         */
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser saveFileChooser = new JFileChooser();
            int status = saveFileChooser.showSaveDialog(null);

            if (status == JFileChooser.APPROVE_OPTION)
            {
                File saveFile = saveFileChooser.getSelectedFile();

                JavaSoundRecorder.setFilePath(saveFile.getAbsolutePath());

                Thread saveThread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            if (recorder != null)
                            {
                                recorder.save(JavaSoundRecorder.wavFile);
                                System.out.println("Audio file saved");
                                recorder = null;
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

                saveThread.start();
            }
            else if (status == JFileChooser.CANCEL_OPTION || status == JFileChooser.ERROR_OPTION)
            {
                System.out.println("Recording canceled");
                recorder = null;
            }
        }
    }

    /*
     * This class is for play, stop, loop listeners
     */
    private class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == playButton)
            {
                setColor(color, new Color(89, 106, 53), color);

                if (isavailable())
                {
                    system.play();
                    System.out.println("Audio playing...");
                }

            }
            else if (e.getSource() == stopButton)
            {
                setColor(color, color, new Color(200, 100, 100));

                if (isavailable())
                {
                  system.stop();
                  System.out.println("Audio stopped...");  
                }
                    

                if (recorder != null)
                {
                    Thread stopThread = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                System.out.println("Stopped recording");
                                recorder.stop();
                                saveItem.doClick();
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });

                    stopThread.start();

                }
            }
            else if (e.getSource() == recordButton)
            {
                if (recorder == null)
                    recorder = new JavaSoundRecorder();

                setColor(new Color(149, 24, 11), color, color);

                recordThread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            if (!recorder.lineOpen())
                            {
                                System.out.println("Started recording...");
                                recorder.start(); 
                            }
                        }
                        catch (LineUnavailableException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
                recordThread.start();
            }
        }

    }

    /*
     * This inner class is for the about us menu item.
     */
    private class aboutUsListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JFrame aboutUsFrame = new JFrame();

            aboutUsFrame.setLayout(new BorderLayout());

            aboutUsFrame.setTitle("About Developer");

            aboutUsFrame.setSize(275, 200);

            aboutUsFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JTextArea textArea = new JTextArea(10, 10);
            String text = "Developer: Ben Kirtley \nVersion: 1.0\nEmail: kirtlepb@dukes.jmu.edu\nRelease Month: May 2014";
            textArea.setEditable(false);
            textArea.setText(text);

            aboutUsFrame.add(textArea);

            aboutUsFrame.setResizable(false);
            aboutUsFrame.setLocationRelativeTo(mainFrame);
            aboutUsFrame.setVisible(true);
        }
    }

    /*
     * This inner class is for the about us menu item.
     */
    private class helpListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JFrame helpFrame = new JFrame();

            helpFrame.setLayout(new BorderLayout());

            helpFrame.setTitle("Help");

            helpFrame.setSize(475, 280);

            helpFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JTextArea textArea = new JTextArea(50, 50);
            
            String greeting = "\t  Welcome to the help menu for your Audio device.";
            
            String playAudio = "\n\nTo play audio a file:\n"
                    + "1. Click File at top of window\n2. Click open file...\n3. Open an audio wave file with extension .wav only"
                    + "\n4. Click Play button at bottom of device.";
            
            String recordAudio = "\n\nTo record an audio file :\n"
                    + "1. Click record on bottom of device.\n2. Click stop when done recording audio."
                    + "\n3. Wait for save dialog window to open...\n4. Type Audio file name into File Name text area with .wav extension."
                    + "\n5. Example: audioFile.wav\n6. Click save at bottom of save dialog window.";  
           
            textArea.setEditable(false);
            textArea.setText(greeting + playAudio + recordAudio); 
            
            helpFrame.add(textArea);
            helpFrame.setResizable(false);
            helpFrame.setLocationRelativeTo(mainFrame);
            helpFrame.setVisible(true);
        }
    }
    
    /*
     * To determine if a sound file is loaded.
     */
    public boolean isavailable()
    {
        if (system != null && system.isavailable())
            return true;
        else
            return false;
    }

    /*
     * set the color of the play button.
     */
    void setColor(Color colorRecord, Color colorPlay, Color colorStop)
    {
        recordButton.setBackground(colorRecord);
        playButton.setBackground(colorPlay);
        stopButton.setBackground(colorStop);
        mainFrame.revalidate();
    }
}
