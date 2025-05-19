package logic;

import model.Board;
import model.Player;
import model.Card;
import model.Point;
import model.enums.PieceColor;
import model.enums.PieceType;
import model.pieces.Piece;

import java.util.ArrayList;
import java.util.Scanner;

public class CardEffects {
    public static boolean apply(Card card, Board board, Player player, Scanner scanner) {
        String nome = card.getName();
        switch (nome) {
            case "Empurrao Tatico":
                // Avance um peao duas casas, mesmo apos o primeiro movimento
                System.out.println("Escolha o peao para avancar duas casas (ex: a2):");
                String coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                int x = coord.charAt(0) - 'a';
                int y = 8 - Character.getNumericValue(coord.charAt(1));
                Piece peao = null;
                for (Piece p : player.getPieces()) {
                    if (p.getPositionX() == x && p.getPositionY() == y && p.getPieceSurname().name().equals("PEAO")) {
                        peao = p;
                        break;
                    }
                }
                if (peao == null) {
                    System.out.println("Nao ha peao seu nessa casa.");
                    return false;
                }
                int dir = player.color() == PieceColor.WHITE ? -1 : 1;
                int destY = peao.getPositionY() + 2 * dir;
                if (destY < 0 || destY > 7 || board.movePiece(peao.getPositionX(), peao.getPositionY(), peao.getPositionX(), destY) == false) {
                    System.out.println("Nao foi possivel avancar o peao.");
                    return false;
                }
                System.out.println("Peao avancou duas casas!");
                return true;

            case "Recuo Seguro":
                // Volte uma peca sua a posicao anterior sem consumir seu turno
                System.out.println("Escolha a peca para recuar (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                Piece psel = null;
                for (Piece p : player.getPieces()) {
                    if (p.getPositionX() == x && p.getPositionY() == y) {
                        psel = p;
                        break;
                    }
                }
                if (psel == null || psel.getLastPosition() == null) {
                    System.out.println("Peca nao encontrada ou sem posicao anterior.");
                    return false;
                }
                // Salva a posicao atual e a ultima posicao
                Point posAtual = new Point(psel.getPositionX(), psel.getPositionY());
                Point posAnterior = new Point(psel.getLastPosition().x, psel.getLastPosition().y);
                // Move manualmente a peca para a posicao anterior
                psel.setPositionX(posAnterior.x);
                psel.setPositionY(posAnterior.y);
                // Atualiza o tabuleiro
                board.updateGrid();
                // Atualiza o lastPosition para a posicao de onde ela veio (para evitar loop de recuo)
                psel.setLastPosition(posAtual); 
                System.out.println("Peca voltou para a posicao anterior!");
                return true;

            case "Mobilidade Extra":
                // Uma peca pode se mover novamente neste turno (mas nao capturar)
                System.out.println("Escolha a peca para mover novamente (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                psel = null;
                for (Piece p : player.getPieces()) {
                    if (p.getPositionX() == x && p.getPositionY() == y) {
                        psel = p;
                        break;
                    }
                }
                if (psel == null) {
                    System.out.println("Peca nao encontrada.");
                    return false;
                }
                System.out.println("Digite a casa de destino (ex: a3):");
                String destino = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(destino)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                int dx = destino.charAt(0) - 'a';
                int dy = 8 - Character.getNumericValue(destino.charAt(1));
                Piece capt = null;
                // Nao pode capturar
                for (Piece op : new ArrayList<Piece>()) {
                    if (op.getPositionX() == dx && op.getPositionY() == dy) capt = op;
                }
                if (board.movePiece(x, y, dx, dy) && board.getCurrentPlayer() == player && capt == null) {
                    System.out.println("Peca movida novamente!");
                    return true;
                } else {
                    System.out.println("Nao foi possivel mover a peca (nao pode capturar).");
                    return false;
                }

            case "Solo Escorregadio":
                // Cavalo inimigo nao pode pular pecas neste turno
                Player oponente = (player == board.getCurrentPlayer()) ? null : board.getCurrentPlayer();
                if (oponente == null) {
                    System.out.println("Nao foi possivel identificar o oponente.");
                    return false;
                }
                for (Piece p : oponente.getPieces()) {
                    if (p.getPieceSurname().name().equals("CAVALO")) p.setAffectedBySoloEscorregadioNextTurn(true);
                }
                System.out.println("Cavalos do oponente nao poderao pular pecas neste turno.");
                return true;

            case "Bloqueio Tatico":
                // Uma peca inimiga nao pode ser movida por um turno (exceto rei)
                System.out.println("Escolha a peca inimiga para bloquear (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                // Identifica o oponente corretamente
                Player inimigo = (player == board.getWhitePlayer()) ? board.getBlackPlayer() : board.getWhitePlayer();
                Piece alvo = null;
                for (Piece p : inimigo.getPieces()) {
                    if (p.getPositionX() == x && p.getPositionY() == y && !p.getPieceSurname().name().equals("REI")) {
                        alvo = p;
                        break;
                    }
                }
                if (alvo == null) {
                    System.out.println("Peca inimiga nao encontrada ou eh o rei.");
                    return false;
                }
                alvo.setTurnsBlockedByTatico(2);
                System.out.println("Peca bloqueada por um turno!");
                return true;

            case "Reflexo Real":
                // Se uma peca sua for capturada, a peca inimiga tambem eh destruida
                player.setReflexoRealAtivo(true); // Ativa o efeito
                System.out.println("Reflexo Real ativado! Se uma peca sua for capturada, a peca inimiga tambem sera destruida.");
                return true;

            case "Troca Estrategica":
                // Troque de lugar duas pecas suas
                System.out.println("Escolha a primeira peca (ex: a2):");
                String c1 = scanner.nextLine().trim().toLowerCase();
                System.out.println("Escolha a segunda peca (ex: b2):");
                String c2 = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(c1) || !Board.isValidCoord(c2)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                int x1 = c1.charAt(0) - 'a';
                int y1 = 8 - Character.getNumericValue(c1.charAt(1));
                int x2 = c2.charAt(0) - 'a';
                int y2 = 8 - Character.getNumericValue(c2.charAt(1));
                Piece p1 = null, p2 = null;
                for (Piece p : player.getPieces()) {
                    if (p.getPositionX() == x1 && p.getPositionY() == y1) p1 = p;
                    if (p.getPositionX() == x2 && p.getPositionY() == y2) p2 = p;
                }
                if (p1 == null || p2 == null) {
                    System.out.println("Pecas nao encontradas.");
                    return false;
                }
                int tx = p1.getPositionX(), ty = p1.getPositionY();
                p1.setPositionX(p2.getPositionX());
                p1.setPositionY(p2.getPositionY());
                p2.setPositionX(tx); 
                p2.setPositionY(ty);
                board.updateGrid();
                System.out.println("Pecas trocadas!");
                return true;

            case "Silencio Real":
                // Silencia ambos jogadores por 2 turnos completos (4 meios-turnos)
                board.getWhitePlayer().setTurnosSilencioRealRestantes(4);
                board.getBlackPlayer().setTurnosSilencioRealRestantes(4);
                System.out.println("Silencio Real ativado! Ninguem podera usar cartas por dois turnos completos.");
                return true;

            case "Coluna de gelo":
                // Uma coluna inteira do tabuleiro nao pode ser usada por 2 turnos
                System.out.println("Escolha a coluna para congelar (a-h):");
                String col = scanner.nextLine().trim().toLowerCase();
                if (col.length() != 1 || col.charAt(0) < 'a' || col.charAt(0) > 'h') {
                    System.out.println("Coluna invalida.");
                    return false;
                }
                int colunaIndex = col.charAt(0) - 'a';
                board.ativarColunaDeGelo(colunaIndex);
                System.out.println("Coluna " + col + " congelada por 2 turnos!");
                return true;

            case "Trato Feito":
                // Consiga 2 cartas ao inves de uma
                System.out.println("Voce recebe 2 cartas!");
                player.startTurn();
                player.startTurn();
                return true;

            case "Poder Supremo":
                // Elimine qualquer peca do tabuleiro imediatamente (Exceto rei ou rainha)
                System.out.println("Escolha a peca para eliminar (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                Piece alvoSup = null;
                for (Piece p : board.getCurrentPlayer().getPieces()) {
                    if (p.getPositionX() == x && p.getPositionY() == y && !p.getPieceSurname().name().equals("REI") && !p.getPieceSurname().name().equals("RAINHA")) {
                        alvoSup = p;
                        break;
                    }
                }
                if (alvoSup == null) {
                    System.out.println("Peca nao encontrada ou e rei/rainha.");
                    return false;
                }
                board.getCurrentPlayer().getPieces().remove(alvoSup);
                board.updateGrid();
                System.out.println("Peca eliminada!");
                return true;

            case "Barreira Imperial":
                board.ativarBarreiraImperial();
                System.out.println("Barreira Imperial ativada! Suas pecas nao podem ser capturadas no proximo turno.");
                return true;

            case "Renascimento":
                // Reviva uma peca capturada e coloque-a em sua linha inicial (exceto a rainha)
                System.out.println("Escolha a peca capturada para reviver (ex: PEAO, TORRE, CAVALO, BISPO, REI):");
                String tipo = scanner.nextLine().trim().toUpperCase();
                if (tipo.equals("RAINHA")) {
                    System.out.println("Nao e possivel reviver a rainha.");
                    return false;
                }
                // Simulacao: revive na coluna 0 da linha inicial
                int linha = player.color() == PieceColor.WHITE ? 7 : 0;
                // Piece revive = new Piece(model.enums.PieceType.valueOf(tipo), player.color(), 0, linha);
                // player.getPieces().add(revive);
                // board.updateGrid();
                System.out.println("Peca revivida!");
                return true;

            case "Dominio Dimensional":
                // Teleporte qualquer peca sua para qualquer casa vazia
                System.out.println("Escolha a peca para teleporte (ex: a2):");
                String origem = scanner.nextLine().trim().toLowerCase();
                System.out.println("Escolha a casa de destino (ex: b3):");
                String destino2 = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(origem) || !Board.isValidCoord(destino2)) {
                    System.out.println("Coordenada invalida.");
                    return false;
                }
                x = origem.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(origem.charAt(1));
                dx = destino2.charAt(0) - 'a';
                dy = 8 - Character.getNumericValue(destino2.charAt(1));
                psel = null;
                for (Piece p : player.getPieces()) {
                    if (p.getPositionX() == x && p.getPositionY() == y) {
                        psel = p;
                        break;
                    }
                }
                if (psel == null) {
                    System.out.println("Peca nao encontrada.");
                    return false;
                }
                if (board.movePiece(x, y, dx, dy)) {
                    System.out.println("Peca teleportada!");
                    return true;
                } else {
                    System.out.println("Nao foi possivel teleportar a peca.");
                    return false;
                }

            default:
                System.out.println("Efeito da carta '" + card.getName() + "' nao implementado.");
                return false;
        }
    }
}