package com.javlovers.bcfs.Screens.BackEnd.Attacks;


import com.javlovers.bcfs.Screens.BackEnd.Main.Attack;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;

public class Heal implements AttackModule{

    @Override
    public void apply(Cock owner, Cock target, Attack parent) {
        int damage = parent.getDamage() + (int) (owner.getStat(Cock.StatName.DAMAGE, Cock.StatType.CURRENT) * parent.getDamageMultiplier());
        int newHealth = owner.getStat(Cock.StatName.HEALTH, Cock.StatType.BONUS) + damage;
        owner.setStat(Cock.StatName.HEALTH, Cock.StatType.BONUS, newHealth);
    }

    @Override
    public String toString() {
        return "Heal";
    }
}
