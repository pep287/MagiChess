package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class Pawn extends Piece{
    public Pawn(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.initialPosX;
        int dy = newPosY - this.initialPosY;

        int dir = this.pieceColor == PieceColor.WHITE ? -1 : 1;

        // Movimento simples para frente
        if (dx == 0 && dy == dir && destinyPlace == null) { 
            return true;
        }

        // Primeiro movimento pode andar 2 casas
        if (dx == 0 
            && dy == 2*dir
            && destinyPlace == null
            && ((this.pieceColor == PieceColor.WHITE && this.initialPosX == 6)
            || (this.pieceColor == PieceColor.BLACK && this.initialPosY == 1))
            && grid[initialPosX][initialPosY+dir] == null
        ) { 
            return true;
        }

        // Captura diagonal
        if (Math.abs(dx) == 1 && dy == dir && destinyPlace != null &&  destinyPlace.getPieceColor() != this.pieceColor) return true;
        
        return false;
    }
}
