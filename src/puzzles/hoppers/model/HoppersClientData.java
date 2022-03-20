package puzzles.hoppers.model;

import java.util.Objects;

public class HoppersClientData {
    private String message = "";

    public HoppersClientData(String message) {
        this.message = Objects.toString(message, "");
    }

    /**
     * @return status message
     */
    public String getMessage() {
        return this.message;
    }
}
