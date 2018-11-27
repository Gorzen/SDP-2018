package ch.epfl.sweng.studyup.specialQuest;

public enum SpecialQuestType {

    THREE_QUESTIONS (
        "Three Questions",
        "Answer three questions for a surprise item!",
        "Trois Questions",
        "Répondez à trois questions pour un objet surprise !",
        3
    );

    private String englishTitle;
    private String englishDesc;
    private String frenchTitle;
    private String frenchDesc;
    private int goal;

    SpecialQuestType(String englishTitle, String englishDesc, String frenchTitle, String frenchDesc, int goal) {
        this.englishTitle = englishTitle;
        this.englishDesc = englishDesc;
        this.frenchTitle = frenchTitle;
        this.frenchDesc = frenchDesc;
        this.goal = goal;
    }

    public String getEnglishTitle() { return this.englishTitle; }
    public String getEnglishDesc() { return this.englishDesc; }
    public String getFrenchTitle() { return this.frenchTitle; }
    public String getFrenchDesc() { return this.frenchDesc; }
    public int getGoal() { return this.goal; }
}
