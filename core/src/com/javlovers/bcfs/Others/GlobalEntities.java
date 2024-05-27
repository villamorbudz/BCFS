package com.javlovers.bcfs.Others;

import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;
import com.javlovers.bcfs.Screens.BackEnd.Main.User;

public class GlobalEntities {
    public static User currentUser = null;
    public static Cock CurrentCock = null;
    public static void setCurrentUser(User currUser){
        currentUser = currUser;
        CurrentCock = new Cock("",currentUser.getUserID());
    }
}
