package eu.ovmc;

import java.sql.*;

public class JDBC {

    public Connection connectToCMIDB(String path){
        Connection con = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:"+path);
            System.out.println("Connected to the CMI database.");

        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e +" Database Connection to CMI FAILED!");
            e.printStackTrace();
        }

        return con;
    }

    public Connection connectToAuthDB(String path){
        Connection con = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:"+path);
            System.out.println("Connected to the AuthMe database.");

        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e +" Database Connection to AuthMe FAILED!");
            e.printStackTrace();
        }

        return con;
    }

    public Connection connectToLuckDB(String path){
        Connection con = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:"+path);
            System.out.println("Connected to the LuckPerms database.");

        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e +" Database Connection to LuckPerms FAILED!");
            e.printStackTrace();
        }

        return con;
    }

    public ResultSet getAllUsers(Connection con){
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();

            rs = stmt.executeQuery( "SELECT users.username, users.player_uuid, users.Homes FROM users;" );

        } catch (SQLException e) {
            System.out.println(e +" Database Connection FAILED!");
        }

        return rs;

    }

    public void clearHomesForPlayer(Connection con, String uuid){
        Statement stmt;

        try{
            stmt = con.createStatement();
            String sql = "UPDATE users SET Homes = NULL WHERE users.player_uuid = '"+uuid+"'";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getTimeAndRank(Connection con, String uuid){
        ResultSet rs;

        try{
            rs = con.createStatement().executeQuery("SELECT users.TotalPlayTime, users.Rank " +
                    "FROM users " +
                    "WHERE users.player_uuid = '"+uuid+"'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rs;
    }

    public ResultSet getUserName(Connection con, String uuid){
        ResultSet rs;

        try{
            rs = con.createStatement().executeQuery("SELECT users.username " +
                    "FROM users " +
                    "WHERE users.player_uuid = '"+uuid+"'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rs;
    }



    public void removePlayerData(Connection con, String uuid){
        //get the user id
        ResultSet rs;
        try{
            rs = con.createStatement().executeQuery("SELECT users.id " +
                    "FROM users " +
                    "WHERE users.player_uuid = '"+uuid+"'");
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //remove inventories from the user id
        try{
            while(rs.next()){
                int id = rs.getInt("id");
                System.out.println("User ID: "+ id);

                Statement stmt = con.createStatement();
                String sql = "DELETE FROM inventories WHERE inventories.player_id =" +id;
                stmt.executeUpdate(sql);
                System.out.println("Removed inventory from CMI");
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove inventory from CMI");
            throw new RuntimeException(e);
        }

        //remove the user
        try{
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM users WHERE users.player_uuid = '"+uuid+"'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Removed user from users table in CMI");
        } catch (SQLException e) {
            System.out.println("Unable to remove the user from users table in CMI");
            throw new RuntimeException(e);
        }

    }

    public void removePlayerFromAuth(Connection con, String username){
        try{
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM authme WHERE authme.realname = '"+username+"'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Removed user from Authme");
        } catch (SQLException e) {
            System.out.println("Unable to remove the user from Authme");
            throw new RuntimeException(e);
        }
    }

    public void removePlayerFromLuckPerm(Connection con, String uuid){
        try{
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM luckperms_players WHERE luckperms_players.uuid = '"+uuid+"'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Removed user from LuckPerms - lp_players");

        } catch (SQLException e) {
            System.out.println("Unable to remove the user from LuckPerms - lp_players");
            throw new RuntimeException(e);
        }

        try{
            Statement stmt = con.createStatement();
            String sql = "DELETE FROM luckperms_user_permissions WHERE luckperms_user_permissions.uuid = '"+uuid+"'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Removed user from LuckPerms - lp_user_permissions");

        } catch (SQLException e) {
            System.out.println("Unable to remove the user from LuckPerms - lp_user_permissions");
            throw new RuntimeException(e);
        }

    }








}
