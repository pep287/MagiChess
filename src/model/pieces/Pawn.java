package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class Pawn extends Piece{
    public Pawn(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.getPositionX(); // coluna
        int dy = newPosY - this.getPositionY(); // linha

        int dir = this.getPieceColor() == PieceColor.WHITE ? -1 : 1;

        // Movimento simples para frente
        if (dx == 0 && dy == dir && destinyPlace == null) {
            return true;
        }

        // Primeiro movimento pode andar 2 casas
        if (dx == 0
            && dy == 2 * dir
            && destinyPlace == null
            && ((this.getPieceColor() == PieceColor.WHITE && this.getPositionY() == 6)
                || (this.getPieceColor() == PieceColor.BLACK && this.getPositionY() == 1))
            && grid[this.getPositionX()][this.getPositionY() + dir] == null
        ) {
            return true;
        }

        // Captura diagonal
        if (Math.abs(dx) == 1 && dy == dir && destinyPlace != null
            && destinyPlace.getPieceColor() != this.getPieceColor()) {
            return true;
        }

        return false;
    }
}
