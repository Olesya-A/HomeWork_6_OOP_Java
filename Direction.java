public enum Direction {

    TOP, RIGHT, BOTTOM, LEFT;

    public static Direction ofString(String str) {
        Direction[] values = values();
        for (Direction value : values) {
            if (str.equals(value.name())) {
                return value;
            }
        }

        // Осторожно! Может быть null
        return null;
    }

}
