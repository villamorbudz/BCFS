package com.javlovers.bcfs.Screens.BackEnd.Attacks;


import com.javlovers.bcfs.Screens.BackEnd.Main.Attack;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;

public interface AttackModule {
     void apply(Cock Owner, Cock Target, Attack parent);
}
