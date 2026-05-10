import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.Dimension;

/**
 Collect Data
 */
public class CollectPanel extends JPanel {

    private AppState state;
    private Runnable onNext;
    private Runnable onBack;

    public CollectPanel(AppState state, Runnable onNext, Runnable onBack) {
        this.state  = state;
        this.onNext = onNext;
        this.onBack = onBack;
        buildUI();
    }

    private void buildUI() {
        setBackground(Colors.BG);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(Colors.BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 40, 8, 40));

        JLabel title = new JLabel("Step 4 - Collect Data", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Colors.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);

        header.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Raw values are shown below. Scores are calculated automatically.",
                SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(Colors.MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(sub);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setBackground(Colors.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(8, 40, 8, 40));

        ScenarioData sc = state.getSelectedScenario();
        for (QualityDimension dim : sc.getDimensions()) {
            content.add(buildDimensionBlock(dim));
            content.add(Box.createVerticalStrut(14));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.getViewport().setBackground(Colors.BG);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Navigation
        JPanel nav = buildNavRow();
        nav.setBorder(BorderFactory.createEmptyBorder(8, 0, 12, 0));
        add(nav, BorderLayout.SOUTH);
    }

    private JPanel buildDimensionBlock(QualityDimension dim) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setBackground(Colors.CARD);
        block.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        block.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel dimLabel = new JLabel(dim.getName() + "  (Coefficient: " + dim.getCoefficient() + ")");
        dimLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        dimLabel.setForeground(Colors.ACCENT);
        block.add(dimLabel, BorderLayout.NORTH);

        String[] cols = { "Metric", "Direction", "Range", "Value", "Score (1-5)", "Coeff / Unit" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Metric m : dim.getMetrics()) {
            String scoreStr = String.format("%.1f", m.getScore());
            model.addRow(new Object[]{
                    m.getName(),
                    m.getDirectionLabel(),
                    m.getRangeLabel(),
                    m.getRawValue(),
                    scoreStr,
                    m.getCoefficient() + " / " + m.getUnit()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        // Color based on val
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(Colors.CARD);
                setForeground(Colors.TEXT);
                if (!sel && val != null) {
                    try {
                        double score = Double.parseDouble(val.toString());
                        setForeground(scoreColor(score));
                        setFont(new Font("SansSerif", Font.BOLD, 12));
                    } catch (NumberFormatException ignored) {}
                }
                return this;
            }
        });

        table.setPreferredScrollableViewportSize(
                new Dimension(table.getPreferredSize().width,
                        table.getRowHeight() * dim.getMetrics().size() + 2));

        block.add(new JScrollPane(table), BorderLayout.CENTER);
        return block;
    }

    // Returns color for score val
    private Color scoreColor(double score) {
        if (score >= 4.5) return new Color(34, 197, 94);   // green
        if (score >= 3.5) return new Color(132, 204, 22);  // lime
        if (score >= 2.5) return new Color(234, 179, 8);   // yellow
        if (score >= 1.5) return new Color(249, 115, 22);  // orange
        return new Color(239, 68, 68);                     // red
    }

    private JPanel buildNavRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        row.setBackground(Colors.BG);

        JButton backBtn = makeSecondaryButton("< Back");
        backBtn.addActionListener(e -> onBack.run());

        JButton nextBtn = makePrimaryButton("Next  >");
        nextBtn.addActionListener(e -> onNext.run());

        row.add(backBtn);
        row.add(nextBtn);
        return row;
    }

    private void styleTable(JTable t) {
        t.setBackground(Colors.CARD);
        t.setForeground(Colors.TEXT);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setRowHeight(26);
        t.setGridColor(Colors.BORDER);
        t.setShowGrid(true);
        t.setIntercellSpacing(new Dimension(1, 1));
        t.getTableHeader().setBackground(Colors.INPUT);
        t.getTableHeader().setForeground(Colors.ACCENT);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        t.setSelectionBackground(Colors.ACCENT);
        t.setSelectionForeground(Color.WHITE);
    }

    private JButton makePrimaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(Colors.ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
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
                BorderFactory.createEmptyBorder(7, 20, 7, 20)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}