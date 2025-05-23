package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class Queen extends Piece {

    public Queen(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.getPositionX();
        int dy = newPosY - this.getPositionY();
        
        if ((dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy)) && freePath(this.getPositionX(), this.getPositionY(), newPosX, newPosY, grid)) 
            return true;
        
        return false;
    }
}
