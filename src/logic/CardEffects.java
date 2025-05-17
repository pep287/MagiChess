package logic;

import model.Board;
import model.Player;
import model.Piece;
import model.Card;
import model.Point;
import model.enums.PieceType;

import java.util.ArrayList;
import java.util.Scanner;

public class CardEffects {
    public static boolean apply(Card card, Board board, Player player, Scanner scanner) {
        String nome = card.getName();
        switch (nome) {
            case "Empurrão Tático":
                // Avance um peão duas casas, mesmo após o primeiro movimento
                System.out.println("Escolha o peão para avançar duas casas (ex: a2):");
                String coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                int x = coord.charAt(0) - 'a';
                int y = 8 - Character.getNumericValue(coord.charAt(1));
                Piece peao = null;
                for (Piece p : player.getPieces()) {
                    if (p.x == x && p.y == y && p.type.name().equals("PEAO")) {
                        peao = p;
                        break;
                    }
                }
                if (peao == null) {
                    System.out.println("Não há peão seu nessa casa.");
                    return false;
                }
                int dir = player.isWhite() ? -1 : 1;
                int destY = peao.y + 2 * dir;
                if (destY < 0 || destY > 7 || board.movePiece(peao.x, peao.y, peao.x, destY) == false) {
                    System.out.println("Não foi possível avançar o peão.");
                    return false;
                }
                System.out.println("Peão avançou duas casas!");
                return true;

            case "Recuo Seguro":
                // Volte uma peça sua à posição anterior sem consumir seu turno
                System.out.println("Escolha a peça para recuar (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                Piece psel = null;
                for (Piece p : player.getPieces()) {
                    if (p.x == x && p.y == y) {
                        psel = p;
                        break;
                    }
                }
                if (psel == null || psel.lastPosition == null) {
                    System.out.println("Peça não encontrada ou sem posição anterior.");
                    return false;
                }
                // Salva a posição atual e a última posição
                Point posAtual = new Point(psel.x, psel.y);
                Point posAnterior = new Point(psel.lastPosition.x, psel.lastPosition.y);
                // Move manualmente a peça para a posição anterior
                psel.x = posAnterior.x;
                psel.y = posAnterior.y;
                // Atualiza o tabuleiro
                board.updateGrid();
                // Atualiza o lastPosition para a posição de onde ela veio (para evitar loop de recuo)
                psel.lastPosition = posAtual;
                System.out.println("Peça voltou para a posição anterior!");
                return true;

            case "Mobilidade Extra":
                // Uma peça pode se mover novamente neste turno (mas não capturar)
                System.out.println("Escolha a peça para mover novamente (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                psel = null;
                for (Piece p : player.getPieces()) {
                    if (p.x == x && p.y == y) {
                        psel = p;
                        break;
                    }
                }
                if (psel == null) {
                    System.out.println("Peça não encontrada.");
                    return false;
                }
                System.out.println("Digite a casa de destino (ex: a3):");
                String destino = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(destino)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                int dx = destino.charAt(0) - 'a';
                int dy = 8 - Character.getNumericValue(destino.charAt(1));
                Piece capt = null;
                // Não pode capturar
                for (Piece op : board.getCurrentPlayer() == player ? board.getCurrentPlayer() == player ? board.getCurrentPlayer() == player ? new ArrayList<Piece>() : board.getCurrentPlayer().getPieces() : new ArrayList<Piece>() : new ArrayList<Piece>()) {
                    if (op.x == dx && op.y == dy) capt = op;
                }
                if (board.movePiece(x, y, dx, dy) && board.getCurrentPlayer() == player && capt == null) {
                    System.out.println("Peça movida novamente!");
                    return true;
                } else {
                    System.out.println("Não foi possível mover a peça (não pode capturar).");
                    return false;
                }

            case "Solo Escorregadio":
                // Cavalo inimigo não pode pular peças neste turno
                Player oponente = (player == board.getCurrentPlayer()) ? null : board.getCurrentPlayer();
                if (oponente == null) {
                    System.out.println("Não foi possível identificar o oponente.");
                    return false;
                }
                for (Piece p : oponente.getPieces()) {
                    if (p.type.name().equals("CAVALO")) p.affectedBySoloEscorregadioNextTurn = true;
                }
                System.out.println("Cavalos do oponente não poderão pular peças neste turno.");
                return true;

            case "Bloqueio Tático":
                // Uma peça inimiga não pode ser movida por um turno (exceto rei)
                System.out.println("Escolha a peça inimiga para bloquear (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                // Identifica o oponente corretamente
                Player inimigo = (player == board.getWhitePlayer()) ? board.getBlackPlayer() : board.getWhitePlayer();
                Piece alvo = null;
                for (Piece p : inimigo.getPieces()) {
                    if (p.x == x && p.y == y && !p.type.name().equals("REI")) {
                        alvo = p;
                        break;
                    }
                }
                if (alvo == null) {
                    System.out.println("Peça inimiga não encontrada ou é o rei.");
                    return false;
                }
                alvo.turnsBlockedByTatico = 2;
                System.out.println("Peça bloqueada por um turno!");
                return true;

            case "Reflexo Real":
                // Se uma peça sua for capturada, a peça inimiga também é destruída
                player.setReflexoRealAtivo(true); // Ativa o efeito
                System.out.println("Reflexo Real ativado! Se uma peça sua for capturada, a peça inimiga também será destruída.");
                return true;

            case "Troca Estratégica":
                // Troque de lugar duas peças suas
                System.out.println("Escolha a primeira peça (ex: a2):");
                String c1 = scanner.nextLine().trim().toLowerCase();
                System.out.println("Escolha a segunda peça (ex: b2):");
                String c2 = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(c1) || !Board.isValidCoord(c2)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                int x1 = c1.charAt(0) - 'a';
                int y1 = 8 - Character.getNumericValue(c1.charAt(1));
                int x2 = c2.charAt(0) - 'a';
                int y2 = 8 - Character.getNumericValue(c2.charAt(1));
                Piece p1 = null, p2 = null;
                for (Piece p : player.getPieces()) {
                    if (p.x == x1 && p.y == y1) p1 = p;
                    if (p.x == x2 && p.y == y2) p2 = p;
                }
                if (p1 == null || p2 == null) {
                    System.out.println("Peças não encontradas.");
                    return false;
                }
                int tx = p1.x, ty = p1.y;
                p1.x = p2.x; p1.y = p2.y;
                p2.x = tx; p2.y = ty;
                board.updateGrid();
                System.out.println("Peças trocadas!");
                return true;

            case "Silêncio Real":
                // Silencia ambos jogadores por 2 turnos completos (4 meios-turnos)
                board.getWhitePlayer().setTurnosSilencioRealRestantes(4);
                board.getBlackPlayer().setTurnosSilencioRealRestantes(4);
                System.out.println("Silêncio Real ativado! Ninguém poderá usar cartas por dois turnos completos.");
                return true;

            case "Coluna de gelo":
                // Uma coluna inteira do tabuleiro não pode ser usada por 2 turnos
                System.out.println("Escolha a coluna para congelar (a-h):");
                String col = scanner.nextLine().trim().toLowerCase();
                if (col.length() != 1 || col.charAt(0) < 'a' || col.charAt(0) > 'h') {
                    System.out.println("Coluna inválida.");
                    return false;
                }
                int colunaIndex = col.charAt(0) - 'a';
                board.ativarColunaDeGelo(colunaIndex);
                System.out.println("Coluna " + col + " congelada por 2 turnos!");
                return true;

            case "Trato Feito":
                // Consiga 2 cartas ao invés de uma
                System.out.println("Você recebe 2 cartas!");
                player.startTurn();
                player.startTurn();
                return true;

            case "Poder Supremo":
                // Elimine qualquer peça do tabuleiro imediatamente (Exceto rei ou rainha)
                System.out.println("Escolha a peça para eliminar (ex: a2):");
                coord = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(coord)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                x = coord.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(coord.charAt(1));
                Piece alvoSup = null;
                for (Piece p : board.getCurrentPlayer().getPieces()) {
                    if (p.x == x && p.y == y && !p.type.name().equals("REI") && !p.type.name().equals("RAINHA")) {
                        alvoSup = p;
                        break;
                    }
                }
                if (alvoSup == null) {
                    System.out.println("Peça não encontrada ou é rei/rainha.");
                    return false;
                }
                board.getCurrentPlayer().getPieces().remove(alvoSup);
                board.updateGrid();
                System.out.println("Peça eliminada!");
                return true;

            case "Barreira Imperial":
                board.ativarBarreiraImperial();
                System.out.println("Barreira Imperial ativada! Suas peças não podem ser capturadas no próximo turno.");
                return true;

            case "Renascimento":
                // Reviva uma peça capturada e coloque-a em sua linha inicial (exceto a rainha)
                System.out.println("Escolha a peça capturada para reviver (ex: PEAO, TORRE, CAVALO, BISPO, REI):");
                String tipo = scanner.nextLine().trim().toUpperCase();
                if (tipo.equals("RAINHA")) {
                    System.out.println("Não é possível reviver a rainha.");
                    return false;
                }
                // Simulação: revive na coluna 0 da linha inicial
                int linha = player.isWhite() ? 7 : 0;
                Piece revive = new Piece(model.enums.PieceType.valueOf(tipo), player.isWhite(), 0, linha);
                player.getPieces().add(revive);
                board.updateGrid();
                System.out.println("Peça revivida!");
                return true;

            case "Domínio Dimensional":
                // Teleporte qualquer peça sua para qualquer casa vazia
                System.out.println("Escolha a peça para teleporte (ex: a2):");
                String origem = scanner.nextLine().trim().toLowerCase();
                System.out.println("Escolha a casa de destino (ex: b3):");
                String destino2 = scanner.nextLine().trim().toLowerCase();
                if (!Board.isValidCoord(origem) || !Board.isValidCoord(destino2)) {
                    System.out.println("Coordenada inválida.");
                    return false;
                }
                x = origem.charAt(0) - 'a';
                y = 8 - Character.getNumericValue(origem.charAt(1));
                dx = destino2.charAt(0) - 'a';
                dy = 8 - Character.getNumericValue(destino2.charAt(1));
                psel = null;
                for (Piece p : player.getPieces()) {
                    if (p.x == x && p.y == y) {
                        psel = p;
                        break;
                    }
                }
                if (psel == null) {
                    System.out.println("Peça não encontrada.");
                    return false;
                }
                if (board.movePiece(x, y, dx, dy)) {
                    System.out.println("Peça teleportada!");
                    return true;
                } else {
                    System.out.println("Não foi possível teleportar a peça.");
                    return false;
                }

            default:
                System.out.println("Efeito da carta '" + card.getName() + "' não implementado.");
                return false;
        }
    }
}