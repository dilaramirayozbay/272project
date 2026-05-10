import java.util.ArrayList;

/**
 * Stores predefined scenarios used in the application.
 */
public class ScenarioRepository {

    private ArrayList<ScenarioData> scenarios;

    public ScenarioRepository() {
        scenarios = new ArrayList<>();

        loadData();
    }

    private void loadData() {

        // Scenario A - Health Product

        ScenarioData s1 =
                new ScenarioData(
                        "Scenario A - Hospital System",
                        "Health",
                        "Product"
                );

        QualityDimension reliability =
                new QualityDimension("Reliability", 20);

        reliability.addMetric(
                new Metric(
                        "Uptime",
                        50,
                        true,
                        95,
                        100,
                        "%",
                        99
                )
        );

        reliability.addMetric(
                new Metric(
                        "MTTR",
                        50,
                        false,
                        0,
                        120,
                        "min",
                        15
                )
        );

        s1.addDimension(reliability);

        // second dimension for scenario A
        QualityDimension security =
                new QualityDimension("Security", 15);

        security.addMetric(
                new Metric(
                        "Attack Detection",
                        50,
                        true,
                        0,
                        100,
                        "%",
                        91
                )
        );

        security.addMetric(
                new Metric(
                        "Response Time",
                        50,
                        false,
                        0,
                        30,
                        "sec",
                        6
                )
        );

        s1.addDimension(security);

        scenarios.add(s1);

        // Scenario B - Health Process

        ScenarioData s2 =
                new ScenarioData(
                        "Scenario B - Scrum Team",
                        "Health",
                        "Process"
                );

        QualityDimension efficiency =
                new QualityDimension("Sprint Efficiency", 25);

        efficiency.addMetric(
                new Metric(
                        "Task Completion",
                        50,
                        true,
                        0,
                        100,
                        "%",
                        82
                )
        );

        efficiency.addMetric(
                new Metric(
                        "Bug Resolution Time",
                        50,
                        false,
                        0,
                        72,
                        "hours",
                        18
                )
        );

        s2.addDimension(efficiency);

        // second dimension for scenario B
        QualityDimension communication =
                new QualityDimension("Communication", 15);

        communication.addMetric(
                new Metric(
                        "Daily Meeting Attendance",
                        50,
                        true,
                        0,
                        100,
                        "%",
                        88
                )
        );

        communication.addMetric(
                new Metric(
                        "Feedback Delay",
                        50,
                        false,
                        0,
                        48,
                        "hours",
                        10
                )
        );

        s2.addDimension(communication);

        scenarios.add(s2);

        // Scenario C - Education Product

        ScenarioData s3 =
                new ScenarioData(
                        "Scenario C - Team Alpha",
                        "Education",
                        "Product"
                );

        QualityDimension usability =
                new QualityDimension("Usability", 25);

        usability.addMetric(
                new Metric(
                        "SUS Score",
                        50,
                        true,
                        0,
                        100,
                        "points",
                        89
                )
        );

        usability.addMetric(
                new Metric(
                        "Onboarding Time",
                        50,
                        false,
                        0,
                        60,
                        "min",
                        5
                )
        );

        s3.addDimension(usability);

        // second dimension for scenario C
        QualityDimension maintainability =
                new QualityDimension("Maintainability", 20);

        maintainability.addMetric(
                new Metric(
                        "Code Duplication",
                        50,
                        false,
                        0,
                        50,
                        "%",
                        12
                )
        );

        maintainability.addMetric(
                new Metric(
                        "Documentation Coverage",
                        50,
                        true,
                        0,
                        100,
                        "%",
                        85
                )
        );

        s3.addDimension(maintainability);

        scenarios.add(s3);

        // Scenario D - Education Process

        ScenarioData s4 =
                new ScenarioData(
                        "Scenario D - Team Beta",
                        "Education",
                        "Process"
                );

        QualityDimension collaboration =
                new QualityDimension("Team Collaboration", 20);

        collaboration.addMetric(
                new Metric(
                        "Code Review Rate",
                        50,
                        true,
                        0,
                        100,
                        "%",
                        90
                )
        );

        collaboration.addMetric(
                new Metric(
                        "Meeting Delay",
                        50,
                        false,
                        0,
                        30,
                        "min",
                        8
                )
        );

        s4.addDimension(collaboration);

        // second dimension for scenario D
        QualityDimension productivity =
                new QualityDimension("Productivity", 20);

        productivity.addMetric(
                new Metric(
                        "Completed Tasks",
                        50,
                        true,
                        0,
                        100,
                        "%",
                        84
                )
        );

        productivity.addMetric(
                new Metric(
                        "Late Submission",
                        50,
                        false,
                        0,
                        20,
                        "%",
                        4
                )
        );

        s4.addDimension(productivity);

        scenarios.add(s4);
    }

    public ArrayList<String> getNames(String qualityType, String mode) {

        ArrayList<String> result = new ArrayList<>();

        for (ScenarioData s : scenarios) {

            if (s.getQualityType().equals(qualityType)
                    && s.getMode().equals(mode)) {

                result.add(s.getName());
            }
        }

        return result;
    }

    public ScenarioData getScenario(String qualityType,
                                    String mode,
                                    String name) {

        for (ScenarioData s : scenarios) {

            if (s.getQualityType().equals(qualityType)
                    && s.getMode().equals(mode)
                    && s.getName().equals(name)) {

                return s;
            }
        }

        return null;
    }
}