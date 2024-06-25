import java.io.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.*;

public class Notepad extends Frame {
    Clipboard cBoard = getToolkit().getSystemClipboard();
    TextArea tArea;
    String fName;

    Notepad() {
        GaListener gListen = new GaListener();
        addWindowListener(gListen);

        tArea = new TextArea();
        add(tArea);

        MenuBar mBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem nOption = new MenuItem("New");
        MenuItem oOption = new MenuItem("Open");
        MenuItem sOption = new MenuItem("Save");
        MenuItem cOption = new MenuItem("Close");

        nOption.addActionListener(new NewOption());
        fileMenu.add(nOption);
        oOption.addActionListener(new OpenOption());
        fileMenu.add(oOption);
        sOption.addActionListener(new SaveOption());
        fileMenu.add(sOption);
        cOption.addActionListener(new CloseOption());
        fileMenu.add(cOption);

        mBar.add(fileMenu);

        Menu editMenu = new Menu("Edit");
        MenuItem cutOption = new MenuItem("Cut");
        MenuItem pasteOption = new MenuItem("Paste");
        MenuItem copyOption = new MenuItem("Copy");

        cutOption.addActionListener(new CutOption());
        editMenu.add(cutOption);
        copyOption.addActionListener(new CopyOption());
        editMenu.add(copyOption);
        pasteOption.addActionListener(new PasteOption());
        editMenu.add(pasteOption);

        mBar.add(editMenu);
        setMenuBar(mBar);
        setTitle("Notepad in Java by Sandhya");
    }

    class GaListener extends WindowAdapter {
        public void windowClosing(WindowEvent closeNotepad) {
            System.exit(0);
        }
    }

    class NewOption implements ActionListener {
        public void actionPerformed(ActionEvent ne) {
            tArea.setText("");
        }
    }

    class OpenOption implements ActionListener {
        public void actionPerformed(ActionEvent ope) {
            FileDialog fDialog = new FileDialog(Notepad.this, "Select a text file", FileDialog.LOAD);
            fDialog.setVisible(true);

            if (fDialog.getFile() != null) {
                fName = fDialog.getDirectory() + fDialog.getFile();
                setTitle(fName);
                readFile();
            }
            tArea.requestFocus();
        }
    }

    class CloseOption implements ActionListener {
        public void actionPerformed(ActionEvent close) {
            System.exit(0);
        }
    }

    class SaveOption implements ActionListener {
        public void actionPerformed(ActionEvent save) {
            FileDialog fDialog = new FileDialog(Notepad.this, "Save file", FileDialog.SAVE);
            fDialog.setVisible(true);

            if (fDialog.getFile() != null) {
                fName = fDialog.getDirectory() + fDialog.getFile();
                setTitle(fName);
                try {
                    DataOutputStream dOutStream = new DataOutputStream(new FileOutputStream(fName));
                    String oLine = tArea.getText();
                    BufferedReader bReader = new BufferedReader(new StringReader(oLine));

                    while ((oLine = bReader.readLine()) != null) {
                        dOutStream.writeBytes(oLine + "\r\n");
                    }
                    dOutStream.close();
                } catch (Exception ex) {
                    System.out.print("Required file not found");
                }
            }
            tArea.requestFocus();
        }
    }

    class CutOption implements ActionListener {
        public void actionPerformed(ActionEvent cut_o) {
            String sText = tArea.getSelectedText();
            StringSelection sSelection = new StringSelection(sText);
            cBoard.setContents(sSelection, sSelection);
            tArea.replaceRange("", tArea.getSelectionStart(), tArea.getSelectionEnd());
        }
    }

    class CopyOption implements ActionListener {
        public void actionPerformed(ActionEvent copy_o) {
            String sText = tArea.getSelectedText();
            StringSelection cString = new StringSelection(sText);
            cBoard.setContents(cString, cString);
        }
    }

    class PasteOption implements ActionListener {
        public void actionPerformed(ActionEvent paste_o) {
            Transferable cTransfer = cBoard.getContents(Notepad.this);
            try {
                String sText = (String) cTransfer.getTransferData(DataFlavor.stringFlavor);
                tArea.replaceRange(sText, tArea.getSelectionStart(), tArea.getSelectionEnd());
            } catch (Exception exc) {
                System.out.println("Not a string flavor");
            }
        }
    }

    public void readFile() {
        BufferedReader br;
        StringBuffer sBuffer = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(fName));
            String oLine;
            while ((oLine = br.readLine()) != null)
                sBuffer.append(oLine + "\n");
            tArea.setText(sBuffer.toString());
            br.close();
        } catch (FileNotFoundException fe) {
            System.out.print("Required file not found.");
        } catch (IOException ioe) {
        }
    }

    public static void main(String args[]) {
        Frame nFrame = new Notepad();
        nFrame.setSize(600, 600);
        nFrame.setVisible(true);
    }
}
