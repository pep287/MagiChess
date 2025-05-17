package model.enums;

public enum PieceType {
    PEAO("P", "p"), TORRE("T", "t"), CAVALO("C", "c"),
    BISPO("B", "b"), RAINHA("D", "d"), REI("R", "r");

    private final String whiteSymbol, blackSymbol;
    PieceType(String ws, String bs) { whiteSymbol = ws; blackSymbol = bs; }
    public String getSymbol(boolean isWhite) { return isWhite ? whiteSymbol : blackSymbol; }
}