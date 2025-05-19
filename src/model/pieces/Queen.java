package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class Queen extends Piece {

    public Queen(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.initialPosX;
        int dy = newPosY - this.initialPosY;
        
        if ((dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy)) && caminhoLivre(initialPosX, initialPosY, newPosX, newPosY, grid)) 
            return true;
        
        return false;
    }

    private boolean caminhoLivre(int initialPosX, int initialPosY, int newPosX, int newPosY, Piece[][] grid) {
        int dx = Integer.compare(newPosX, initialPosX);
        int dy = Integer.compare(newPosY, initialPosY);
        int x = initialPosX + dx, y = initialPosY + dy;
        while (x != newPosX || y != newPosY) {
            if (grid[x][y] != null) return false;
            x += dx;
            y += dy;
        }
        return true;
    }
}
