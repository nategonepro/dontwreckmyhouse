package learn.dontwreckmyhouse.ui;

public enum MainMenuOption {
    EXIT(0, "Exit"),
    VIEW_RESERVATION(1,"View Reservations by Host"),
    MAKE_RESERVATION(2, "Make a Reservation"),
    UPDATE_RESERVATION(3, "Edit a Reservation"),
    REMOVE_RESERVATION(4, "Cancel a Reservation");

    private int value;
    private String message;

    MainMenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public static MainMenuOption fromValue(int value){
        for(MainMenuOption option : MainMenuOption.values()){
            if(option.getValue() == value){
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
