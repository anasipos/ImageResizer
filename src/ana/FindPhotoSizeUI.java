package ana;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FindPhotoSizeUI {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel originalWidth;
    private JTextField originalWidthText;
    private JLabel originalHeight;
    private JTextField originalHeightText;
    private JButton calculate;

    private JPanel resultsPanel;
    private JScrollPane scrollPane;

    private FindPhotoSize findSize = new FindPhotoSize();

    public FindPhotoSizeUI() {
        prepareGUI();
    }

    public static void main(String[] args) {
        FindPhotoSizeUI swingContainerDemo = new FindPhotoSizeUI();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Find photo size");
        mainFrame.setSize(600, 400);
//        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));

        JPanel origWidthPanel = new JPanel();
        origWidthPanel.setLayout(new FlowLayout());

        JPanel origHeightPanel = new JPanel();
        origHeightPanel.setLayout(new FlowLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        headerLabel = new JLabel("Please enter original image size", JLabel.LEFT);
        originalWidth = new JLabel("Original width", JLabel.LEFT);
        originalWidthText = new JTextField(6);
        originalHeight = new JLabel("Original height", JLabel.LEFT);
        originalHeightText = new JTextField(6);

        calculate = new JButton("Calculate");
        calculate.setBackground(new Color(183, 232, 183));
        calculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateSize();
            }
        });
        calculate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculateSize();
                }
            }
        });

        JButton reset = new JButton("Reset");
        reset.setBackground(new Color(232, 183, 183));
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });

        contentPanel.add(headerLabel);

        origWidthPanel.add(originalWidth);
        origWidthPanel.add(originalWidthText);
        contentPanel.add(origWidthPanel);

        origHeightPanel.add(originalHeight);
        origHeightPanel.add(originalHeightText);
        contentPanel.add(origHeightPanel);

        buttonsPanel.add(calculate);
        buttonsPanel.add(reset);
        contentPanel.add(buttonsPanel);

        contentPanel.add(scrollPane);

        mainFrame.add(contentPanel);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void calculateSize() {
        Size size = getValidInput();
        if (size.isValid()) {
            try {
                populateSizeList(findSize.calculateOptimalSizes(size));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please provide valid input", "No valid size provided", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearAll() {
        resultsPanel.removeAll();
        resultsPanel.updateUI();

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void populateSizeList(List<Size> optimalSizes) {
        resultsPanel.removeAll();
        final ArrayList<JTextField> textFields = new ArrayList<JTextField>();

        for (final Size size : optimalSizes) {

            JPanel sizePanel = new JPanel(new FlowLayout());
            final JTextField text = new JTextField(size.displaySize());
            sizePanel.add(text);
            textFields.add(text);

            JButton copy = new JButton("Copy to clipboard");
            copy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    copyToClipboard(size.toString());

                    for (JTextField textField : textFields) {
                        textField.setBackground(Color.WHITE);
                    }
                    text.setBackground(Color.ORANGE);
                }
            });
            sizePanel.add(copy);

            resultsPanel.add(sizePanel);
        }

        resultsPanel.updateUI();

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void copyToClipboard(String s) {
        StringSelection stringSelection = new StringSelection(s);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    private Size getValidInput() {
        return new Size(getIntOrZero(originalWidthText.getText()), getIntOrZero(originalHeightText.getText()), 0);
    }

    private int getIntOrZero(String text) {
        return text != null && !"".equals(text.trim()) ? Integer.parseInt(text) : 0;
    }

}