import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Step 1 panel where the user enters profile information.
 */
public class ProfilePanel extends JPanel {

    private AppState  state;
    private Runnable  onNext;

    private JTextField usernameField;
    private JTextField schoolField;
    private JTextField sessionNameField;

    public ProfilePanel(AppState state, Runnable onNext) {
        this.state  = state;
        this.onNext = onNext;
        buildUI();
    }

    private void buildUI() {
        setBackground(Colors.BG);
        setLayout(new GridBagLayout()); // centers the card on the screen

        // centered form card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Colors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        card.setPreferredSize(new Dimension(420, 360));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Title
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        JLabel title = new JLabel("Step 1 - User Profile", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Colors.TEXT);
        card.add(title, g);

        // Subtitle
        g.gridy = 1;
        JLabel sub = new JLabel("Fill in all fields to continue.", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(Colors.MUTED);
        card.add(sub, g);

        // Separator
        g.gridy = 2; g.insets = new Insets(2, 10, 14, 10);
        card.add(new JSeparator(), g);
        g.insets = new Insets(8, 10, 8, 10);

        // Username row
        g.gridwidth = 1; g.weightx = 0;
        g.gridx = 0; g.gridy = 3;
        card.add(makeLabel("Username:"), g);

        g.gridx = 1; g.weightx = 1.0;
        usernameField = makeTextField();
        usernameField.setText(state.getUsername());
        card.add(usernameField, g);

        // School row
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        card.add(makeLabel("School:"), g);

        g.gridx = 1; g.weightx = 1.0;
        schoolField = makeTextField();
        schoolField.setText(state.getSchool());
        card.add(schoolField, g);

        // Session name
        g.gridx = 0; g.gridy = 5; g.weightx = 0;
        card.add(makeLabel("Session Name:"), g);

        g.gridx = 1; g.weightx = 1.0;
        sessionNameField = makeTextField();
        sessionNameField.setText(state.getSessionName());
        card.add(sessionNameField, g);

        // Next button
        g.gridx = 0; g.gridy = 6; g.gridwidth = 2;
        g.fill   = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(24, 10, 8, 10);
        JButton nextBtn = makePrimaryButton("Next  >");
        nextBtn.addActionListener(e -> tryNext());
        card.add(nextBtn, g);

        add(card);
    }
    // checks if all fields are filled before continuing

    private void tryNext() {
        String username = usernameField.getText().trim();
        String school   = schoolField.getText().trim();
        String session  = sessionNameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your username to continue.",
                    "Missing Field", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (school.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your school name to continue.",
                    "Missing Field", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (session.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a session name to continue.",
                    "Missing Field", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // // save entered values and continue
        state.setUsername(username);
        state.setSchool(school);
        state.setSessionName(session);
        onNext.run();
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(Colors.TEXT);
        return l;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField(18);
        tf.setBackground(Colors.INPUT);
        tf.setForeground(Colors.TEXT);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setCaretColor(Colors.ACCENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
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
}