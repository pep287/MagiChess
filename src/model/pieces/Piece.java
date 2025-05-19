package model.pieces;

import model.Point;
import model.enums.PieceColor;
import model.enums.PieceType;

public abstract class Piece {
    public PieceType pieceSurname;
    public int initialPosX, initialPosY;
    public PieceColor pieceColor;
    public int turnsProtected = 0;
    public boolean affectedBySoloEscorregadioNextTurn = false;
    public int turnsBlockedByTatico = 0;
    public Point lastPosition = null;

    public Piece(PieceType pieceSurname, PieceColor pieceColor, int x, int y) {
        this.pieceSurname = pieceSurname; 
        this.pieceColor = pieceColor;
        this.initialPosX = x;
        this.initialPosY = y;
        this.lastPosition = new Point(x, y);
    }

    public void updatePosition(int newX, int newY) {
        this.lastPosition = new Point(this.initialPosX, this.initialPosY);
        this.initialPosX = newX;
        this.initialPosY = newY;
    }

    public abstract boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid);

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

    public int getInitialPosX() {
        return initialPosX;
    }

    public void setInitialPosX(int initialPosX) {
        this.initialPosX = initialPosX;
    }

    public int getInitialPosY() {
        return initialPosY;
    }

    public void setInitialPosY(int initialPosY) {
        this.initialPosY = initialPosY;
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