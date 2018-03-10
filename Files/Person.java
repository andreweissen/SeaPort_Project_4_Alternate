/**
 * Person.java - Class for <code>Person</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project4;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;

/**
 * A subclass of <code>Thing</code>, the <code>Person</code> class represents dock workers located
 * at specific <code>SeaPort</code>s. Each worker has a specific profession, notated in the class
 * itself as <code>skill</code>, and is placed in a <code>SeaPort</code>'s <code>persons</code>
 * <code>ArrayList</code>. Class contains setter/getter and the overridden <code>toString()</code>
 * method as per the rubric.
 * <br />
 * <br />
 * For the Project 3 design, the author was initially wary of adding any fields related to the
 * various <code>Job</code> threads to the <code>Person</code> class. However, after the taxing
 * method of removing and readding <code>SeaPort.persons</code> entries for each thread was removed,
 * the author simply added a single <code>isWorking</code> <code>boolean</code> flag to denote the
 * worker's employment status.
 * <br />
 * <br />
 * For the Project 4 design, the author elected to pursue a design ethos similar in style to that
 * previously employed by the <code>Job</code> class in Project 3. As such, this class contains a
 * number of GUI-related methods that enable the main GUI class, <code>SeaPortProgram</code>, to
 * assemble a "row" related to this worker and add it to a <code>JTable</code>-esque pseudo-table in
 * the GUI.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project4.Thing
 * @author Andrew Eissen
 */
final class Person extends Thing {

    // Rubric-required field
    private String skill;

    // Additional fields
    private volatile boolean isWorking; // Should be volatile?
    private SeaPort parentPort;

    // GUI fields
    private JPanel rowPanel;
    private JLabel nameLabel, skillLabel, portLabel, statusLabel;

    /**
     * Parameterized constructor
     * @param scannerContents Contents of the <code>.txt</code> file
     * @param portsMap <code>HashMap</code>
     */
    protected Person(Scanner scannerContents, HashMap<Integer, SeaPort> portsMap) {
        super(scannerContents);

        if (scannerContents.hasNext()) {
            this.setSkill(scannerContents.next());
        } else {
            this.setSkill("Error");
        }

        this.setIsWorking(false);
        this.setParentPort(portsMap.get(this.getParent()));
    }

    // Setters

    /**
     * Setter for <code>skill</code>
     * @param skill <code>String</code>
     * @return void
     */
    private void setSkill(String skill) {
        this.skill = skill;
    }

    /**
     * Setter for <code>isWorking</code>
     * @param isWorking <code>boolean</code>
     * @return void
     */
    protected void setIsWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }

    /**
     * Setter for <code>parentPort</code>
     * @param parentPort <code>SeaPort</code>
     * @return void
     */
    private void setParentPort(SeaPort parentPort) {
        this.parentPort = parentPort;
    }

    // Getters

    /**
     * Getter for <code>skill</code>
     * @return <code>this.skill</code>
     */
    protected String getSkill() {
        return this.skill;
    }

    /**
     * Getter for <code>isWorking</code>
     * @return <code>this.isWorking</code>
     */
    protected boolean getIsWorking() {
        return this.isWorking;
    }

    /**
     * Getter for <code>parentPort</code>
     * @return <code>this.parentPort</code>
     */
    protected SeaPort getParentPort() {
        return this.parentPort;
    }

    // Utility methods

    /**
     * This method was built to emulate <code>Job.getJobAsPanel</code> in terms of creating a
     * <code>JTable</code>-like row for each individual <code>Person</code>. The author attempted a
     * <code>JTable</code> + <code>AbstractTableModel</code>-driven approach in the early stages of
     * Project 4, as per the future improvements section of the Project 3 documentation, but was
     * ultimately unsatisfied with the overly difficult implementation process. A simple
     * <code>JPanel</code>-driven approach similar to the <code>Job</code> rows seemed a more
     * reasonable alternative.
     *
     * @return rowPanel <code>JPanel</code> row object
     */
    protected JPanel getPersonAsPanel() {

        // Capitalize skill name
        String job = this.getSkill().substring(0, 1).toUpperCase() + this.getSkill().substring(1);

        // Definitions
        this.rowPanel = new JPanel(new GridLayout(1, 4));
        this.nameLabel = new JLabel(this.getName(), JLabel.CENTER);
        this.skillLabel = new JLabel(job, JLabel.CENTER);
        this.portLabel = new JLabel(this.getParentPort().getName(), JLabel.CENTER);
        this.statusLabel = new JLabel("Available", JLabel.CENTER);

        // Allow for colorization of JLabel backgrounds
        this.nameLabel.setOpaque(true);
        this.skillLabel.setOpaque(true);
        this.portLabel.setOpaque(true);
        this.statusLabel.setOpaque(true);

        // Add JTable-esque borders
        this.nameLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.skillLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.portLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.statusLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Add initial colorization
        this.nameLabel.setBackground(Color.WHITE);
        this.skillLabel.setBackground(Color.WHITE);
        this.portLabel.setBackground(Color.WHITE);
        this.statusLabel.setBackground(Color.GREEN);

        // Add elements to the pseudo-row
        this.rowPanel.add(this.nameLabel);
        this.rowPanel.add(this.skillLabel);
        this.rowPanel.add(this.portLabel);
        this.rowPanel.add(this.statusLabel);

        return this.rowPanel;
    }

    /**
     * Like the method above it, this utility method was established to exist in line with the ethos
     * of the <code>Job</code> "pseudo-row" approach. This method is much akin to
     * <code>Job.showStatus</code> in terms of its essential purpose to colorize the background of a
     * <code>JComponent</code> based upon a certain condition. This method will display different
     * text and colors depending upon whether the dock worker in question is available for use by
     * moored ships.
     *
     * @return void
     */
    protected void updateWorkAvailability() {
        if (this.getIsWorking()) {
            this.statusLabel.setBackground(Color.RED);
            this.statusLabel.setText("Unavailable");
        } else {
            this.statusLabel.setBackground(Color.GREEN);
            this.statusLabel.setText("Available");
        }
    }

    // Overridden method

    /**
     * @inheritdoc
     * @return <code>String</code>
     */
    @Override
    public String toString() {
        return "Person: " + super.toString() + " " + this.getSkill();
    }
}