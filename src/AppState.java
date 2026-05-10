public class AppState {

    // Profile
    private String username    = "";
    private String school      = "";
    private String sessionName = "";

    // Define
    private String qualityType  = "Product"; // default selection
    private String mode         = "Health";  // default selection
    private String scenarioName = "";

    //after Step 2 is confirmed
    private ScenarioData selectedScenario = null;

    // Getters and setters for Step 1
    public String getUsername()              { return username; }
    public void   setUsername(String v)      { this.username = v; }

    public String getSchool()                { return school; }
    public void   setSchool(String v)        { this.school = v; }

    public String getSessionName()           { return sessionName; }
    public void   setSessionName(String v)   { this.sessionName = v; }

    // Getters and setters for Step 2
    public String getQualityType()           { return qualityType; }
    public void   setQualityType(String v)   { this.qualityType = v; }

    public String getMode()                  { return mode; }
    public void   setMode(String v)          { this.mode = v; }

    public String getScenarioName()          { return scenarioName; }
    public void   setScenarioName(String v)  { this.scenarioName = v; }

    // The Scenario object used from Step 3
    public ScenarioData getSelectedScenario()          { return selectedScenario; }
    public void     setSelectedScenario(ScenarioData s){ this.selectedScenario = s; }
}