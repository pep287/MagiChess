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
        CARTAS_DISPONIVEIS.add(new Card("Empurrão Tático", RarityType.COMUM, "Avance um peão duas casas", 1));
        CARTAS_DISPONIVEIS.add(new Card("Recuo Seguro", RarityType.COMUM, "Volte uma peça sua à posição anterior", 1));
        CARTAS_DISPONIVEIS.add(new Card("Mobilidade Extra", RarityType.COMUM, "Uma peça pode se mover novamente", 1));
        CARTAS_DISPONIVEIS.add(new Card("Solo Escorregadio", RarityType.COMUM, "Cavalo inimigo não pode pular peças neste turno", 1));
        CARTAS_DISPONIVEIS.add(new Card("Bloqueio Tático", RarityType.COMUM, "Uma peça inimiga não pode ser movida por um turno (exceto rei)", 1));
        CARTAS_DISPONIVEIS.add(new Card("Reflexo Real", RarityType.RARA, "Se uma peça sua for capturada, a peça inimiga também é destruída", 3));
        CARTAS_DISPONIVEIS.add(new Card("Troca Estratégica", RarityType.RARA, "Troque de lugar duas peças suas", 3));
        CARTAS_DISPONIVEIS.add(new Card("Silêncio Real", RarityType.RARA, "Ninguém pode usar cartas por 2 turnos", 3));
        CARTAS_DISPONIVEIS.add(new Card("Coluna de gelo", RarityType.RARA, "Uma coluna inteira do tabuleiro não pode ser usada por 2 turnos", 3));
        CARTAS_DISPONIVEIS.add(new Card("Trato Feito", RarityType.RARA, "Consiga 2 cartas ao invés de uma", 3));
        CARTAS_DISPONIVEIS.add(new Card("Poder Supremo", RarityType.LENDARIA, "Elimine qualquer peça do tabuleiro imediatamente (Exceto rei ou rainha)", 6));
        CARTAS_DISPONIVEIS.add(new Card("Barreira Imperial", RarityType.LENDARIA, "Suas peças não podem ser capturadas no próximo turno", 6));
        CARTAS_DISPONIVEIS.add(new Card("Renascimento", RarityType.LENDARIA, "Reviva uma peça capturada e coloque-a em sua linha inicial (exceto a rainha)", 6));
        CARTAS_DISPONIVEIS.add(new Card("Domínio Dimensional", RarityType.LENDARIA, "Teleporte qualquer peça sua para qualquer casa vazia", 6));
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
            return "Cooldown de categoria LENDÁRIA: " + player.getLegendaryCategoryCooldownTurnsLeft() + " turno(s) do jogador.";
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