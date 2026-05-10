import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

/**
 * Application entry

 */
public class MeasurementApp {

    // switch between panels
    private static final String CARD_PROFILE = "profile";
    private static final String CARD_DEFINE  = "define";
    private static final String CARD_PLAN    = "plan";
    private static final String CARD_COLLECT = "collect";
    private static final String CARD_ANALYSE = "analyse";

    private JFrame        frame;
    private CardLayout    cardLayout;
    private JPanel        cardPanel;
    private StepIndicator stepIndicator;

    private AppState     appState;     // shared data passed between all steps
    private ScenarioRepository scenarioData;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MeasurementApp().start();
            }
        });
    }

    private void start() {
        appState     = new AppState();
        scenarioData = new ScenarioRepository();

        frame = new JFrame("ISO 15939 Measurement Process Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(780, 560));
        frame.setSize(880, 660);
        frame.setLocationRelativeTo(null); // center on screen
        frame.getContentPane().setBackground(Colors.BG);
        frame.setLayout(new BorderLayout());

        // Step indicator
        stepIndicator = new StepIndicator(1);
        stepIndicator.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.BORDER));
        frame.add(stepIndicator, BorderLayout.NORTH);

        // CardLayout
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(Colors.BG);
        frame.add(cardPanel, BorderLayout.CENTER);

        showProfile(); // Step 1

        frame.setVisible(true);
    }


    // Navigation methods

    private void showProfile() {
        stepIndicator.setCurrentStep(1);
        ProfilePanel panel = new ProfilePanel(appState, new Runnable() {
            public void run() { showDefine(); }
        });
        cardPanel.add(panel, CARD_PROFILE);
        cardLayout.show(cardPanel, CARD_PROFILE);
    }

    private void showDefine() {
        stepIndicator.setCurrentStep(2);
        DefinePanel panel = new DefinePanel(appState, scenarioData,
                new Runnable() { public void run() { showPlan(); } },
                new Runnable() { public void run() { showProfile(); } }
        );
        cardPanel.add(panel, CARD_DEFINE);
        cardLayout.show(cardPanel, CARD_DEFINE);
    }

    private void showPlan() {
        // Resolve Scenario object
        ScenarioData sc = scenarioData.getScenario(
                appState.getQualityType(),
                appState.getMode(),
                appState.getScenarioName()
        );

        if (sc == null) {
            JOptionPane.showMessageDialog(frame,
                    "Could not find the selected scenario. Please go back and try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        appState.setSelectedScenario(sc);

        stepIndicator.setCurrentStep(3);
        PlanPanel panel = new PlanPanel(appState,
                new Runnable() { public void run() { showCollect(); } },
                new Runnable() { public void run() { showDefine(); } }
        );
        cardPanel.add(panel, CARD_PLAN);
        cardLayout.show(cardPanel, CARD_PLAN);
    }

    private void showCollect() {
        stepIndicator.setCurrentStep(4);
        CollectPanel panel = new CollectPanel(appState,
                new Runnable() { public void run() { showAnalyse(); } },
                new Runnable() { public void run() { showPlan(); } }
        );
        cardPanel.add(panel, CARD_COLLECT);
        cardLayout.show(cardPanel, CARD_COLLECT);
    }

    private void showAnalyse() {
        stepIndicator.setCurrentStep(5);
        AnalysePanel panel = new AnalysePanel(appState,
                new Runnable() { public void run() { showCollect(); } },
                new Runnable() { public void run() { restart(); } }
        );
        cardPanel.add(panel, CARD_ANALYSE);
        cardLayout.show(cardPanel, CARD_ANALYSE);
    }

    /** Resets everything to Step 1*/
    private void restart() {
        appState = new AppState();
        cardPanel.removeAll();
        showProfile();
        cardPanel.revalidate();
        cardPanel.repaint();
    }
}