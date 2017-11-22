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

    private FindPhotoSizeUI() {
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

        final JPanel minWidthPanel = new JPanel();
        minWidthPanel.setLayout(new FlowLayout());

        final JPanel maxWidthPanel = new JPanel();
        maxWidthPanel.setLayout(new FlowLayout());

        final JPanel thresholdPanel = new JPanel();
        thresholdPanel.setLayout(new FlowLayout());

        final JLabel minWidth = new JLabel("Min width", JLabel.LEFT);
        final JTextField minWidthText = new JTextField(6);
        minWidthText.setText("630");
        minWidthPanel.add(minWidth);
        minWidthPanel.add(minWidthText);
        contentPanel.add(minWidthPanel);

        final JLabel maxWidth = new JLabel("Max width", JLabel.LEFT);
        final JTextField maxWidthText = new JTextField(6);
        maxWidthText.setText("670");
        maxWidthPanel.add(maxWidth);
        maxWidthPanel.add(maxWidthText);
        contentPanel.add(maxWidthPanel);

        final JLabel threshold = new JLabel("Threshold", JLabel.LEFT);
        final JTextField thresholdText = new JTextField(6);
        thresholdText.setText("0.1");
        thresholdPanel.add(threshold);
        thresholdPanel.add(thresholdText);
        contentPanel.add(thresholdPanel);

        headerLabel = new JLabel("Please enter original image size", JLabel.LEFT);
        originalWidth = new JLabel("Original width", JLabel.LEFT);
        originalWidthText = new JTextField(6);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                originalWidthText.requestFocus();
            }
        });
        originalHeight = new JLabel("Original height", JLabel.LEFT);
        originalHeightText = new JTextField(6);

        calculate = new JButton("Calculate");
        calculate.setBackground(new Color(183, 232, 183));
        calculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateSize(minWidthText.getText(), maxWidthText.getText(), thresholdText.getText());
            }
        });
        calculate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculateSize(minWidthText.getText(), maxWidthText.getText(), thresholdText.getText());
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

    private void calculateSize(String minWidth, String maxWidth, String threshold) {
        final Size size = new Size(getIntOrZero(originalWidthText.getText()), getIntOrZero(originalHeightText.getText()), 0);
        if (size.isValid()) {
            try {
                populateSizeList(findSize.calculateOptimalSizes(size, minWidth, maxWidth, threshold));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please provide valid input", "No valid size provided", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearAll() {
        originalWidthText.setText("");
        originalHeightText.setText("");

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

            JButton copy = new JButton("Copy CSS");
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

    private int getIntOrZero(String text) {
        return text != null && !"".equals(text.trim()) ? Integer.parseInt(text) : 0;
    }

}