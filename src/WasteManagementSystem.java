import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class WasteManagementSystem extends JFrame {
    private JTabbedPane tabbedPane;
    private DefaultTableModel binTableModel;
    private DefaultTableModel truckTableModel;
    private DefaultTableModel routeTableModel;
    private JTextArea alertArea;
    private JTextArea reportArea;
    private JProgressBar systemStatusBar;
    private JLabel statusLabel;

    // Data structures to simulate system components
    private List<WasteBin> wasteBins;
    private List<Truck> trucks;
    private List<Route> routes;
    private List<Alert> alerts;

    public WasteManagementSystem() {
        initializeData();
        initializeGUI();
        startSimulation();
    }

    private void initializeData() {
        wasteBins = new ArrayList<>();
        trucks = new ArrayList<>();
        routes = new ArrayList<>();
        alerts = new ArrayList<>();

        // Initialize sample waste bins
        wasteBins.add(new WasteBin("BIN001", "King Fahd Road", 75, "Full"));
        wasteBins.add(new WasteBin("BIN002", "Olaya District", 45, "Medium"));
        wasteBins.add(new WasteBin("BIN003", "Al Malaz", 90, "Full"));
        wasteBins.add(new WasteBin("BIN004", "Diplomatic Quarter", 30, "Low"));
        wasteBins.add(new WasteBin("BIN005", "Al Sahafa", 60, "Medium"));

        // Initialize sample trucks
        trucks.add(new Truck("TRUCK001", "Ahmed Ali", "Active", "Route A"));
        trucks.add(new Truck("TRUCK002", "Mohammed Hassan", "Active", "Route B"));
        trucks.add(new Truck("TRUCK003", "Khalid Omar", "Maintenance", "None"));

        // Initialize sample routes
        routes.add(new Route("ROUTE001", "King Fahd Road → Al Malaz", "High Priority", "In Progress"));
        routes.add(new Route("ROUTE002", "Olaya District → Al Sahafa", "Medium Priority", "Pending"));
    }

    private void initializeGUI() {
        setTitle("Waste Management Optimization System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create menu bar
        createMenuBar();

        // Create main tabbed pane
        tabbedPane = new JTabbedPane();

        // Create tabs
        createDashboardTab();
        createBinMonitoringTab();
        createTruckManagementTab();
        createRouteOptimizationTab();
        createReportsTab();
        createAlertsTab();
        createSettingsTab();

        add(tabbedPane, BorderLayout.CENTER);

        // Create status bar
        createStatusBar();

        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newSessionItem = new JMenuItem("New Session");
        JMenuItem exportReportItem = new JMenuItem("Export Report");
        JMenuItem exitItem = new JMenuItem("Exit");

        newSessionItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "New session started!"));
        exportReportItem.addActionListener(e -> exportReport());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newSessionItem);
        fileMenu.add(exportReportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Tools Menu
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem routeCalcItem = new JMenuItem("Route Calculator");
        JMenuItem sensorDiagItem = new JMenuItem("Sensor Diagnostics");
        JMenuItem backupItem = new JMenuItem("System Backup");

        routeCalcItem.addActionListener(e -> generateOptimalRoute());
        sensorDiagItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "All sensors operating normally."));
        backupItem.addActionListener(e -> backupSystem());

        toolsMenu.add(routeCalcItem);
        toolsMenu.add(sensorDiagItem);
        toolsMenu.add(backupItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem manualItem = new JMenuItem("User Manual");
        JMenuItem aboutItem = new JMenuItem("About");

        manualItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Waste Management System v1.0\nUser Manual: Available online"));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Waste Management Optimization System\nDeveloped for CPIT-250 Project\nVersion 1.0"));

        helpMenu.add(manualItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createDashboardTab() {
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // System Status Panel
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statusPanel.setBorder(new TitledBorder("System Status"));

        statusPanel.add(createStatusCard("Total Bins", "25", Color.BLUE));
        statusPanel.add(createStatusCard("Full Bins", "8", Color.RED));
        statusPanel.add(createStatusCard("Active Trucks", "2", Color.GREEN));
        statusPanel.add(createStatusCard("Pending Routes", "3", Color.ORANGE));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        dashboardPanel.add(statusPanel, gbc);

        // Quick Actions Panel
        JPanel actionsPanel = new JPanel(new FlowLayout());
        actionsPanel.setBorder(new TitledBorder("Quick Actions"));

        JButton generateRouteBtn = new JButton("Generate Optimal Route");
        JButton sendAlertsBtn = new JButton("Send Alerts");
        JButton refreshDataBtn = new JButton("Refresh Data");
        JButton viewReportsBtn = new JButton("View Reports");

        generateRouteBtn.addActionListener(e -> generateOptimalRoute());
        sendAlertsBtn.addActionListener(e -> sendAlerts());
        refreshDataBtn.addActionListener(e -> refreshData());
        viewReportsBtn.addActionListener(e -> tabbedPane.setSelectedIndex(4));

        actionsPanel.add(generateRouteBtn);
        actionsPanel.add(sendAlertsBtn);
        actionsPanel.add(refreshDataBtn);
        actionsPanel.add(viewReportsBtn);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        dashboardPanel.add(actionsPanel, gbc);

        // System Performance Chart Area
        JPanel chartPanel = new JPanel();
        chartPanel.setBorder(new TitledBorder("System Performance"));
        chartPanel.setPreferredSize(new Dimension(400, 200));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.add(new JLabel("Interactive Map Display"));

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        dashboardPanel.add(chartPanel, gbc);

        tabbedPane.addTab("Dashboard", dashboardPanel);
    }

    private JPanel createStatusCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void createBinMonitoringTab() {
        JPanel binPanel = new JPanel(new BorderLayout());

        // Bin table
        String[] binColumns = {"Bin ID", "Location", "Fill Level (%)", "Status", "Last Updated"};
        binTableModel = new DefaultTableModel(binColumns, 0);
        JTable binTable = new JTable(binTableModel);

        // Populate bin table
        updateBinTable();

        JScrollPane binScrollPane = new JScrollPane(binTable);
        binScrollPane.setBorder(new TitledBorder("Waste Bin Status"));

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton refreshBinsBtn = new JButton("Refresh Bins");
        JButton addBinBtn = new JButton("Add New Bin");
        JButton removeBinBtn = new JButton("Remove Bin");
        JButton viewDetailsBtn = new JButton("View Details");

        refreshBinsBtn.addActionListener(e -> updateBinTable());
        addBinBtn.addActionListener(e -> addNewBin());
        removeBinBtn.addActionListener(e -> removeBin(binTable));
        viewDetailsBtn.addActionListener(e -> viewBinDetails(binTable));

        controlPanel.add(refreshBinsBtn);
        controlPanel.add(addBinBtn);
        controlPanel.add(removeBinBtn);
        controlPanel.add(viewDetailsBtn);

        binPanel.add(binScrollPane, BorderLayout.CENTER);
        binPanel.add(controlPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Bin Monitoring", binPanel);
    }

    private void createTruckManagementTab() {
        JPanel truckPanel = new JPanel(new BorderLayout());

        // Truck table
        String[] truckColumns = {"Truck ID", "Driver Name", "Status", "Current Route", "Last Location"};
        truckTableModel = new DefaultTableModel(truckColumns, 0);
        JTable truckTable = new JTable(truckTableModel);

        updateTruckTable();

        JScrollPane truckScrollPane = new JScrollPane(truckTable);
        truckScrollPane.setBorder(new TitledBorder("Truck Fleet Status"));

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton assignRouteBtn = new JButton("Assign Route");
        JButton trackTruckBtn = new JButton("Track Truck");
        JButton maintenanceBtn = new JButton("Schedule Maintenance");
        JButton communicateBtn = new JButton("Send Message");

        assignRouteBtn.addActionListener(e -> assignRoute(truckTable));
        trackTruckBtn.addActionListener(e -> trackTruck(truckTable));
        maintenanceBtn.addActionListener(e -> scheduleMaintenance(truckTable));
        communicateBtn.addActionListener(e -> sendMessage(truckTable));

        controlPanel.add(assignRouteBtn);
        controlPanel.add(trackTruckBtn);
        controlPanel.add(maintenanceBtn);
        controlPanel.add(communicateBtn);

        truckPanel.add(truckScrollPane, BorderLayout.CENTER);
        truckPanel.add(controlPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Truck Management", truckPanel);
    }

    private void createRouteOptimizationTab() {
        JPanel routePanel = new JPanel(new BorderLayout());

        // Route table
        String[] routeColumns = {"Route ID", "Path", "Priority", "Status", "Estimated Time"};
        routeTableModel = new DefaultTableModel(routeColumns, 0);
        JTable routeTable = new JTable(routeTableModel);

        updateRouteTable();

        JScrollPane routeScrollPane = new JScrollPane(routeTable);
        routeScrollPane.setBorder(new TitledBorder("Optimized Routes"));

        // Route optimization panel
        JPanel optimizationPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Optimization Parameters:"));
        JComboBox<String> criteriaCombo = new JComboBox<>(new String[]{"Distance", "Traffic", "Fill Level", "Combined"});
        inputPanel.add(criteriaCombo);

        JButton optimizeBtn = new JButton("Optimize Routes");
        optimizeBtn.addActionListener(e -> optimizeRoutes());
        inputPanel.add(optimizeBtn);

        JPanel mapPanel = new JPanel();
        mapPanel.setBorder(new TitledBorder("Route Map"));
        mapPanel.setBackground(Color.LIGHT_GRAY);
        mapPanel.add(new JLabel("Interactive Map Display"));

        optimizationPanel.add(inputPanel);
        optimizationPanel.add(mapPanel);

        routePanel.add(routeScrollPane, BorderLayout.CENTER);
        routePanel.add(optimizationPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Route Optimization", routePanel);
    }

    private void createReportsTab() {
        JPanel reportPanel = new JPanel(new BorderLayout());

        // Report generation panel
        JPanel generationPanel = new JPanel(new FlowLayout());
        generationPanel.setBorder(new TitledBorder("Generate Reports"));

        JComboBox<String> reportTypeCombo = new JComboBox<>(new String[]{
                "Daily Collection Report", "Weekly Summary", "Monthly Analysis",
                "Performance Report", "Environmental Impact"
        });

        JButton generateReportBtn = new JButton("Generate Report");
        JButton exportReportBtn = new JButton("Export to PDF");
        JButton scheduleReportBtn = new JButton("Schedule Auto-Report");

        generateReportBtn.addActionListener(e -> generateReport(reportTypeCombo));
        exportReportBtn.addActionListener(e -> exportReport());
        scheduleReportBtn.addActionListener(e -> scheduleReport());

        generationPanel.add(new JLabel("Report Type:"));
        generationPanel.add(reportTypeCombo);
        generationPanel.add(generateReportBtn);
        generationPanel.add(exportReportBtn);
        generationPanel.add(scheduleReportBtn);

        // Report display area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setBorder(new TitledBorder("Report Content"));

        reportPanel.add(generationPanel, BorderLayout.NORTH);
        reportPanel.add(reportScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Reports", reportPanel);
    }

    private void createAlertsTab() {
        JPanel alertPanel = new JPanel(new BorderLayout());

        // Alert display area
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane alertScrollPane = new JScrollPane(alertArea);
        alertScrollPane.setBorder(new TitledBorder("System Alerts"));

        // Alert control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton clearAlertsBtn = new JButton("Clear All Alerts");
        JButton testAlertBtn = new JButton("Test Alert System");
        JButton configAlertsBtn = new JButton("Configure Alerts");

        clearAlertsBtn.addActionListener(e -> clearAlerts());
        testAlertBtn.addActionListener(e -> testAlerts());
        configAlertsBtn.addActionListener(e -> configureAlerts());

        controlPanel.add(clearAlertsBtn);
        controlPanel.add(testAlertBtn);
        controlPanel.add(configAlertsBtn);

        alertPanel.add(alertScrollPane, BorderLayout.CENTER);
        alertPanel.add(controlPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Alerts", alertPanel);
    }

    private void createSettingsTab() {
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // System Configuration
        JPanel configPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        configPanel.setBorder(new TitledBorder("System Configuration"));

        configPanel.add(new JLabel("Alert Threshold (%):"));
        configPanel.add(new JSpinner(new SpinnerNumberModel(80, 0, 100, 5)));

        configPanel.add(new JLabel("Update Interval (minutes):"));
        configPanel.add(new JSpinner(new SpinnerNumberModel(5, 1, 60, 1)));

        configPanel.add(new JLabel("Max Route Distance (km):"));
        configPanel.add(new JSpinner(new SpinnerNumberModel(50, 1, 200, 5)));

        configPanel.add(new JLabel("Enable SMS Notifications:"));
        configPanel.add(new JCheckBox());

        configPanel.add(new JLabel("Auto-Generate Routes:"));
        configPanel.add(new JCheckBox("", true));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        settingsPanel.add(configPanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveSettingsBtn = new JButton("Save Settings");
        JButton resetSettingsBtn = new JButton("Reset to Default");
        JButton backupBtn = new JButton("Backup System");
        JButton restoreBtn = new JButton("Restore System");

        saveSettingsBtn.addActionListener(e -> saveSettings());
        resetSettingsBtn.addActionListener(e -> resetSettings());
        backupBtn.addActionListener(e -> backupSystem());
        restoreBtn.addActionListener(e -> restoreSystem());

        buttonPanel.add(saveSettingsBtn);
        buttonPanel.add(resetSettingsBtn);
        buttonPanel.add(backupBtn);
        buttonPanel.add(restoreBtn);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        settingsPanel.add(buttonPanel, gbc);

        tabbedPane.addTab("Settings", settingsPanel);
    }

    private void createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        statusLabel = new JLabel("System Status: Online");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        systemStatusBar = new JProgressBar();
        systemStatusBar.setStringPainted(true);
        systemStatusBar.setString("System Load: 45%");
        systemStatusBar.setValue(45);
        systemStatusBar.setPreferredSize(new Dimension(200, 20));

        JLabel timeLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(systemStatusBar, BorderLayout.CENTER);
        statusPanel.add(timeLabel, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
    }

    private void startSimulation() {
        // Simulate real-time updates using javax.swing.Timer
        javax.swing.Timer timer = new javax.swing.Timer(5000, e -> {
            simulateDataUpdates();
            updateAllTables();
            updateSystemStatus();
        });
        timer.start();

        // Add initial alerts
        addAlert("System Started", "Waste Management System is now online", "INFO");
        addAlert("Full Bin Detected", "Bin BIN001 at King Fahd Road is 90% full", "WARNING");
    }

    private void simulateDataUpdates() {
        Random random = new Random();
        for (WasteBin bin : wasteBins) {
            // Simulate fill level changes
            int change = random.nextInt(10) - 5; // -5 to +5
            bin.fillLevel = Math.max(0, Math.min(100, bin.fillLevel + change));

            if (bin.fillLevel >= 80) {
                bin.status = "Full";
            } else if (bin.fillLevel >= 50) {
                bin.status = "Medium";
            } else {
                bin.status = "Low";
            }
        }
    }

    // Helper methods for updating tables
    private void updateBinTable() {
        binTableModel.setRowCount(0);
        for (WasteBin bin : wasteBins) {
            binTableModel.addRow(new Object[]{
                    bin.id, bin.location, bin.fillLevel + "%", bin.status,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            });
        }
    }

    private void updateTruckTable() {
        truckTableModel.setRowCount(0);
        for (Truck truck : trucks) {
            truckTableModel.addRow(new Object[]{
                    truck.id, truck.driverName, truck.status, truck.currentRoute, "GPS Coordinates"
            });
        }
    }

    private void updateRouteTable() {
        routeTableModel.setRowCount(0);
        for (Route route : routes) {
            routeTableModel.addRow(new Object[]{
                    route.id, route.path, route.priority, route.status, "45 mins"
            });
        }
    }

    private void updateAllTables() {
        updateBinTable();
        updateTruckTable();
        updateRouteTable();
    }

    private void updateSystemStatus() {
        Random random = new Random();
        int load = 30 + random.nextInt(40); // 30-70%
        systemStatusBar.setValue(load);
        systemStatusBar.setString("System Load: " + load + "%");
    }

    // Event handlers for various actions
    private void generateOptimalRoute() {
        String routeId = "ROUTE" + String.format("%03d", routes.size() + 1);
        Route newRoute = new Route(routeId, "Auto-generated optimal path", "High Priority", "Generated");
        routes.add(newRoute);
        updateRouteTable();
        addAlert("Route Generated", "New optimal route " + routeId + " has been generated", "INFO");
        JOptionPane.showMessageDialog(this, "Optimal route generated successfully!");
    }

    private void sendAlerts() {
        for (WasteBin bin : wasteBins) {
            if (bin.fillLevel >= 80) {
                addAlert("Full Bin Alert", "Bin " + bin.id + " at " + bin.location + " needs immediate collection", "WARNING");
            }
        }
        JOptionPane.showMessageDialog(this, "Alerts sent to all relevant personnel!");
    }

    private void refreshData() {
        simulateDataUpdates();
        updateAllTables();
        addAlert("Data Refreshed", "All system data has been updated", "INFO");
        JOptionPane.showMessageDialog(this, "Data refreshed successfully!");
    }

    private void addNewBin() {
        String binId = JOptionPane.showInputDialog(this, "Enter Bin ID:");
        String location = JOptionPane.showInputDialog(this, "Enter Location:");
        if (binId != null && location != null && !binId.trim().isEmpty() && !location.trim().isEmpty()) {
            wasteBins.add(new WasteBin(binId, location, 0, "Empty"));
            updateBinTable();
            addAlert("New Bin Added", "Bin " + binId + " added at " + location, "INFO");
        }
    }

    private void removeBin(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String binId = (String) table.getValueAt(selectedRow, 0);
            wasteBins.removeIf(bin -> bin.id.equals(binId));
            updateBinTable();
            addAlert("Bin Removed", "Bin " + binId + " has been removed from the system", "INFO");
        }
    }

    private void viewBinDetails(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String binId = (String) table.getValueAt(selectedRow, 0);
            WasteBin bin = wasteBins.stream().filter(b -> b.id.equals(binId)).findFirst().orElse(null);
            if (bin != null) {
                String details = String.format(
                        "Bin Details:\n\nID: %s\nLocation: %s\nFill Level: %d%%\nStatus: %s\nSensor Status: Active\nLast Maintenance: 2024-01-15",
                        bin.id, bin.location, bin.fillLevel, bin.status
                );
                JOptionPane.showMessageDialog(this, details, "Bin Details", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void assignRoute(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String truckId = (String) table.getValueAt(selectedRow, 0);
            String[] availableRoutes = routes.stream().map(r -> r.id + " - " + r.path).toArray(String[]::new);
            if (availableRoutes.length > 0) {
                String selectedRoute = (String) JOptionPane.showInputDialog(
                        this, "Select route for " + truckId, "Assign Route",
                        JOptionPane.QUESTION_MESSAGE, null, availableRoutes, availableRoutes[0]
                );
                if (selectedRoute != null) {
                    addAlert("Route Assigned", "Route assigned to " + truckId, "INFO");
                    JOptionPane.showMessageDialog(this, "Route assigned successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No routes available. Please generate routes first.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a truck first.");
        }
    }

    private void trackTruck(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String truckId = (String) table.getValueAt(selectedRow, 0);
            JOptionPane.showMessageDialog(this,
                    "Truck " + truckId + " Tracking:\n\nCurrent Location: 24.7136° N, 46.6753° E\nSpeed: 45 km/h\nETA to next bin: 12 minutes\nFuel Level: 75%",
                    "Truck Tracking", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a truck first.");
        }
    }

    private void scheduleMaintenance(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String truckId = (String) table.getValueAt(selectedRow, 0);
            String date = JOptionPane.showInputDialog(this, "Enter maintenance date (YYYY-MM-DD):");
            if (date != null) {
                addAlert("Maintenance Scheduled", "Maintenance scheduled for " + truckId + " on " + date, "INFO");
                JOptionPane.showMessageDialog(this, "Maintenance scheduled successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a truck first.");
        }
    }

    private void sendMessage(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String truckId = (String) table.getValueAt(selectedRow, 0);
            String message = JOptionPane.showInputDialog(this, "Enter message for " + truckId + ":");
            if (message != null) {
                addAlert("Message Sent", "Message sent to " + truckId, "INFO");
                JOptionPane.showMessageDialog(this, "Message sent successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a truck first.");
        }
    }

    private void optimizeRoutes() {
        addAlert("Route Optimization", "Route optimization process started", "INFO");
        // Simulate optimization process
        javax.swing.Timer optimizationTimer = new javax.swing.Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addAlert("Optimization Complete", "All routes have been optimized for maximum efficiency", "SUCCESS");
                updateRouteTable();
                ((javax.swing.Timer)event.getSource()).stop();
            }
        });
        optimizationTimer.setRepeats(false);
        optimizationTimer.start();

        JOptionPane.showMessageDialog(this, "Route optimization started. Check alerts for updates.");
    }

    private void generateReport(JComboBox<String> reportTypeCombo) {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        StringBuilder report = new StringBuilder();

        report.append("=== ").append(reportType.toUpperCase()).append(" ===\n");
        report.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");

        report.append("SUMMARY:\n");
        report.append("Total Bins Monitored: ").append(wasteBins.size()).append("\n");
        report.append("Full Bins: ").append(wasteBins.stream().mapToInt(b -> b.status.equals("Full") ? 1 : 0).sum()).append("\n");
        report.append("Active Trucks: ").append(trucks.stream().mapToInt(t -> t.status.equals("Active") ? 1 : 0).sum()).append("\n");
        report.append("Routes Generated: ").append(routes.size()).append("\n\n");

        report.append("BIN STATUS DETAILS:\n");
        for (WasteBin bin : wasteBins) {
            report.append(String.format("%-10s %-20s %3d%% %-10s\n",
                    bin.id, bin.location, bin.fillLevel, bin.status));
        }

        report.append("\nTRUCK STATUS DETAILS:\n");
        for (Truck truck : trucks) {
            report.append(String.format("%-10s %-15s %-12s %s\n",
                    truck.id, truck.driverName, truck.status, truck.currentRoute));
        }

        report.append("\n=== END OF REPORT ===");

        reportArea.setText(report.toString());
        addAlert("Report Generated", reportType + " has been generated successfully", "INFO");
    }

    private void exportReport() {
        JOptionPane.showMessageDialog(this, "Report exported to PDF successfully!\nSaved as: waste_management_report.pdf", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        addAlert("Report Exported", "Report has been exported to PDF format", "INFO");
    }

    private void scheduleReport() {
        String[] frequencies = {"Daily", "Weekly", "Monthly"};
        String frequency = (String) JOptionPane.showInputDialog(
                this, "Select report frequency:", "Schedule Auto-Report",
                JOptionPane.QUESTION_MESSAGE, null, frequencies, frequencies[0]
        );
        if (frequency != null) {
            JOptionPane.showMessageDialog(this, "Auto-report scheduled for " + frequency.toLowerCase() + " generation!");
            addAlert("Auto-Report Scheduled", frequency + " reports have been scheduled", "INFO");
        }
    }

    private void clearAlerts() {
        alertArea.setText("");
        alerts.clear();
        addAlert("Alerts Cleared", "All previous alerts have been cleared", "INFO");
    }

    private void testAlerts() {
        addAlert("Test Alert", "This is a test alert to verify the alert system is working", "TEST");
        JOptionPane.showMessageDialog(this, "Test alert sent successfully!");
    }

    private void configureAlerts() {
        JDialog configDialog = new JDialog(this, "Configure Alert Settings", true);
        configDialog.setSize(400, 300);
        configDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Email Notifications:"));
        panel.add(new JCheckBox("", true));

        panel.add(new JLabel("SMS Notifications:"));
        panel.add(new JCheckBox("", false));

        panel.add(new JLabel("Push Notifications:"));
        panel.add(new JCheckBox("", true));

        panel.add(new JLabel("Alert Threshold (%):"));
        panel.add(new JSpinner(new SpinnerNumberModel(80, 0, 100, 5)));

        panel.add(new JLabel("Alert Frequency (minutes):"));
        panel.add(new JSpinner(new SpinnerNumberModel(15, 1, 60, 5)));

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            addAlert("Settings Saved", "Alert configuration has been updated", "INFO");
            configDialog.dispose();
        });
        cancelBtn.addActionListener(e -> configDialog.dispose());

        panel.add(saveBtn);
        panel.add(cancelBtn);

        configDialog.add(panel);
        configDialog.setVisible(true);
    }

    private void saveSettings() {
        JOptionPane.showMessageDialog(this, "Settings saved successfully!");
        addAlert("Settings Saved", "System configuration has been saved", "INFO");
    }

    private void resetSettings() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset all settings to default values?",
                "Reset Settings", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Settings reset to default values!");
            addAlert("Settings Reset", "All settings have been reset to default values", "INFO");
        }
    }

    private void backupSystem() {
        // Simulate backup process
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Backing up system...");

        JDialog progressDialog = new JDialog(this, "System Backup", true);
        progressDialog.add(progressBar);
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(this);

        javax.swing.Timer backupTimer = new javax.swing.Timer(100, new ActionListener() {
            int progress = 0;
            public void actionPerformed(ActionEvent event) {
                progress += 5;
                progressBar.setValue(progress);
                progressBar.setString("Backing up... " + progress + "%");

                if (progress >= 100) {
                    ((javax.swing.Timer)event.getSource()).stop();
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(WasteManagementSystem.this,
                            "System backup completed successfully!\nBackup saved as: backup_" +
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".wmb");
                    addAlert("Backup Complete", "System backup has been created successfully", "INFO");
                }
            }
        });

        progressDialog.setVisible(true);
        backupTimer.start();
    }

    private void restoreSystem() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to restore the system from a backup?\nThis will overwrite current data.",
                "Restore System", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "System restored successfully from backup!");
            addAlert("System Restored", "System has been restored from backup", "INFO");
            // Reinitialize data after restore
            initializeData();
            updateAllTables();
        }
    }

    private void addAlert(String title, String message, String type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String alertText = String.format("[%s] %s: %s - %s\n", timestamp, type, title, message);

        Alert alert = new Alert(title, message, type, timestamp);
        alerts.add(alert);

        alertArea.append(alertText);
        alertArea.setCaretPosition(alertArea.getDocument().getLength());

        // Color code alerts based on type
        if (type.equals("WARNING") || type.equals("ERROR")) {
            // In a real implementation, you might use styled text
            System.out.println("HIGH PRIORITY: " + alertText);
        }
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // If system look and feel fails, use default
            ex.printStackTrace();
        }

        // Create and show the application
        SwingUtilities.invokeLater(() -> {
            new WasteManagementSystem().setVisible(true);
        });
    }
}