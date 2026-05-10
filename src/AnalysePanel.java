import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Step 5 Analyse Results
 */
public class AnalysePanel extends JPanel {

    private AppState state;
    private Runnable onBack;
    private Runnable onRestart;

    public AnalysePanel(AppState state, Runnable onBack, Runnable onRestart) {
        this.state     = state;
        this.onBack    = onBack;
        this.onRestart = onRestart;
        buildUI();
    }

    private void buildUI() {
        setBackground(Colors.BG);
        setLayout(new BorderLayout());

        // Title header
        JPanel header = new JPanel();
        header.setBackground(Colors.BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 40, 8, 40));

        JLabel title = new JLabel("Step 5 - Analysis Results", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Colors.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Scrollable content area
        JPanel content = new JPanel();
        content.setBackground(Colors.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(8, 40, 8, 40));

        content.add(buildScoreBarsSection());   // 5a
        content.add(Box.createVerticalStrut(16));
        content.add(buildGapAnalysisSection()); // 5c
        content.add(Box.createVerticalStrut(20));
        content.add(buildNavRow());

        JScrollPane scroll = new JScrollPane(content);
        scroll.getViewport().setBackground(Colors.BG);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    // Dimension Scores

    private JPanel buildScoreBarsSection() {
        JPanel card = makeCard("Dimension Scores");

        for (QualityDimension dim : state.getSelectedScenario().getDimensions()) {
            double score = dim.getDimensionScore();

            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(Colors.CARD);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

            JLabel nameLabel = new JLabel(dim.getName());
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            nameLabel.setForeground(Colors.TEXT);
            nameLabel.setPreferredSize(new Dimension(190, 24));
            row.add(nameLabel, BorderLayout.WEST);

            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue((int) (score / 5.0 * 100)); // convert 0-5 score to 0-100%
            bar.setStringPainted(false);
            bar.setBackground(Colors.INPUT);
            bar.setForeground(scoreColor(score));
            bar.setBorder(BorderFactory.createEmptyBorder());
            row.add(bar, BorderLayout.CENTER);

            JLabel scoreLabel = new JLabel(String.format("  %.2f / 5.00", score));
            scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            scoreLabel.setForeground(scoreColor(score));
            scoreLabel.setPreferredSize(new Dimension(90, 24));
            row.add(scoreLabel, BorderLayout.EAST);

            card.add(row);
            card.add(Box.createVerticalStrut(8));
        }

        return card;
    }


    //  Gap Analysis


    private JPanel buildGapAnalysisSection() {
        JPanel card = makeCard("Gap Analysis");

        ScenarioData         sc      = state.getSelectedScenario();
        QualityDimension weakest = sc.getWeakestDimension();

        if (weakest == null) {
            card.add(new JLabel("No data available."));
            return card;
        }

        double score = weakest.getDimensionScore();
        double gap   = 5.0 - score;
        String level = qualityLevel(score);

        JPanel box = new JPanel(new GridBagLayout());
        box.setBackground(new Color(30, 30, 50));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(scoreColor(score), 2),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(4, 6, 4, 6);
        g.anchor  = GridBagConstraints.WEST;
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        JLabel dimTitle = new JLabel("Weakest Dimension:  " + weakest.getName());
        dimTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        dimTitle.setForeground(scoreColor(score));
        box.add(dimTitle, g);

        // Score row
        g.gridy = 1; g.gridwidth = 1;
        box.add(infoRow("Score:", String.format("%.2f / 5.00", score)), g);

        // Gap row
        g.gridy = 2;
        box.add(infoRow("Gap to perfect (5.0):", String.format("%.2f", gap)), g);

        // Quality level row
        g.gridy = 3;
        box.add(infoRow("Quality Level:", level), g);

        // Improvement note
        g.gridy = 4; g.gridwidth = 2;
        JLabel note = new JLabel("This dimension has the lowest score and requires the most improvement.");
        note.setFont(new Font("SansSerif", Font.ITALIC, 12));
        note.setForeground(Colors.MUTED);
        box.add(note, g);

        card.add(box);
        return card;
    }

    // Creates label row
    private JPanel infoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setBackground(new Color(30, 30, 50));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(Colors.MUTED);
        lbl.setPreferredSize(new Dimension(190, 20));

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 12));
        val.setForeground(Colors.TEXT);

        row.add(lbl);
        row.add(val);
        return row;
    }

    // Navigation

    private JPanel buildNavRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        row.setBackground(Colors.BG);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = makeSecondaryButton("< Back");
        backBtn.addActionListener(e -> onBack.run());

        JButton restartBtn = makePrimaryButton("Restart");
        restartBtn.addActionListener(e -> onRestart.run());

        row.add(backBtn);
        row.add(restartBtn);
        return row;
    }

    // Returns quality label
    private String qualityLevel(double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 3.5) return "Good";
        if (score >= 2.5) return "Needs Improvement";
        return "Poor";
    }

    // Returns a color due to score
    private Color scoreColor(double score) {
        if (score >= 4.5) return new Color(34,  197, 94);  // green
        if (score >= 3.5) return new Color(132, 204, 22);  // lime
        if (score >= 2.5) return new Color(234, 179,  8);  // yellow
        if (score >= 1.5) return new Color(249, 115, 22);  // orange
        return                    new Color(239,  68, 68);  // red
    }

    /**
     * Creates a card panel
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
        card.add(Box.createVerticalStrut(10));
        return card;
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