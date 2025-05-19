package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class Knight extends Piece{

    public Knight(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.initialPosX;
        int dy = newPosY - this.initialPosY;
        
        if ((Math.abs(dx) == 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2)) 
            return true;
            
        return false;
    }

    public PieceColor getPieceColor() {
        return this.pieceColor;
    }
    
}
