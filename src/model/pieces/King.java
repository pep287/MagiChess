package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class King extends Piece{

    public King(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.initialPosX;
        int dy = newPosY - this.initialPosY;
        
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) 
            return true;
        
        return false;
    }
}
