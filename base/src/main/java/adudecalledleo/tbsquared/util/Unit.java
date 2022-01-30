package adudecalledleo.tbsquared.util;

public final class Unit {
    public static final Unit INSTANCE = new Unit();

    private Unit() { }

    @Override
    public String toString() {
        return "Unit";
    }
}
