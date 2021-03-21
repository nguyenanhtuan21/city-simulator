package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.helper.DocumentOutputStream;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Console extends JInternalFrame {
    private static boolean console_flag;

    static {
        Console.console_flag = true;
    }

    private JTextComponent outputArea;
    private JViewport viewPort;
    private JButton clear;

    public Console(final String title, final FileOutputStream fileOutputStream, final String s) {
        super(title);
        this.outputArea = new JTextPane();
        this.clear = new JButton("Clear");
        this.setIconifiable(true);
        this.setFrameIcon(new ImageIcon());
        this.outputArea.setEditable(true);
        final Document document = this.outputArea.getDocument();
        final SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, Color.black);
        System.setOut(new PrintStream(new DocumentOutputStream(document, a.copyAttributes(), fileOutputStream, s, this, false)));
        a.removeAttributes(a);
        StyleConstants.setForeground(a, Color.red);
        StyleConstants.setBold(a, true);
        System.setErr(new PrintStream(new DocumentOutputStream(document, a.copyAttributes(), null, null, this, true)));
        final JScrollPane comp = new JScrollPane(this.outputArea);
        this.viewPort = comp.getViewport();
        this.getContentPane().add(comp, "Center");
        this.clear.setMargin(new Insets(0, 2, 0, 2));
        this.clear.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    document.remove(0, document.getLength());
                    Console.this.outputArea.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.getContentPane().add(this.clear, "North");
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, screenSize.height - 200, 900, Math.min(200, screenSize.height - 50));
    }

    public void update(final Graphics g) {
        this.paint(g);
    }

    public void scrollToBottom() {
        if (this.viewPort == null) {
            return;
        }
        final Rectangle bounds = this.viewPort.getBounds();
        final int height = this.outputArea.getHeight();
        if (height > bounds.height) {
            final Rectangle rectangle = bounds;
            rectangle.y += height - bounds.height;
            this.viewPort.scrollRectToVisible(bounds);
        }
    }

    public boolean getConsoleFlag() {
        return Console.console_flag;
    }

    public void setConsoleFlag(final boolean console_flag) {
        Console.console_flag = console_flag;
    }

    private void close() {
        this.setVisible(false);
        Console.console_flag = false;
    }

    public void clear() {
        this.clear.doClick();
    }
}

