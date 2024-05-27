package com.javlovers.bcfs.Screens.BackEnd.Builders;


import com.javlovers.bcfs.Screens.BackEnd.Attacks.AttackModule;
import com.javlovers.bcfs.Screens.BackEnd.Attacks.Heal;
import com.javlovers.bcfs.Screens.BackEnd.Attacks.Leech;
import com.javlovers.bcfs.Screens.BackEnd.Attacks.SingleAttack;

public class AttackModuleBuilder {
    public static AttackModule buildAttackModule(int type){
        AttackModule res;
        switch (type){
            case 1:
                res = new SingleAttack();
                break;
            case 2:
                res = new Leech();
                break;
            case 3:
                res = new Heal();
                break;
            default:
                throw new IllegalStateException("attack module not found");
        }
        return res;
    }
}
