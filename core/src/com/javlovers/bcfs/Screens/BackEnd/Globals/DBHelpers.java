package com.javlovers.bcfs.Screens.BackEnd.Globals;

import com.javlovers.bcfs.Screens.BackEnd.Attacks.AttackModule;
import com.javlovers.bcfs.Screens.BackEnd.Builders.AttackModuleBuilder;
import com.javlovers.bcfs.Screens.BackEnd.DB.AttackHelper;
import com.javlovers.bcfs.Screens.BackEnd.DB.DBConnection;
import com.javlovers.bcfs.Screens.BackEnd.Main.*;
import com.mysql.cj.protocol.SocketMetadata;
import sun.security.util.Password;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBHelpers {

    /**is an interface to allow modularity in connections**/
    private static DBConnection dbConnection;
    /**to allow a unified connection when constructing DBHelpers <br> DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection()) <br> So that we can easily change from supabase to localhost for testing<**/
    private static DBConnection globalConnection;
    public DBHelpers(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public static DBConnection getGlobalConnection(){
        return globalConnection;
    }


    public static void setGlobalConnection(DBConnection dbc){
        System.out.println("GC set");
        globalConnection = dbc;
    }
    public HashMap<Integer, Attack> getAllAttacksEnabled() {
        HashMap<Integer, Attack> allAttacks = new HashMap<>();
        try (Connection C = dbConnection.getConnection();
             Statement statement = C.createStatement()) {
            String query = "SELECT * FROM tblattack WHERE isDisabled = 0";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String aName = rs.getString("name");
                int aSpeed = rs.getInt("speed");
                int aDamage = rs.getInt("damage");
                double adamageMult = rs.getDouble("damageMultiplier");
                int attackModule = rs.getInt("attackModule");
                int Id = rs.getInt("attackID");
                AttackModule AM = AttackModuleBuilder.buildAttackModule(attackModule);
                Attack atk = new Attack(aName, aSpeed, aDamage, adamageMult, AM,Id);
                atk.setAttackID(rs.getInt("attackID"));
                atk.setIsDisabled(rs.getBoolean("isDisabled"));
                allAttacks.put(Id, atk);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAttacks;
    }

    public HashMap<Integer, Attack> getAllAttacks() {
        HashMap<Integer, Attack> allAttacks = new HashMap<>();
        try (Connection C = dbConnection.getConnection();
             Statement statement = C.createStatement()) {
            String query = "SELECT * FROM tblattack";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String aName = rs.getString("name");
                int aSpeed = rs.getInt("speed");
                int aDamage = rs.getInt("damage");
                double adamageMult = rs.getDouble("damageMultiplier");
                int attackModule = rs.getInt("attackModule");
                int Id = rs.getInt("attackID");
                AttackModule AM = AttackModuleBuilder.buildAttackModule(attackModule);
                Attack atk = new Attack(aName, aSpeed, aDamage, adamageMult, AM,Id);
                atk.setAttackID(rs.getInt("attackID"));
                atk.setIsDisabled(rs.getBoolean("isDisabled"));
                allAttacks.put(Id, atk);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAttacks;
    }
    public User LoginUser(String Username, String Password) {
        try (Connection c = dbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT count(*) as RECORDCOUNT,UserID,DisplayName FROM tbluser WHERE Username = ? AND Password = ?")) {
            ps.setString(1, Username);
            ps.setString(2, Password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int rows = rs.getInt("RECORDCOUNT");
            if(rows == 0){
                System.out.println("Something is not right");
                return null;
            }
            int UID = rs.getInt("UserID");
            String DisplayName = rs.getString("DisplayName");
            User.setCurrentUser(DisplayName,UID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return User.getCurrUser();
    }

    public boolean SendCockData(Cock cock) {
        ArrayList<Attack> lists = cock.getAttackList();
        boolean result = false;
        try (Connection c = dbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO `tblcock` (`UserID`, `CockName`, `Attack1ID`, `Attack2ID`, `Attack3ID`, `Attack4ID`) VALUES (?, ?, ? , ? , ? , ?)")) {
            int startInd = 3;
            ps.setInt(1, cock.getOwnerID());
            ps.setString(2, cock.getName());
            for (Attack atk : lists) {
                int atkID = AttackHelper.attackModuleToInt(atk.getAttackModule());
                ps.setInt(startInd++, atkID);
            }
            while (startInd-2 <= Cock.MAX_ATTACKS) {
                ps.setInt(startInd++, 0);
            }
            System.out.println(ps.toString());
            ps.execute();
            System.out.println("Cock Insert Successfull");
            // fetch cockdata
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public boolean sendToTempCock(Cock cock) {
        if (!valueExists("tblCurrCock", "OwnerID", String.valueOf(cock.getOwnerID()), false)) {
            ArrayList<Attack> lists = cock.getAttackList();
            boolean result = false;
            try (Connection c = dbConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO `tblCurrCock` (`OwnerID`, `CockName`, `Attack1ID`, `Attack2ID`, `Attack3ID`, `Attack4ID`) VALUES (?, ?, ? , ? , ? , ?)")) {
                int startInd = 3;
                ps.setInt(1, cock.getOwnerID());
                ps.setString(2, cock.getName());
                for (Attack atk : lists) {
                    int atkID = AttackHelper.attackModuleToInt(atk.getAttackModule());
                    ps.setInt(startInd++, atkID);
                }

                while (startInd - 2 <= Cock.MAX_ATTACKS) {
                    ps.setInt(startInd++, 0);
                }
                System.out.println(ps.toString());
                ps.execute();
                System.out.println("Cock Insertion Successfull");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return result;
        } else {
            System.out.println("duplicate entry");
            ArrayList<Attack> lists = cock.getAttackList();
            boolean result = false;
            try (Connection c = dbConnection.getConnection();
                 PreparedStatement ps = c.prepareStatement("UPDATE tblCurrCock  SET Attack1ID = ?, Attack2ID = ?, Attack3ID=?, Attack4ID=?,CockName=? where OwnerID = ?")) {
                int startInd=1;
                for (Attack atk : lists) {
                    int atkID = atk.getAttackID();
                    ps.setInt(startInd++, atkID);
                }
                while (startInd-1 <= Cock.MAX_ATTACKS) {
                    ps.setInt(startInd++, 0);
                }
                ps.setString(5,cock.getName());
                ps.setInt(6,cock.getOwnerID());


                boolean success = ps.executeUpdate()>0;
                if(success)System.out.println("Cock Update Successfull");
                return success;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int getCockID(Cock cock){
            ArrayList<Attack> lists = cock.getAttackList();
            int res = -1;
            try(Connection C = dbConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("SELECT CockID FROM tblcock WHERE UserID = ? AND CockName = ? AND Attack1ID = ? AND Attack2ID = ? AND Attack3ID = ? AND Attack4ID = ?")) {
                ps.setInt(1,cock.getOwnerID());
                ps.setString(2,cock.getName());
                int startInd = 3;
                for(Attack atk: lists){
                    ps.setInt(startInd++,AttackHelper.attackModuleToInt(atk.getAttackModule()));
                }
                while(startInd-2 <= Cock.MAX_ATTACKS){
                    ps.setInt(startInd++,0);
                }
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    res = rs.getInt("CockID");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return res;
    }

    public HashMap<Integer, Cock> getAllCockData(){
        HashMap<Integer, Cock> cockData = null;
        try(Connection c = dbConnection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tblcock")){
            ResultSet rs = ps.executeQuery();
            HashMap<Integer,Attack> allAttack = AttackHelper.fetchAllAttack();
            cockData = new HashMap<>();
            while(rs.next()){
                Cock cock = new Cock(
                        rs.getString("CockName"),
                        rs.getInt("UserID")
                );
                int[] AttackIDs = {rs.getInt("Attack1ID"),rs.getInt("Attack2ID"),rs.getInt("Attack3ID"),rs.getInt("Attack4ID")};
                cock.setCockID(rs.getInt("UserID"));
                for(int AIDs: AttackIDs){
                    if(AIDs == 0) break;
                    Attack tempAtk = AttackHelper.cloneAttack(allAttack.get(AIDs));
                    cock.addAttack(tempAtk);
                }
                cockData.put(rs.getInt("CockID"),cock);
            }
            System.out.println("Cocks Fetched Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cockData;
    }
    public HashMap<Integer,Cock> getAllCurrCockData(){
        HashMap<Integer, Cock> cockData = null;
        try(Connection c = dbConnection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tblCurrCock")){
            ResultSet rs = ps.executeQuery();
            cockData = new HashMap<>();
            while(rs.next()){
                Cock cock = new Cock(
                        rs.getString("CockName"),
                        rs.getInt("OwnerID")
                );
                int[] AttackIDs = {rs.getInt("Attack1ID"),rs.getInt("Attack2ID"),rs.getInt("Attack3ID"),rs.getInt("Attack4ID")};
                cock.setCockID(rs.getInt("CurrID"));
                HashMap<Integer,Attack> allAttack = AttackHelper.fetchAllAttack();
                for(int AIDs: AttackIDs){
                    if(AIDs == 0) break;
                    Attack tempAtk = AttackHelper.cloneAttack(allAttack.get(AIDs));
                    cock.addAttack(tempAtk);
                }
                cockData.put(rs.getInt("OwnerID"),cock);
            }
            System.out.println("Cocks Fetched Successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cockData;
    }

    //TODO: Update, Using The Outdated tbl
    public boolean challengePlayer(int invitorCockID,int inviteeID, int invitorID){
        boolean isSuccess = false;
        try(Connection C = dbConnection.getConnection();){
            PreparedStatement ps = C.prepareStatement("Insert into tblinvite(invitorCockID,inviteeID,invitorID) values(?,?,?)");
            ps.setInt(1,invitorCockID);
            ps.setInt(2,inviteeID);
            ps.setInt(3,invitorID);
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean createMatch(Cock invitorCock, Cock inviteeCock, int winnerID){
        int Cock1isSaved = getCockID(invitorCock);
        int Cock2isSaved = getCockID(inviteeCock);
        if(Cock1isSaved == -1){
            SendCockData(invitorCock);
            Cock1isSaved = getCockID(invitorCock);
        }
        if(Cock2isSaved == -1){
            SendCockData(inviteeCock);
            Cock2isSaved = getCockID(inviteeCock);
        }
        try(Connection C = dbConnection.getConnection();){
            PreparedStatement ps = C.prepareStatement("Insert into tblmatch(invitorCockID,inviteeCockID,winner) values (?,?,?);");
            ps.setInt(1,Cock1isSaved);
            ps.setInt(2,Cock2isSaved);
            ps.setInt(3,winnerID);
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int insertWhenNotExistCock(Cock cock){
        return -1;
    }
    public HashMap<Integer, ArrayList<Integer>> getMatchesByUser(int UserID){
        try(Connection C = dbConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("SELECT matchID,invitorCockID,inviteeCockID,winner FROM tblmatch JOIN tblcock AS invitor ON tblmatch.invitorCockID = invitor.CockID JOIN tblcock AS invitee ON tblmatch.inviteeCockID = invitee.CockID WHERE invitor.UserID = ? OR invitee.UserID = ? ORDER BY matchID;")){
            HashMap<Integer,ArrayList<Integer>> Matches = new HashMap<>();
            ps.setInt(1,UserID);
            ps.setInt(2,UserID);
            ResultSet res = ps.executeQuery();
            while(res.next()){
                int MatchID = res.getInt("matchID");
                int me = res.getInt("invitorCockID");
                int them = res.getInt("inviteeCockID");
                int winner = res.getInt("winner");
                ArrayList<Integer> something = new ArrayList<Integer>();
                something.add(me);
                something.add(them);
                something.add(winner);
                Matches.put(MatchID,something);
            }
            return Matches;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean setWinner(int matchID, int WinnerID){
        Boolean res;
        try(Connection C = dbConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("UPDATE tblmatch SET winner = ? WHERE matchID = ?")){
            ps.setInt(1,WinnerID);
            ps.setInt(2,matchID);
            res = ps.execute();
            if(res){
                res = res && DeletePreviousMatch(matchID);
                return res;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public boolean batchSetWinner(HashMap<Integer,Integer> reses){
        Boolean res;
        try(Connection C = dbConnection.getConnection();){
            StringBuilder switchCase = new StringBuilder();
            for(int matchID : reses.keySet()){
                String CaseStament = String.format(" WHEN matchID = %d THEN %d",matchID,reses.get(matchID));
                switchCase.append(CaseStament);
            }
            switchCase.append(" ELSE winner");
            Iterator<Integer> it= reses.keySet().iterator();
            Integer prevs = null;
            StringBuilder InsideSet = new StringBuilder();
            while(it.hasNext()){
                Integer currs = it.next();
                prevs = currs;
                InsideSet.append(prevs);
                if(it.hasNext()){
                    InsideSet.append(",");
                }
            }
            Statement st = C.createStatement();
            String query = String.format("UPDATE tblmatch SET winner = CASE %s END WHERE matchID IN (%s)",switchCase,InsideSet);
            System.out.println(query);
            return st.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean DeletePreviousMatch(int matchID){
        try(Connection C = dbConnection.getConnection();
        PreparedStatement ps = C.prepareStatement("DELETE FROM tblmatch WHERE matchID = ?")){
            ps.setInt(1,matchID);
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<MatchFacade> getAllUnverifiedMatches(){
        try(Connection C = dbConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("SELECT * FROM tblmatch WHERE winner = 0")){
            ResultSet res = ps.executeQuery();
            HashMap<Integer,Cock> allCocks = getAllCockData();
            ArrayList<MatchFacade> Matches = new ArrayList<>();
            while(res.next()){
                int invtrID = res.getInt("invitorCockID");
                int invteID = res.getInt("inviteeCockID");
                MatchFacade mf = new MatchFacade(res.getInt("matchID"),invtrID,invteID);
                Matches.add(mf);
            }
            return Matches;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Integer> getChallenges(int userID){
        ArrayList<Integer> inviteIds = new ArrayList<>();

        try(Connection C = dbConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("Select InviteID from tblinvite where isChallenge = 1 and referenceID = ?")){
            ps.setInt(1,userID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                inviteIds.add(rs.getInt("InviteID"));
            }
            return inviteIds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateDetails(int userID, String displayName, String username, String Password){
        try(Connection C = dbConnection.getConnection();
            PreparedStatement ps = C.prepareStatement("UPDATE tbluser Set DisplayName = ?, Username = ?, Password = ? where UserID = ?")){
            ps.setString(1,displayName);
            ps.setString(2,username);
            ps.setString(3,Password);
            ps.setInt(4,userID);
            return ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getDisplayName(int userid){
        try(Connection C = dbConnection.getConnection();){
            PreparedStatement ps = C.prepareStatement("Select DisplayName from tbluser where UserID = ?");
            ps.setInt(1,userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString("DisplayName");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public int getInvitorCockID(int inviteID){
        try (Connection c = dbConnection.getConnection();) {
            PreparedStatement ps = c.prepareStatement("Select CockID from tblinvite where InviteID = ?");
            ps.setInt(  1, inviteID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt("CockID");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; //returns -1 if error occurs
    }

    public boolean insertMatch(int invitorCockID, int inviteeCockId){
        try (Connection c = dbConnection.getConnection();) {
            PreparedStatement ps = c.prepareStatement("Insert into tblmatch(invitorCockID, inviteeCockID) values (?,?)");
            ps.setInt(  1, invitorCockID);
            ps.setInt(  1, inviteeCockId);
            return ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean valueExists(String tablename, String columnname, String value, boolean isValueString){
        try (Connection c = dbConnection.getConnection();) {
            PreparedStatement ps = c.prepareStatement("Select * from "+tablename+" where "+columnname+" = ?");
            if(!isValueString){
                ps.setInt(1,Integer.parseInt(value));
            }else{
                ps.setString(1,value);
            }
            ResultSet rs = ps.executeQuery();
            int number_of_rows = 0;
            while (rs.next()) {
                number_of_rows++;
            }
            return number_of_rows>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int createAccount(String DisplayName,String Username, String Password){
        try (Connection c = dbConnection.getConnection();) {
            boolean usernameExists = valueExists("tbluser","Username",Username,true);
            if(usernameExists ){
                System.out.println("Username already exists");
                return -1;
            }else{
                PreparedStatement ps = c.prepareStatement("Insert into tbluser(Displayname,Username,Password) values (?,?,?)");
                ps.setString(1,DisplayName);
                ps.setString(2,Username);
                ps.setString(3,Password);
                ps.execute();
                return getUserId(Username);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserId(String username){
        try (Connection c = dbConnection.getConnection();) {
            PreparedStatement ps = c.prepareStatement("Select UserID from tbluser where Username=?");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return rs.getInt("UserID");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    public boolean sendAttack(Attack atk){
        boolean res = false;
        try(Connection c = dbConnection.getConnection();
         PreparedStatement ps = c.prepareStatement("INSERT INTO tblattack(name,speed,damage,damageMultiplier,attackModule) values (?,?,?,?,?)")){
            ps.setString(1,atk.getName());
            ps.setInt(2,atk.getSpeed());
            ps.setInt(3,atk.getDamage());
            ps.setDouble(4,atk.getDamageMultiplier());
            ps.setInt(5,AttackHelper.attackModuleToInt(atk.getAttackModule()));
            res = ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public boolean toggleIsDisabled(int AttackID, boolean isDisabledState){
        boolean res = false;
        try(Connection c = dbConnection.getConnection();
            PreparedStatement ps = c.prepareStatement("UPDATE tblattack SET isDisabled = ? WHERE attackID = ?")){
            ps.setBoolean(1,isDisabledState);
            ps.setInt(2,AttackID);
            res = ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public HashMap<Integer,String> getAllDIsplayNames(){
        HashMap<Integer,String> Unames = new HashMap<>();
        try(Connection C = dbConnection.getConnection();){
            Statement st = C.createStatement();
            String querry = "SELECT UserID,DisplayName FROM tbluser";
            ResultSet rs =  st.executeQuery(querry);
            while (rs.next()){
                Unames.put(rs.getInt("UserID"), rs.getString("DisplayName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Unames;
    }
}
