package Api.models.cat;

public enum CatColor {
    ORANGE,
    BLACK,
    WHITE,
    BROWN,
    PURPLE;

    public static CatColor fromString(String color)
    throws IllegalArgumentException {
        if (color == null) {
            return null;
        } else {
            return CatColor.valueOf(color.toUpperCase());
        }
    }
}
