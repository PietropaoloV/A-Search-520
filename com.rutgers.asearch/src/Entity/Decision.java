package Entity;

public enum Decision {
    Right    (0), // calls constructor with value 0
    Left    (1), // calls constructor with value 1
    Up    (2), // calls constructor with value 2
    Down    (3), // calls constructor with value 3
    NOOP    (5); // calls constructor with value 5

    private final int choice;

    Decision(int choice) {
        this.choice = choice;
    }

    public int getChoice() {
        return this.choice;
    }

    public Decision inverse(){
        switch (this.choice){
            case 1: return Left;
            case 3: return Down;
            case 4: return Up;
            case 2: return Right;
            default: return NOOP;
        }
    }

    @Override
    public String toString() {
        return "Decision{" +
                "choice=" + choice +
                '}';
    }
}
