package model.pieces;

import model.enums.PieceColor;
import model.enums.PieceType;

public class Bisp extends Piece{

    public Bisp(PieceType pieceSurname, PieceColor pieceColor, int initialPosX, int initialPosY) {
        super(pieceSurname, pieceColor, initialPosX, initialPosY);
    }

    @Override
    public boolean validMoviment(int newPosX, int newPosY, Piece destinyPlace, Piece[][] grid) {
        int dx = newPosX - this.initialPosX;
        int dy = newPosY - this.initialPosY;
        
        if (Math.abs(dx) != Math.abs(dy)) 
            return false;
        
        if (!caminhoLivre(this.initialPosX, this.initialPosY, newPosX, newPosY, grid)) 
            return false;
        
        return true;
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
