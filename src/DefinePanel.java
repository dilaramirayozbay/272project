import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;

/**
Define Quality Scope
 */
public class DefinePanel extends JPanel {

    private AppState     state;
    private ScenarioRepository scenarioData;
    private Runnable     onNext;
    private Runnable     onBack;

    //quality type
    private JRadioButton rbProduct;
    private JRadioButton rbProcess;

    //mode
    private JRadioButton rbHealth;
    private JRadioButton rbEducation;

    //scenario
    private ButtonGroup  scenarioGroup;
    private JPanel       scenarioHolder;

    public DefinePanel(AppState state, ScenarioRepository scenarioData,
                       Runnable onNext, Runnable onBack) {
        this.state        = state;
        this.scenarioData = scenarioData;
        this.onNext       = onNext;
        this.onBack       = onBack;
        buildUI();
    }

    private void buildUI() {
        setBackground(Colors.BG);
        setLayout(new BorderLayout());

        //column of cards
        JPanel content = new JPanel();
        content.setBackground(Colors.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 16, 50));

        // Title
        JLabel title = new JLabel("Step 2 - Define Quality Scope", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Colors.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(16));

        // quality type card
        content.add(buildQualityTypeCard());
        content.add(Box.createVerticalStrut(12));

        // mode card
        content.add(buildModeCard());
        content.add(Box.createVerticalStrut(12));

        //Scenario card
        JPanel scenarioCard = makeCard("Scenario  (select one)");
        scenarioHolder = new JPanel();
        scenarioHolder.setBackground(Colors.CARD);
        scenarioHolder.setLayout(new BoxLayout(scenarioHolder, BoxLayout.Y_AXIS));
        scenarioCard.add(scenarioHolder);
        content.add(scenarioCard);
        content.add(Box.createVerticalStrut(20));

        // Navigation buttons
        content.add(buildNavRow());

        JScrollPane scroll = new JScrollPane(content);
        scroll.getViewport().setBackground(Colors.BG);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        refreshScenarios();
    }

    // Builds quality Type card
    private JPanel buildQualityTypeCard() {
        JPanel card = makeCard("Quality Type  (select one)");

        ButtonGroup group = new ButtonGroup();
        rbProduct = makeRadio("Product Quality  -  software product characteristics (performance, security, usability, reliability)");
        rbProcess = makeRadio("Process Quality  -  development process characteristics (sprint efficiency, code quality, collaboration)");
        group.add(rbProduct);
        group.add(rbProcess);

        // Restore
        if ("Process".equals(state.getQualityType())) rbProcess.setSelected(true);
        else                                           rbProduct.setSelected(true);

        // refresh
        rbProduct.addActionListener(e -> refreshScenarios());
        rbProcess.addActionListener(e -> refreshScenarios());

        card.add(rbProduct);
        card.add(Box.createVerticalStrut(6));
        card.add(rbProcess);
        return card;
    }

    // Builds mode card
    private JPanel buildModeCard() {
        JPanel card = makeCard("Mode  (select one)");

        ButtonGroup group = new ButtonGroup();
        rbHealth    = makeRadio("Health      -  health management system scenarios");
        rbEducation = makeRadio("Education   -  education LMS system scenarios");
        group.add(rbHealth);
        group.add(rbEducation);

        // Restore
        if ("Education".equals(state.getMode())) rbEducation.setSelected(true);
        else                                     rbHealth.setSelected(true);

        // Refresh
        rbHealth.addActionListener(e    -> refreshScenarios());
        rbEducation.addActionListener(e -> refreshScenarios());

        card.add(rbHealth);
        card.add(Box.createVerticalStrut(6));
        card.add(rbEducation);
        return card;
    }

    /**
     * Rebuilds scenario radio buttons
     */
    private void refreshScenarios() {
        scenarioHolder.removeAll();
        scenarioGroup = new ButtonGroup();

        String qt   = rbProcess.isSelected()   ? "Process"   : "Product";
        String mode = rbEducation.isSelected()  ? "Education" : "Health";

        ArrayList<String> names = scenarioData.getNames(qt, mode);

        if (names.isEmpty()) {
            JLabel none = new JLabel("No scenarios available for this combination.");
            none.setForeground(Colors.MUTED);
            none.setFont(new Font("SansSerif", Font.ITALIC, 12));
            scenarioHolder.add(none);
        } else {
            for (String name : names) {
                JRadioButton rb = makeRadio(name);
                scenarioGroup.add(rb);
                scenarioHolder.add(rb);
                scenarioHolder.add(Box.createVerticalStrut(6));
                // Re-select
                if (name.equals(state.getScenarioName())) rb.setSelected(true);
            }
            // if not restored select 1st option default
            if (scenarioGroup.getSelection() == null) {
                Enumeration<AbstractButton> elems = scenarioGroup.getElements();
                if (elems.hasMoreElements()) elems.nextElement().setSelected(true);
            }
        }

        scenarioHolder.revalidate();
        scenarioHolder.repaint();
    }

    // Saves all to Step 3
    private void tryNext() {
        String qt   = rbProcess.isSelected()   ? "Process"   : "Product";
        String mode = rbEducation.isSelected()  ? "Education" : "Health";

        // finds selected scenario button
        String scenarioName = null;
        Enumeration<AbstractButton> elems = scenarioGroup.getElements();
        while (elems.hasMoreElements()) {
            AbstractButton btn = elems.nextElement();
            if (btn.isSelected()) {
                scenarioName = btn.getText();
                break;
            }
        }

        if (scenarioName == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a scenario to continue.",
                    "Missing Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        state.setQualityType(qt);
        state.setMode(mode);
        state.setScenarioName(scenarioName);
        onNext.run();
    }

    private JPanel buildNavRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        row.setBackground(Colors.BG);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = makeSecondaryButton("< Back");
        backBtn.addActionListener(e -> onBack.run());

        JButton nextBtn = makePrimaryButton("Next  >");
        nextBtn.addActionListener(e -> tryNext());

        row.add(backBtn);
        row.add(nextBtn);
        return row;
    }

    /**
     Creates card panel
     */
    private JPanel makeCard(String heading) {
        JPanel card = new JPanel();
        card.setBackground(Colors.CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel h = new JLabel(heading);
        h.setFont(new Font("SansSerif", Font.BOLD, 13));
        h.setForeground(Colors.ACCENT);
        card.add(h);
        card.add(Box.createVerticalStrut(8));
        return card;
    }

    private JRadioButton makeRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBackground(Colors.CARD);
        rb.setForeground(Colors.TEXT);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rb.setFocusPainted(false);
        return rb;
    }

    private JButton makePrimaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(Colors.ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(9, 28, 9, 28));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeSecondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(Colors.INPUT);
        b.setForeground(Colors.TEXT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 22, 8, 22)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}