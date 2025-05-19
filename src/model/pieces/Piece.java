package model.pieces;

import model.Point;
import model.enums.PieceColor;
import model.enums.PieceType;

public abstract class Piece {
    private PieceType pieceSurname;
    private int positionX, positionY;
    private PieceColor pieceColor;
    private int turnsProtected = 0;
    private boolean affectedBySoloEscorregadioNextTurn = false;
    private int turnsBlockedByTatico = 0;
    private Point lastPosition = null;

    public Piece(PieceType pieceSurname, PieceColor pieceColor, int x, int y) {
        this.pieceSurname = pieceSurname; 
        this.pieceColor = pieceColor;
        this.positionX = x;
        this.positionY = y;
        this.lastPosition = new Point(x, y);
    }

    public void updatePosition(int newX, int newY) {
        this.lastPosition = new Point(this.positionX, this.positionY);
        this.positionX = newX;
        this.positionY = newY;
    }

    public abstract boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid);

    public boolean freePath(int positionX, int positionY, int newPosX, int newPosY, Piece[][] grid) {
        int dx = Integer.compare(newPosX, positionX);
        int dy = Integer.compare(newPosY, positionY);
        int x = positionX + dx, y = positionY + dy;
        while (x != newPosX || y != newPosY) {
            if (grid[x][y] != null) return false;
            x += dx;
            y += dy;
        }
        return true;
    }

    public boolean isProtected() { return turnsProtected > 0; }
    public void setProtected(int turns) { this.turnsProtected = turns; }
    public void decrementProtection() { if (turnsProtected > 0) turnsProtected--; }
    public void decrementBlockTatico() { if (turnsBlockedByTatico > 0) turnsBlockedByTatico--; }

    @Override public String toString() { return pieceSurname.getSymbol(this.pieceColor); }

    public PieceType getPieceSurname() {
        return pieceSurname;
    }

    public void setPieceSurname(PieceType pieceSurname) {
        this.pieceSurname = pieceSurname;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public int getTurnsProtected() {
        return turnsProtected;
    }

    public void setTurnsProtected(int turnsProtected) {
        this.turnsProtected = turnsProtected;
    }

    public boolean isAffectedBySoloEscorregadioNextTurn() {
        return affectedBySoloEscorregadioNextTurn;
    }

    public void setAffectedBySoloEscorregadioNextTurn(boolean affectedBySoloEscorregadioNextTurn) {
        this.affectedBySoloEscorregadioNextTurn = affectedBySoloEscorregadioNextTurn;
    }

    public int getTurnsBlockedByTatico() {
        return turnsBlockedByTatico;
    }

    public void setTurnsBlockedByTatico(int turnsBlockedByTatico) {
        this.turnsBlockedByTatico = turnsBlockedByTatico;
    }

    public Point getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Point lastPosition) {
        this.lastPosition = lastPosition;
    }
}