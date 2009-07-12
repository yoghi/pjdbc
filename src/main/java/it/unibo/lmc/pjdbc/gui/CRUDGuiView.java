/*
 * CRUDGuiView.java
 */

package it.unibo.lmc.pjdbc.gui;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 * The application's main frame.
 */
public class CRUDGuiView extends FrameView {

    private boolean connected = false;
    private Connection connection = null;

    public CRUDGuiView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = CRUDGuiApp.getApplication().getMainFrame();
            aboutBox = new CRUDGuiAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CRUDGuiApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jCatalogNameField = new javax.swing.JTextField();
        jCatalogField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jConnectButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jQueryTextArea = new javax.swing.JTextArea();
        jExecuteButton = new javax.swing.JButton();
        jDirChooser = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(CRUDGuiApp.class).getContext().getResourceMap(CRUDGuiView.class);
        jCatalogNameField.setText(resourceMap.getString("jCatalogNameField.text")); // NOI18N
        jCatalogNameField.setName("jCatalogNameField"); // NOI18N

        jCatalogField.setText(resourceMap.getString("jCatalogField.text")); // NOI18N
        jCatalogField.setToolTipText(resourceMap.getString("jCatalogField.toolTipText")); // NOI18N
        jCatalogField.setName("jCatalogField"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(CRUDGuiApp.class).getContext().getActionMap(CRUDGuiView.class, this);
        jConnectButton.setAction(actionMap.get("connectToDatabase")); // NOI18N
        jConnectButton.setText(resourceMap.getString("jConnectButton.text")); // NOI18N
        jConnectButton.setActionCommand(resourceMap.getString("jConnectButton.actionCommand")); // NOI18N
        jConnectButton.setName("jConnectButton"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jQueryTextArea.setColumns(20);
        jQueryTextArea.setRows(5);
        jQueryTextArea.setName("jQueryTextArea"); // NOI18N
        jScrollPane2.setViewportView(jQueryTextArea);

        jExecuteButton.setAction(actionMap.get("executeQuery")); // NOI18N
        jExecuteButton.setText(resourceMap.getString("jExecuteButton.text")); // NOI18N
        jExecuteButton.setName("jExecuteButton"); // NOI18N

        jDirChooser.setAction(actionMap.get("chooseCatalogDir")); // NOI18N
        jDirChooser.setText(resourceMap.getString("jDirChooser.text")); // NOI18N
        jDirChooser.setName("jDirChooser"); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jConnectButton)
                    .add(jExecuteButton)
                    .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, mainPanelLayout.createSequentialGroup()
                            .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel1)
                                .add(jLabel2))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jCatalogField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                .add(jCatalogNameField))
                            .add(18, 18, 18)
                            .add(jDirChooser))))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(28, 28, 28)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCatalogField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1)
                    .add(jDirChooser))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCatalogNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .add(18, 18, 18)
                .add(jConnectButton)
                .add(18, 18, 18)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jExecuteButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 619, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(54, 54, 54))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void connectToDatabase() {

        if ( !this.connected ) {

            if ( !this.jCatalogField.getText().equalsIgnoreCase("") ){

                String url = "jdbc:prolog:"+this.jCatalogField.getText();

                if ( !this.jCatalogNameField.getText().equalsIgnoreCase("") ){
                    url = url+":"+this.jCatalogNameField.getText();
                }

                try{

                    Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
                    connection = DriverManager.getConnection(url);

                } catch (Exception e){
                    e.printStackTrace();
                }

                this.jConnectButton.setText("Disconnect");
                this.connected = true;

            }

        } else {

            try {
                this.connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }

            this.jConnectButton.setText("Connect");

        }


    }

    @Action
    public void chooseCatalogDir() {

        final JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setAcceptAllFileFilterUsed(false);
        if (jfc.showOpenDialog(this.getRootPane()) == JFileChooser.APPROVE_OPTION) {
            this.jCatalogField.setText(jfc.getSelectedFile().getPath());
        }

    }

    @Action
    public void executeQuery() {

    	if ( !this.connected ){
    		JOptionPane.showMessageDialog(null, "not connected!");
    		return;
    	}
    	
        try {
            
            Statement stmt = connection.createStatement();
            //ResultSet result = stmt.executeQuery(this.jQueryTextArea.getText());
            stmt.execute(this.jQueryTextArea.getText());
            ResultSet result = stmt.getResultSet();

            String[] columnNameTmp = null;
            int columnTmp = -1;
            try {
                columnTmp = result.getMetaData().getColumnCount();
                columnNameTmp = new String[columnTmp];
                for(int i = 1; i<columnTmp+1;i++){
                    columnNameTmp[i-1] = result.getMetaData().getColumnName(i);
                }
            } catch (SQLException ex) {
            	JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            }

            this.jTable1.setModel(new DefaultTableModel(columnNameTmp, 0));
            
            try {

                DefaultTableModel modelTmp = (DefaultTableModel) this.jTable1.getModel();
                String[] objTmp = new String[columnTmp];
                while (result.next()) {
                    for (int i = 1; i < columnTmp+1; i++) {
                        objTmp[i-1] = result.getString(i);
                    }
                    modelTmp.addRow(objTmp);
                }

            } catch (SQLException ex) {
            	JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            }
            

        } catch (SQLException e){
        	JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }


    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jCatalogField;
    private javax.swing.JTextField jCatalogNameField;
    private javax.swing.JButton jConnectButton;
    private javax.swing.JButton jDirChooser;
    private javax.swing.JButton jExecuteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextArea jQueryTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
