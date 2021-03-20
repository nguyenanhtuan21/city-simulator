package com.natuan.citysimulator.helper;

import com.natuan.citysimulator.GUI.Console;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import java.io.*;

public class DocumentOutputStream extends OutputStream {
    private static PrintStream pStream;
    private byte[] one;
    private Document doc;
    private AttributeSet attr;
    private Console console;
    private Runnable scrollToBottom;
    private boolean showConsole;

    public DocumentOutputStream(final Document doc, final AttributeSet attr, final FileOutputStream fileOutputStream, final String s, final Console console, final boolean showConsole) {
        this.one = new byte[1];
        this.showConsole = false;
        this.doc = doc;
        this.attr = attr;
        this.console = console;
        if (fileOutputStream != null) {
            try {
                fileOutputStream.flush();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    doc.insertString(doc.getLength(), line + "\n", attr);
                }
                bufferedReader.close();
                DocumentOutputStream.pStream = new PrintStream(new FileOutputStream(s, true));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (console != null) {
            this.scrollToBottom = new Runnable() {
                public void run() {
                    console.scrollToBottom();
                }
            };
            this.showConsole = showConsole;
        }
    }

    public DocumentOutputStream(final Document document) {
        this(document, null, null, null, null, false);
    }

    public void write(final int n) throws IOException {
        this.one[0] = (byte) n;
        this.write(this.one, 0, 1);
    }

    public void write(final byte[] array, final int n, final int n2) throws IOException {
        try {
            this.doc.insertString(this.doc.getLength(), new String(array, n, n2), this.attr);
            if (DocumentOutputStream.pStream != null) {
                DocumentOutputStream.pStream.write(array, n, n2);
            }
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
        if (this.scrollToBottom != null) {
            SwingUtilities.invokeLater(this.scrollToBottom);
        }
    }
}

