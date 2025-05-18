import model.*;
import model.enums.*;
import logic.CardEffects;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(Board.TITLE_ART);
        System.out.println("Bem-vindo ao Magichess! Digite 'help' para ver os comandos.\n");
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();

        while (!board.isGameOver()) {
            try {
                Player currentPlayer = board.getCurrentPlayer();
                String nomeJogador = currentPlayer.isWhite() ? "Jogador 1" : "Jogador 2";
                // Informação de turno
                System.out.println("\n--- Turno de " + nomeJogador + " (Turno #" + currentPlayer.getTurnCount() + ") ---");

                // Tabuleiro
                board.printBoard();

                // Informação da carta comprada removida daqui para evitar duplicidade
                // Interface do jogador
                System.out.println("Cooldowns");
System.out.println("COMUM:(" + currentPlayer.getCommonCategoryCooldownTurnsLeft() + "/1)");
System.out.println("RARA:(" + currentPlayer.getRareCategoryCooldownTurnsLeft() + "/3)");
System.out.println("LENDARIA:(" + currentPlayer.getLegendaryCategoryCooldownTurnsLeft() + "/6)");
                currentPlayer.printHand();

                System.out.print("\nDigite seu comando ('help', 'move (Peça de Origem) (Destino)', 'use (Número da Carta)', 'pass'): ");
                if (!scanner.hasNextLine()) {
                    System.out.println("Entrada encerrada. Jogo finalizado.");
                    break;
                }
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("exit")) break;
                else if (input.equals("help")) printHelp();
                else if (input.startsWith("move ")) {
                    String[] parts = input.split(" ");
                    if (parts.length == 3 && Board.isValidCoord(parts[1]) && Board.isValidCoord(parts[2])) {
                        int fromX = parts[1].charAt(0) - 'a';
                        int fromY = 8 - Character.getNumericValue(parts[1].charAt(1));
                        int toX = parts[2].charAt(0) - 'a';
                        int toY = 8 - Character.getNumericValue(parts[2].charAt(1));
                        if (board.movePiece(fromX, fromY, toX, toY)) {
                            System.out.println("Movimento realizado!");
                            if (!board.isGameOver()) board.switchPlayer();
                        } else System.out.println("Movimento inválido ou falhou!");
                    } else System.out.println("Formato: 'move <origem> <destino>' (ex: move a2 a4).");
                } else if (input.startsWith("use ")) {
                    if (currentPlayer.isSilenced()) System.out.println("Silêncio Real ativo! Não pode usar cartas.");
                    else if (currentPlayer.hasPlayedCardThisTurn()) System.out.println("Você já usou uma carta neste turno!");
                    else {
                        try {
                            int cardIdx = Integer.parseInt(input.substring(4).trim()) - 1;
                            if (currentPlayer.isValidCardIndex(cardIdx)) {
                                Card cardToUse = currentPlayer.getCard(cardIdx);
                                if (cardToUse.canBeUsed(currentPlayer)) {
                                    if (CardEffects.apply(cardToUse, board, currentPlayer, scanner)) {
                                        System.out.println("Carta '" + cardToUse.getName() + "' usada com sucesso.");
                                        cardToUse.use(currentPlayer);
                                        currentPlayer.setHasPlayedCardThisTurn(true);
                                        currentPlayer.removeCard(cardToUse); // Remove a carta da mão após uso
                                    } else System.out.println("Não foi possível aplicar o efeito da carta '" + cardToUse.getName() + "'.");
                                } else System.out.println("Carta '" + cardToUse.getName() + "' não pode ser usada: " + cardToUse.getCooldownStatus(currentPlayer));
                            } else System.out.println("Índice de carta inválido!");
                        } catch (NumberFormatException e) { System.out.println("Formato: 'use <numero_da_carta>'."); }
                    }
                } else if (input.equals("pass") || input.equals("endturn")) {
                    System.out.println(nomeJogador + " passou o turno.");
                    board.switchPlayer();
                } else System.out.println("Comando não reconhecido.");
            } catch (Exception e) {
                System.err.println("ERRO INESPERADO: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
        scanner.close();
        // Libera referências para facilitar coleta de lixo
        board = null;
        scanner = null;
        System.gc();
        // Remove arquivos .class gerados (limpeza pós-jogo)
        try {
            String[] dirs = {"bin", "bin/logic", "bin/model", "bin/model/enums"};
            for (String dir : dirs) {
                java.nio.file.Path path = java.nio.file.Paths.get(dir);
                if (!java.nio.file.Files.exists(path)) continue;
                java.nio.file.Files.walk(path)
                    .filter(p -> p.toString().endsWith(".class"))
                    .forEach(p -> {
                        try { java.nio.file.Files.deleteIfExists(p); } catch (Exception ex) {}
                    });
            }
            System.out.println("Arquivos .class removidos.");
        } catch (Exception ex) {
            // Silencia erro se diretório não existir ou não for possível remover
        }
        System.out.println("Jogo finalizado.");
    }

    private static void printHelp() {
        System.out.println("\nComandos disponíveis:");
        System.out.println("  move <origem> <destino> - Move uma peça (ex: move a2 a4)");
        System.out.println("  use <número>            - Usa a carta da sua mão com o número especificado");
        System.out.println("  pass / endturn          - Passa o turno");
        System.out.println("  help                    - Mostra esta ajuda");
        System.out.println("  exit                    - Sai do jogo");
    }
}