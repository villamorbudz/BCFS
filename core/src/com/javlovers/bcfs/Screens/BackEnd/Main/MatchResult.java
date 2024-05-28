package com.javlovers.bcfs.Screens.BackEnd.Main;

import java.util.ArrayList;

public class MatchResult {
    int playerID;
    int enemyID;
    int matchID;
    int damageDealt;
    int damageTaken;
    int healthHealed;
    int winnerID;
    int totalTurns;
    ArrayList<String> attackOrder;

    public MatchResult(int PlayerID,int EnemyID,int MatchID){
        playerID = PlayerID;
        enemyID = EnemyID;
        matchID = MatchID;
        damageDealt = 0;
        damageTaken = 0;
        healthHealed = 0;
        winnerID = 0;
        totalTurns = 0;
        attackOrder = new ArrayList<>();
    }
    public MatchResult addAttackOrder(Attack atk){
        String atkString= atk.getName() + "----" + atk.getOwner().getName();
        attackOrder.add(atkString);
        return this;
    }

    public int getPlayerID() {
        return playerID;
    }

    public MatchResult setPlayerID(int playerID) {
        this.playerID = playerID;
        return this;
    }

    public int getEnemyID() {
        return enemyID;
    }

    public MatchResult setEnemyID(int enemyID) {
        this.enemyID = enemyID;
        return this;
    }

    public int getMatchID() {
        return matchID;
    }

    public MatchResult setMatchID(int matchID) {
        this.matchID = matchID;
        return this;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public MatchResult setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
        return this;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public MatchResult setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
        return this;
    }

    public int getHealthHealed() {
        return healthHealed;
    }

    public MatchResult setHealthHealed(int healthHealed) {
        this.healthHealed = healthHealed;
        return this;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public MatchResult setWinnerID(int winnerID) {
        this.winnerID = winnerID;
        return this;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public MatchResult setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
        return this;
    }
}
