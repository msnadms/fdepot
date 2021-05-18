import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FDepotGUI extends JComponent implements Runnable {

    FDepotGUI fd;
    JFileChooser dirGet;
    JLabel destDirect;
    HoverButton snagDir;
    JLabel listLabel;
    JLabel messageLabel;
    JTextField enterExt;
    HoverButton submitExt;
    HoverButton sort;
    HoverButton leave;

    String sourceDirectory;
    String[] extensions;

    StringBuilder cmdArguments;

    public FDepotGUI() {



    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new FDepotGUI());

    }

    public void run(){

        JFrame mainFrame = new JFrame();
        Container content = mainFrame.getContentPane();
        content.setLayout(new BorderLayout());
        fd = new FDepotGUI();
        content.add(fd, BorderLayout.CENTER);

        JTabbedPane tp = new JTabbedPane();
        content.add(tp, BorderLayout.CENTER);

        ImageIcon prgIcon = new ImageIcon("prgico.png");
        mainFrame.setIconImage(prgIcon.getImage());

        Color schmlue = new Color(3, 123, 252);

        ImageIcon direcImg = new ImageIcon("folder.png");
        ImageIcon enterImg = new ImageIcon("enter.png");


        snagDir = new HoverButton(direcImg, schmlue, new Color(0, 103, 232));
        snagDir.setFocusPainted(false);
        dirGet = new JFileChooser();
        submitExt = new HoverButton(enterImg, new Color(24, 201, 255), new Color(4, 180, 235));
        submitExt.setFocusPainted(false);
        leave = new HoverButton(" ", new Color(193, 42, 42), new Color(173, 22, 22));


        JPanel sortTab = new JPanel(new BorderLayout());
        tp.addTab("Sort", sortTab);

        JPanel execPanel = new JPanel(new GridLayout(2, 0));
        JPanel nwPanel = new JPanel(new GridLayout(4, 1));
        execPanel.add(nwPanel);
        JPanel spanel = new JPanel(new BorderLayout());
        sortTab.add(spanel, BorderLayout.SOUTH);
        spanel.add(snagDir, BorderLayout.NORTH);
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBackground(schmlue);
        messageLabel.setOpaque(true);
        sortTab.add(messageLabel, BorderLayout.NORTH);
        destDirect = new JLabel();
        sortTab.add(destDirect, BorderLayout.WEST);
        destDirect.setBackground(new Color(3, 152, 252));
        destDirect.setOpaque(true);

        ImageIcon sortImg = new ImageIcon("paper.png");

        sort = new HoverButton(sortImg, new Color(3, 181, 252), new Color(0, 161, 232));
        sortTab.add(sort, BorderLayout.CENTER);
        sort.setFocusPainted(false);
        spanel.add(leave, BorderLayout.SOUTH);
        leave.setFocusPainted(false);

        JPanel choosePanel = new JPanel(new GridLayout(2, 0));
        sortTab.add(choosePanel, BorderLayout.EAST);
        JPanel nePanel = new JPanel(new GridLayout(2,0));
        choosePanel.add(nePanel);
        enterExt = new JTextField(10);
        nePanel.add(submitExt);
        nePanel.add(enterExt);
        listLabel = new JLabel("Enter file extensions", SwingConstants.CENTER);
        choosePanel.add(listLabel);
        enterExt.setOpaque(true);
        enterExt.setBackground(Color.lightGray);

        listLabel.setOpaque(true);
        listLabel.setBackground(Color.lightGray);

        snagDir.addActionListener(actionListener);
        submitExt.addActionListener(actionListener);
        sort.addActionListener(actionListener);
        leave.addActionListener(actionListener);
        dirGet.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        sort.setBorder(BorderFactory.createEmptyBorder());
        submitExt.setBorder(BorderFactory.createEmptyBorder());
        snagDir.setBorder(BorderFactory.createEmptyBorder());
        leave.setBorder(BorderFactory.createEmptyBorder());
        enterExt.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));
        listLabel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainFrame.setResizable(false);
        mainFrame.setSize(500, 500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == snagDir) {
                int valFile = dirGet.showOpenDialog(FDepotGUI.this);
                if (valFile == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = dirGet.getSelectedFile();
                    sourceDirectory = selectedFile.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\") + "\\\\";
                    String[] disDir = sourceDirectory.split("\\\\\\\\");
                    destDirect.setText("<html>&rarr; " + disDir[0] + "&nbsp;<br>");
                    for (String s : disDir) {
                        if (!s.equals(disDir[0])) {
                            destDirect.setText(destDirect.getText() + "&nbsp;&nbsp;&darr; " + s + "&nbsp;<br>");
                        }
                    }
                    destDirect.setText(destDirect.getText() + "</html>");
                    destDirect.setFont(new Font("Courier", Font.PLAIN, 16));
                }
            } else if (e.getSource() == submitExt) {
                extensions = enterExt.getText().split(" ");
                listLabel.setText("<html>");
                for (String s : extensions) {
                    if (s.indexOf('.') != 0) {
                        listLabel.setText("<html> Improper format!");
                        extensions = null;
                    } else {
                        listLabel.setText(listLabel.getText() + "<br>" + s);
                    }
                }
                listLabel.setText(listLabel.getText() + "</html>");
            } else if (e.getSource() == sort) {
                if (extensions == null || sourceDirectory == null) {
                    messageLabel.setText("Failure.");
                    return;
                }
                cmdArguments = new StringBuilder();
                cmdArguments.append(sourceDirectory);
                for (String s : extensions) {
                    String ta = " " + s;
                    cmdArguments.append(ta);
                }
                beginSort();
                messageLabel.setText("Success!");
            } else if (e.getSource() == leave) {
                cmdArguments = null;
                sourceDirectory = null;
                extensions = null;
                destDirect.setText("");
                enterExt.setText("");
                messageLabel.setText("");
                listLabel.setText("Enter file extentions");
            }
            if (extensions != null && sourceDirectory != null) {
                sort.setBorder(BorderFactory.createLineBorder(new Color(42, 212, 119), 2));
            } else {
                sort.setBorder(BorderFactory.createEmptyBorder());
            }
        }
    };

    public void beginSort(){
        try {
            Process d = Runtime.getRuntime().exec("./fdepotx " + cmdArguments.toString());
            System.out.println("Process :: " + d);
        } catch (IOException ioe) {
            messageLabel.setText(">.. -ERR: C EXECUTABLE MISPLACED. OUTCOME: YOU'RE FUCKING STUPID.");
        }
    }
}