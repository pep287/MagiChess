package model;

import model.enums.RarityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card {
    private String name;
    private RarityType rarityType;
    private String effectDescription;
    private int baseCooldown;
    private int currentCooldown = 0;

    // Lista de cartas disponíveis (adicione mais conforme necessário)
    private static final List<Card> CARTAS_DISPONIVEIS = new ArrayList<>();
    static {
        CARTAS_DISPONIVEIS.add(new Card("Empurrao Tatico", RarityType.COMUM, "Avance um peao duas casas", 1));
        CARTAS_DISPONIVEIS.add(new Card("Recuo Seguro", RarityType.COMUM, "Volte uma peca sua a posicao anterior", 1));
        CARTAS_DISPONIVEIS.add(new Card("Mobilidade Extra", RarityType.COMUM, "Uma peca pode se mover novamente", 1));
        CARTAS_DISPONIVEIS.add(new Card("Solo Escorregadio", RarityType.COMUM, "Cavalo inimigo nao pode pular pecas neste turno", 1));
        CARTAS_DISPONIVEIS.add(new Card("Bloqueio Tatico", RarityType.COMUM, "Uma peca inimiga nao pode ser movida por um turno (exceto rei)", 1));
        CARTAS_DISPONIVEIS.add(new Card("Reflexo Real", RarityType.RARA, "Se uma peca sua for capturada, a peca inimiga tambem eh destruida", 3));
        CARTAS_DISPONIVEIS.add(new Card("Troca Estrategica", RarityType.RARA, "Troque de lugar duas pecas suas", 3));
        CARTAS_DISPONIVEIS.add(new Card("Silencio Real", RarityType.RARA, "Ninguem pode usar cartas por 2 turnos", 3));
        CARTAS_DISPONIVEIS.add(new Card("Coluna de gelo", RarityType.RARA, "Uma coluna inteira do tabuleiro nao pode ser usada por 2 turnos", 3));
        CARTAS_DISPONIVEIS.add(new Card("Trato Feito", RarityType.RARA, "Consiga 2 cartas ao inves de uma", 3));
        CARTAS_DISPONIVEIS.add(new Card("Poder Supremo", RarityType.LENDARIA, "Elimine qualquer peca do tabuleiro imediatamente (Exceto rei ou rainha)", 6));
        CARTAS_DISPONIVEIS.add(new Card("Barreira Imperial", RarityType.LENDARIA, "Suas pecas nao podem ser capturadas no proximo turno", 6));
        CARTAS_DISPONIVEIS.add(new Card("Renascimento", RarityType.LENDARIA, "Reviva uma peca capturada e coloque-a em sua linha inicial (exceto a rainha)", 6));
        CARTAS_DISPONIVEIS.add(new Card("Dominio Dimensional", RarityType.LENDARIA, "Teleporte qualquer peca sua para qualquer casa vazia", 6));
    }

    public static Card sorteiaCartaAleatoria() {
        Random rand = new Random();
        double prob = rand.nextDouble();
        RarityType tipo;
        if (prob < 0.63) tipo = RarityType.COMUM;
        else if (prob < 0.93) tipo = RarityType.RARA;
        else tipo = RarityType.LENDARIA;
        // Filtra cartas do tipo
        List<Card> candidatas = new ArrayList<>();
        for (Card c : CARTAS_DISPONIVEIS) {
            if (c.getRarityType() == tipo) candidatas.add(c);
        }
        if (candidatas.isEmpty()) return CARTAS_DISPONIVEIS.get(rand.nextInt(CARTAS_DISPONIVEIS.size()));
        Card sorteada = candidatas.get(rand.nextInt(candidatas.size()));
        // Retorna uma nova instância para não compartilhar cooldown
        return new Card(sorteada.getName(), sorteada.getRarityType(), sorteada.getEffectDescription(), sorteada.getBaseCooldown());
    }

    public Card(String name, RarityType rarityType, String effectDescription, int baseCooldown) {
        this.name = name;
        this.rarityType = rarityType;
        this.effectDescription = effectDescription;
        this.baseCooldown = baseCooldown;
    }

    public String getName() { return name; }
    public RarityType getRarityType() { return rarityType; }
    public String getEffectDescription() { return effectDescription; }
    public int getBaseCooldown() { return baseCooldown; }
    public int getCurrentCooldown() { return currentCooldown; }

    public boolean canBeUsed(Player player) {
        if (currentCooldown > 0) return false;
        if (rarityType == RarityType.RARA && player.getRareCategoryCooldownTurnsLeft() > 0) return false;
        if (rarityType == RarityType.LENDARIA && player.getLegendaryCategoryCooldownTurnsLeft() > 0) return false;
        return true;
    }

    public String getCooldownStatus(Player player) {
        if (currentCooldown > 0) return "Carta em cooldown individual: " + currentCooldown + " turno(s) desta carta.";
        if (rarityType == RarityType.RARA && player.getRareCategoryCooldownTurnsLeft() > 0)
            return "Cooldown de categoria RARA: " + player.getRareCategoryCooldownTurnsLeft() + " turno(s) do jogador.";
        if (rarityType == RarityType.LENDARIA && player.getLegendaryCategoryCooldownTurnsLeft() > 0)
            return "Cooldown de categoria LENDARIA: " + player.getLegendaryCategoryCooldownTurnsLeft() + " turno(s) do jogador.";
        return "Pronta para uso.";
    }

    public void use(Player player) {
        currentCooldown = baseCooldown;
        if (rarityType == RarityType.COMUM) player.setCommonCategoryCooldownTurnsLeft(1);
        else if (rarityType == RarityType.RARA) player.setRareCategoryCooldownTurnsLeft(3);
        else if (rarityType == RarityType.LENDARIA) player.setLegendaryCategoryCooldownTurnsLeft(6);
    }

    public void updateIndividualCooldown() { if (currentCooldown > 0) currentCooldown--; }

    @Override
    public String toString() {
        return "[" + rarityType + "] " + name + ": " + effectDescription;
    }
}