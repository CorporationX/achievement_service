package faang.school.achievement.model;

public enum AchievementCode {
    COLLECTOR("COLLECTOR"),
    MR_PRODUCTIVITY("MR PRODUCTIVITY"),
    EXPERT("EXPERT"),
    SENSEI("SENSEI"),
    MANAGER("MANAGER"),
    CELEBRITY("CELEBRITY"),
    WRITER("WRITER"),
    HANDSOME("HANDSOME");

    private final String name;

    public String getName() {
        return name;
    }

    AchievementCode(String name) {
        this.name = name;
    }
}
