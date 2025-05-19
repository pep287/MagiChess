package model;

import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.pieces.Bisp;
import model.pieces.King;
import model.pieces.Knight;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.Queen;
import model.pieces.Tower;
import model.enums.PieceColor;
import model.enums.PieceType;

public class Player {
    private boolean reflexoRealAtivo = false;
    public boolean isReflexoRealAtivo() { return reflexoRealAtivo; }
    public void setReflexoRealAtivo(boolean ativo) { reflexoRealAtivo = ativo; }
    private int commonCategoryCooldownTurnsLeft = 0;
    // Remove uma carta da mão
    public void removeCard(Card card) {
        hand.remove(card);
    }
    private PieceColor color;
    private List<Card> hand = new ArrayList<>();
    private List<Piece> pieces = new ArrayList<>();
    private int turnCount = 0;
    private boolean hasPlayedCardThisTurn = false;
    private int rareCategoryCooldownTurnsLeft = 0;
    private int legendaryCategoryCooldownTurnsLeft = 0;
    public int turnosSilencioRealRestantes = 0;
    public void setTurnosSilencioRealRestantes(int v) { this.turnosSilencioRealRestantes = v; }
    public void decrementarSilencioReal() { if (turnosSilencioRealRestantes > 0) turnosSilencioRealRestantes--; }

    public Player(PieceColor color) {
        this.color = color;
        initializePieces();
    }

    private void initializePieces() {
        int pawnRow = color == PieceColor.WHITE ? 6 : 1;
        int backRow = color == PieceColor.BLACK  ? 7 : 0;
        for (int i = 0; i < 8; i++) pieces.add(new Pawn(PieceType.PEAO, color, i, pawnRow));

        pieces.add(new Tower(PieceType.TORRE, color, 0, backRow));
        pieces.add(new Tower(PieceType.TORRE, color, 7, backRow));
        pieces.add(new Knight(PieceType.CAVALO, color, 1, backRow));
        pieces.add(new Knight(PieceType.CAVALO, color, 6, backRow));
        pieces.add(new Bisp(PieceType.BISPO, color, 2, backRow));
        pieces.add(new Bisp(PieceType.BISPO, color, 5, backRow));
        pieces.add(new Queen(PieceType.RAINHA, color, 3, backRow));
        pieces.add(new King(PieceType.REI, color, 4, backRow));
    }

    public void startTurn() {
        turnCount++;
        if (hand.size() < 5) {
            Card novaCarta = Card.sorteiaCartaAleatoria();
            hand.add(novaCarta);
        }
        hasPlayedCardThisTurn = false;
        // Reduz cooldowns globais
        if (commonCategoryCooldownTurnsLeft > 0) commonCategoryCooldownTurnsLeft--;
        if (rareCategoryCooldownTurnsLeft > 0) rareCategoryCooldownTurnsLeft--;
        if (legendaryCategoryCooldownTurnsLeft > 0) legendaryCategoryCooldownTurnsLeft--;
    }

    public List<Piece> getPieces() { return pieces; }
    // ... (restante dos getters e métodos)
    public PieceColor color() { return color; }
    public int getTurnCount() { return turnCount; }
    public boolean hasPlayedCardThisTurn() { return hasPlayedCardThisTurn; }
    public void setHasPlayedCardThisTurn(boolean v) { hasPlayedCardThisTurn = v; }
    public int getRareCategoryCooldownTurnsLeft() { return rareCategoryCooldownTurnsLeft; }
    public int getCommonCategoryCooldownTurnsLeft() { return commonCategoryCooldownTurnsLeft; }
    public void setCommonCategoryCooldownTurnsLeft(int v) { commonCategoryCooldownTurnsLeft = v; }
    public void setRareCategoryCooldownTurnsLeft(int v) { rareCategoryCooldownTurnsLeft = v; }
    public int getLegendaryCategoryCooldownTurnsLeft() { return legendaryCategoryCooldownTurnsLeft; }
    public void setLegendaryCategoryCooldownTurnsLeft(int v) { legendaryCategoryCooldownTurnsLeft = v; }
    public boolean isSilenced() { return turnosSilencioRealRestantes > 0; }
    public boolean isValidCardIndex(int idx) { return idx >= 0 && idx < hand.size(); }
    public Card getCard(int idx) { return hand.get(idx); }
    public void printHand() {
        if (hand.isEmpty()) System.out.println("Nenhuma carta.");
        else for (int i = 0; i < hand.size(); i++)
            System.out.println("Carta " + (i + 1) + ": " + hand.get(i));
    }
}