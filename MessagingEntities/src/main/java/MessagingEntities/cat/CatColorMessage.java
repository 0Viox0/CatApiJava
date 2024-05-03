package MessagingEntities.cat;

public enum CatColorMessage {
    ORANGE,
    BLACK,
    WHITE,
    BROWN,
    PURPLE;

    public static CatColorMessage fromString(String color)
            throws IllegalArgumentException {
        if (color == null) {
            return null;
        } else {
            return CatColorMessage.valueOf(color.toUpperCase());
        }
    }
}
