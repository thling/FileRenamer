/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package renamer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author sam
 */
public class FileRenamer extends javax.swing.JFrame {
    private final String FILE_S = System.getProperty("file.separator");
    private String work_dir = System.getProperty("user.dir");
    private HashMap<String, String> renameMapping;
    
    
    /**
     * Creates new form FileRenamer
     */
    public FileRenamer() {
        initComponents();
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        
        setLocation(
                d.width / 2 - getWidth() / 2,
                d.height / 2 - getHeight() / 2
        );
        
        this.txtDirectory.setText(work_dir);
    }
    
    /**
     * List the files in the list view, indicating the old file names and new file names.
     */
    private void listFiles() {
        ArrayList<String> fileList = new ArrayList<>();
        
        for (String i : renameMapping.keySet()) {
            fileList.add(i + " ==> " + renameMapping.get(i));
        }
        
        this.lstFileList.removeAll();
        this.lstFileList.setListData(fileList.toArray(new String[0]));
        this.lstFileList.doLayout();
        this.scrpnlFileList.doLayout();
    }
    
    /**
     * Attempts to look for file number in the old file name.\
     * 
     * @param fname The string to search file number from
     * @return String containing the file number found
     */
    private String findNumber(String fname) {
        char[] f = fname.toCharArray();
        
        for (int i = 0; i < f.length; i++) {
            if (Character.isDigit(f[i])) {
                if (i + 1 < f.length && Character.isDigit(f[i + 1])) {
                    boolean match = false;
                    
                    // Finds based on the following format:
                    //   1. ...[[file_number]]...
                    //   2. ...[[file_number]v[version_number]]...
                    if (i + 2 < f.length && f[i + 2] != 'v' && !Character.isDigit(f[i + 2])) {
                        match = true;
                    } else if (i + 4 < f.length && f[i + 2] == 'v' && Character.isDigit(f[i + 3]) && !Character.isDigit(f[i + 4])) {
                        match = true;
                    }
                    
                    if (match) {
                        return (Character.toString(f[i]) + Character.toString(f[i + 1]));
                    }
                }
            }
        }
        
        return null;
    }

    /**
     * Scans the directory listed in the Directory text field
     * and hashes the file into the HashMap with its possible new name.
     */
    private void scanDirectory() {
        String directory = this.txtDirectory.getText();
        
        if (directory.length() <= 0) {
            return;
        }
        
        File[] dir = new File(directory).listFiles();
        
        // Hashes the files based on the file modified date, ascending
        Arrays.sort(dir, new Comparator<File>(){
            @Override
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });
        
        // Dumps the hash map to avoid residue entries
        renameMapping = new HashMap<>();
        
        String fillPrefix = this.txtPrefix.getText();
        int fillDigit = Integer.parseInt(this.spnDigitFill.getValue().toString());
        
        int fileNumber = 1;
        for (int i = 0; i < dir.length; i++) {
            if (dir[i].isDirectory()) {
                continue;
            }
            
            String fill = fillPrefix;
            
            // fillAmount: the length of the file number strings
            // Example:
            //   fillAmount = 4:
            //     file number   1 = 0001
            //     file number 232 = 0232
            int fillAmount = fillDigit - Integer.toString(fileNumber).length();
            
            for (int j = 0; j < fillAmount; j++) {
                fill = fill + "0";
            }
            
            // Gets the file extension
            String fileExt =
                    dir[i].getName().substring(dir[i].getName().lastIndexOf("."));
            
            String findMatch = null;
            
            // See if the user wants to find matching file number
            if (this.chkFileNameMatch.isSelected()) {
                findMatch = this.findNumber(dir[i].getName());
            }
            
            if (findMatch != null) {
                renameMapping.put(dir[i].getName(), findMatch + fileExt);
            } else {
                renameMapping.put(dir[i].getName(), fill + fileNumber + fileExt);
                fileNumber++;
            }
        }
        
        // Displays the scanned files and their new names
        this.listFiles();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblDirectory = new javax.swing.JLabel();
        txtDirectory = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        scrpnlFileList = new javax.swing.JScrollPane();
        lstFileList = new javax.swing.JList();
        btnRename = new javax.swing.JButton();
        lblPrefix = new javax.swing.JLabel();
        txtPrefix = new javax.swing.JTextField();
        lblDigitFill = new javax.swing.JLabel();
        spnDigitFill = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        chkFileNameMatch = new javax.swing.JCheckBox();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Renamer");

        lblDirectory.setText("Directory:");

        txtDirectory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDirectoryKeyReleased(evt);
            }
        });

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        lstFileList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lstFileListKeyReleased(evt);
            }
        });
        scrpnlFileList.setViewportView(lstFileList);

        btnRename.setText("Rename");
        btnRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenameActionPerformed(evt);
            }
        });

        lblPrefix.setText("Prefix");

        lblDigitFill.setText("Digit FIll");

        spnDigitFill.setValue(2);

        chkFileNameMatch.setSelected(true);
        chkFileNameMatch.setText("Enable File Name Match");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrpnlFileList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRename))
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPrefix)
                            .addComponent(lblDigitFill))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spnDigitFill, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRefresh))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDirectory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDirectory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowse)
                        .addGap(18, 18, 18)
                        .addComponent(chkFileNameMatch)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDirectory)
                        .addComponent(txtDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBrowse))
                    .addComponent(chkFileNameMatch, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrpnlFileList, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPrefix)
                    .addComponent(txtPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDigitFill)
                    .addComponent(spnDigitFill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRename)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.showOpenDialog(this);
        
        File dir = fileChooser.getSelectedFile();
        
        if (dir == null) {
            return;
        }
        
        this.txtDirectory.setText(dir.getAbsolutePath());
        this.work_dir = dir.getAbsolutePath();
        this.scanDirectory();
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void txtDirectoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDirectoryKeyReleased
        if (evt.getKeyChar() == java.awt.event.KeyEvent.VK_ENTER) {
            this.work_dir = this.txtDirectory.getText();
            this.scanDirectory();
        }
    }//GEN-LAST:event_txtDirectoryKeyReleased

    private void lstFileListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lstFileListKeyReleased
        if (evt.getKeyChar() == java.awt.event.KeyEvent.VK_DELETE) {
            List ls = this.lstFileList.getSelectedValuesList();
            
            for (int i = 0; i < ls.size(); i++) {
                String item = (String)ls.get(i);
                item = item.substring(0, item.indexOf(" ==>"));
                
                this.renameMapping.remove(item);
            }
            
            this.listFiles();
        }
    }//GEN-LAST:event_lstFileListKeyReleased

    private void btnRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenameActionPerformed
        String p = this.work_dir;
        if (!p.endsWith(FILE_S)) {
            p = p + FILE_S;
        }
        
        for (String k : renameMapping.keySet()) {
            String v = renameMapping.get(k);
            
            
            File old_f = new File(p + k);
            File new_f = new File(p + v);
            
            old_f.renameTo(new_f);
        }
        
        // Disposes entire window to avoid double rename
        // Debating: to quit or not to quit...?
        JOptionPane.showMessageDialog(this, "Rename Completed! Click to quit.");
        dispose();
    }//GEN-LAST:event_btnRenameActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        this.work_dir = this.txtDirectory.getText();
        this.scanDirectory();
    }//GEN-LAST:event_btnRefreshActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println(e.getMessage());
        }
        
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileRenamer().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRename;
    private javax.swing.JCheckBox chkFileNameMatch;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDigitFill;
    private javax.swing.JLabel lblDirectory;
    private javax.swing.JLabel lblPrefix;
    private javax.swing.JList lstFileList;
    private javax.swing.JScrollPane scrpnlFileList;
    private javax.swing.JSpinner spnDigitFill;
    private javax.swing.JTextField txtDirectory;
    private javax.swing.JTextField txtPrefix;
    // End of variables declaration//GEN-END:variables
}
