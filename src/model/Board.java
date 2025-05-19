package model;

import model.enums.PieceColor;
import model.pieces.Piece;

public class Board {
    public static final String TITLE_ART =
            "##   ##    ##      ####     ####     ####    ##  ##   ######    ####     ####\n" +
            " ### ###   ####    ##  ##     ##     ##  ##   ##  ##   ##       ##  ##   ##  ##\n" +
            " #######  ##  ##   ##         ##     ##       ##  ##   ##       ##       ##\n" +
            " ## # ##  ######   ## ###     ##     ##       ######   ####      ####     ####\n" +
            " ##   ##  ##  ##   ##  ##     ##     ##       ##  ##   ##           ##       ##\n" +
            " ##   ##  ##  ##   ##  ##     ##     ##  ##   ##  ##   ##       ##  ##   ##  ##\n" +
            " ##   ##  ##  ##    ####     ####     ####    ##  ##   ######    ####     ####\n";
    private Piece[][] grid = new Piece[8][8];
    // Efeitos persistentes de cartas
    public int colunaDeGeloIndex = -1;
    public int turnosColunaDeGeloRestantes = 0;
    public boolean barreiraImperialAtiva = false;
    public int turnosBarreiraImperialRestantes = 0;
    public static boolean isValidCoord(String s) {
        return s.length() == 2 && s.charAt(0) >= 'a' && s.charAt(0) <= 'h' && s.charAt(1) >= '1' && s.charAt(1) <= '8';
    }
    private Player whitePlayer = new Player(PieceColor.WHITE);
    private Player blackPlayer = new Player(PieceColor.BLACK);
    private Player currentPlayer = whitePlayer;
    private boolean isGameOver = false;

    public Board() {
        updateGrid();
        currentPlayer.startTurn();
    }

    public void updateGrid() {
        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                grid[x][y] = null;
        for (Piece p : whitePlayer.getPieces()) grid[p.getPositionX()][p.getPositionY()] = p;
        for (Piece p : blackPlayer.getPieces()) grid[p.getPositionX()][p.getPositionY()] = p;
    }

    public boolean movePiece(int fromX, int fromY, int toX, int toY) {
        if (fromX < 0 || fromX >= 8 || fromY < 0 || fromY >= 8 || toX < 0 || toX >= 8 || toY < 0 || toY >= 8)
            return false;

        Piece piece = grid[fromX][fromY];
        if (piece == null) 
            return false;

        Piece destino = grid[toX][toY];
        if (destino != null && destino.getPieceColor() == piece.getPieceColor()) 
            return false;
        
            // Bloqueio por Coluna de Gelo
        if (colunaDeGeloIndex != -1 && turnosColunaDeGeloRestantes > 0 && (fromX == colunaDeGeloIndex || toX == colunaDeGeloIndex)) {
            System.out.println("A coluna " + (char)('a'+colunaDeGeloIndex) + " esta congelada!");
            return false;
        }
        
        // Bloqueio por Barreira Imperial (impede capturas)
        if (barreiraImperialAtiva && turnosBarreiraImperialRestantes > 0 && destino != null && destino.getPieceColor() != piece.getPieceColor()) {
            System.out.println("Barreira Imperial ativa! Nao e possivel capturar pecas neste turno.");
            return false;
        }
        
        // Validacao de movimento conforme tipo da peca
        if (!piece.validMoviment(toX, toY, destino, grid)) {
            System.out.println("Movimento invalido para a peca " + piece.getPieceSurname() + ".");
            return false;
        }

        // Reflexo Real: se destino for capturado e o jogador dono de destino tiver Reflexo Real ativo
        if (destino != null && destino.getPieceColor() != piece.getPieceColor()) {
            Player defensor = destino.getPieceColor() == PieceColor.WHITE ? whitePlayer : blackPlayer;
            Player atacante = piece.getPieceColor() == PieceColor.WHITE  ? whitePlayer : blackPlayer;
            if (defensor.isReflexoRealAtivo()) {
                // Remove a peca atacante tambem
                System.out.println("Reflexo Real: a peca capturadora tambem foi destruida!");
                atacante.getPieces().remove(piece);
                defensor.setReflexoRealAtivo(false); // Efeito eh de uso unico
                // Remove a peca capturada normalmente abaixo
            }
            defensor.getPieces().remove(destino);
        }

        grid[fromX][fromY] = null;
        piece.setPositionX(toX);
        piece.setPositionY(toY);
        grid[toX][toY] = piece;
        updateGrid();
        return true;
    }

    public void printBoard() {
        System.out.println("\n    a  b  c  d  e  f  g  h");
        System.out.println("   -------------------------");
        for (int y = 0; y < 8; y++) {
            System.out.print((8 - y) + " |");
            for (int x = 0; x < 8; x++) {
                Piece p = grid[x][y];
                System.out.print(p == null ? " . " : " " + p + " ");
            }
            System.out.println("| " + (8 - y));
        }
        System.out.println("   -------------------------");
        System.out.println("    a  b  c  d  e  f  g  h");
    }

    public void switchPlayer() {
        // Atualiza efeitos persistentes
        // Coluna de Gelo
        if (colunaDeGeloIndex != -1 && turnosColunaDeGeloRestantes > 0) {
            turnosColunaDeGeloRestantes--;
            if (turnosColunaDeGeloRestantes == 0) colunaDeGeloIndex = -1;
        }
        // Barreira Imperial
        if (barreiraImperialAtiva && turnosBarreiraImperialRestantes > 0) {
            turnosBarreiraImperialRestantes--;
            if (turnosBarreiraImperialRestantes == 0) barreiraImperialAtiva = false;
        }
        // Silêncio Real (em ambos jogadores)
        whitePlayer.decrementarSilencioReal();
        blackPlayer.decrementarSilencioReal();

        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        currentPlayer.startTurn();
    }
    // Métodos auxiliares para efeitos
    public boolean isColunaDeGeloAtiva(int coluna) {
        return colunaDeGeloIndex == coluna && turnosColunaDeGeloRestantes > 0;
    }
    public boolean isBarreiraImperialAtiva() {
        return barreiraImperialAtiva && turnosBarreiraImperialRestantes > 0;
    }
    public void ativarBarreiraImperial() {
        barreiraImperialAtiva = true;
        turnosBarreiraImperialRestantes = 2;
    }
    public void ativarColunaDeGelo(int coluna) {
        colunaDeGeloIndex = coluna;
        turnosColunaDeGeloRestantes = 2;
    }

    public boolean isGameOver() { return isGameOver; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getWhitePlayer() { return whitePlayer; }
    public Player getBlackPlayer() { return blackPlayer; }
}