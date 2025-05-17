package model;

import model.enums.PieceType;

public class Piece {
    public PieceType type;
    public boolean isWhite;
    public int x, y;
    public int turnsProtected = 0;
    public boolean affectedBySoloEscorregadioNextTurn = false;
    public int turnsBlockedByTatico = 0;
    public Point lastPosition = null;

    public Piece(PieceType type, boolean isWhite, int x, int y) {
        this.type = type; this.isWhite = isWhite; this.x = x; this.y = y;
        this.lastPosition = new Point(x, y);
    }

    public void updatePosition(int newX, int newY) {
        this.lastPosition = new Point(this.x, this.y);
        this.x = newX;
        this.y = newY;
    }

    public boolean isProtected() { return turnsProtected > 0; }
    public void setProtected(int turns) { this.turnsProtected = turns; }
    public void decrementProtection() { if (turnsProtected > 0) turnsProtected--; }
    public void decrementBlockTatico() { if (turnsBlockedByTatico > 0) turnsBlockedByTatico--; }

    @Override public String toString() { return type.getSymbol(isWhite); }
}