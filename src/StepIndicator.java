import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// step progress indicator
public class StepIndicator extends JPanel {

    private static final String[] STEP_NAMES = {
            "Profile", "Define", "Plan", "Collect", "Analyse"
    };

    private int currentStep; // 1-based

    public StepIndicator(int currentStep) {
        this.currentStep = currentStep;
        setBackground(Colors.BG);
        setPreferredSize(new Dimension(0, 68));
    }

    // updates the active step
    public void setCurrentStep(int step) {
        this.currentStep = step;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int total   = STEP_NAMES.length;
        int stepW   = getWidth() / total;
        int circleR = 13;
        int cy      = 26;

        for (int i = 0; i < total; i++) {
            int stepNo = i + 1;
            int cx     = stepW * i + stepW / 2;

            // connector line
            if (i > 0) {
                int prevCx = stepW * (i - 1) + stepW / 2;
                Color lineColor = (stepNo <= currentStep)
                        ? new Color(34, 197, 94)    // green done/active
                        : new Color(70, 70, 100);   // dim future
                g2.setColor(lineColor);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(prevCx + circleR, cy, cx - circleR, cy);
            }


            Color circleFill;
            if (stepNo < currentStep) {
                circleFill = new Color(34, 197, 94);  // done: green
            } else if (stepNo == currentStep) {
                circleFill = Colors.ACCENT;            // active: indigo
            } else {
                circleFill = new Color(60, 60, 90);   // future: dark
            }
            g2.setColor(circleFill);
            g2.fillOval(cx - circleR, cy - circleR, circleR * 2, circleR * 2);

            // icon
            g2.setColor(Color.WHITE);
            if (stepNo < currentStep) {
                // completed step
                String check = "\u2713";
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(check, cx - fm.stringWidth(check) / 2, cy + fm.getAscent() / 2 - 1);
            } else {
                // Current or future
                String num = String.valueOf(stepNo);
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(num, cx - fm.stringWidth(num) / 2, cy + fm.getAscent() / 2 - 1);
            }

            // Step name
            Color nameColor;
            Font  nameFont;
            if (stepNo == currentStep) {
                nameColor = Colors.TEXT;
                nameFont  = new Font("SansSerif", Font.BOLD,  11);
            } else if (stepNo < currentStep) {
                nameColor = new Color(34, 197, 94);
                nameFont  = new Font("SansSerif", Font.PLAIN, 11);
            } else {
                nameColor = new Color(100, 100, 130);
                nameFont  = new Font("SansSerif", Font.PLAIN, 11);
            }
            g2.setColor(nameColor);
            g2.setFont(nameFont);
            FontMetrics fm = g2.getFontMetrics();
            String name = STEP_NAMES[i];
            g2.drawString(name, cx - fm.stringWidth(name) / 2, cy + circleR + 14);
        }

        g2.dispose();
    }
}