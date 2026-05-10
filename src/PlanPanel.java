import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Plan Measurement
 */
public class PlanPanel extends JPanel {

    private AppState state;
    private Runnable onNext;
    private Runnable onBack;

    public PlanPanel(AppState state, Runnable onNext, Runnable onBack) {
        this.state  = state;
        this.onNext = onNext;
        this.onBack = onBack;
        buildUI();
    }

    private void buildUI() {
        setBackground(Colors.BG);
        setLayout(new BorderLayout());

        //title and scenario info
        JPanel header = new JPanel();
        header.setBackground(Colors.BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(16, 40, 8, 40));

        JLabel title = new JLabel("Step 3 - Measurement Plan", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Colors.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(title);

        header.add(Box.createVerticalStrut(4));

        ScenarioData sc = state.getSelectedScenario();
        JLabel info = new JLabel(
                "Scenario: " + sc.getName() + "   |   Mode: " + sc.getMode() +
                        "   |   Type: " + sc.getQualityType(),
                SwingConstants.CENTER);
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(Colors.MUTED);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(info);

        add(header, BorderLayout.NORTH);

        // Scrollable content
        JPanel content = new JPanel();
        content.setBackground(Colors.BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(8, 40, 8, 40));

        for (QualityDimension dim : sc.getDimensions()) {
            content.add(buildDimensionBlock(dim));
            content.add(Box.createVerticalStrut(14));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.getViewport().setBackground(Colors.BG);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Navigation buttons
        JPanel nav = buildNavRow();
        nav.setBorder(BorderFactory.createEmptyBorder(8, 0, 12, 0));
        add(nav, BorderLayout.SOUTH);
    }

    // heading + read-only table
    private JPanel buildDimensionBlock(QualityDimension dim) {
        JPanel block = new JPanel(new BorderLayout(0, 6));
        block.setBackground(Colors.CARD);
        block.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        block.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Dimension heading
        JLabel dimLabel = new JLabel(dim.getName() + "   (Coefficient: " + dim.getCoefficient() + ")");
        dimLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        dimLabel.setForeground(Colors.ACCENT);
        block.add(dimLabel, BorderLayout.NORTH);

        // Table columns
        String[] columns = { "Metric", "Coefficient", "Direction", "Range", "Unit" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; } // read-only
        };

        for (Metric m : dim.getMetrics()) {
            model.addRow(new Object[]{
                    m.getName(),
                    m.getCoefficient(),
                    m.getDirectionLabel(),
                    m.getRangeLabel(),
                    m.getUnit()
            });
        }

        JTable table = new JTable(model);
        styleTable(table);

        // table size
        int rowH = table.getRowHeight();
        table.setPreferredScrollableViewportSize(
                new Dimension(table.getPreferredSize().width, rowH * dim.getMetrics().size() + 2));

        block.add(new JScrollPane(table), BorderLayout.CENTER);
        return block;
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