package com.javlovers.bcfs.Screens.BackEnd.Attacks;

import com.javlovers.bcfs.Screens.BackEnd.Main.Attack;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;

public class SingleAttack implements AttackModule{
    @Override
    public void apply(Cock owner, Cock entity, Attack parent) {
        int damage = parent.getDamage() + (int) (owner.getStat(Cock.StatName.DAMAGE, Cock.StatType.CURRENT) * parent.getDamageMultiplier());
        entity.addStat(Cock.StatName.HEALTH, Cock.StatType.BONUS,damage * -1);
    }
}
