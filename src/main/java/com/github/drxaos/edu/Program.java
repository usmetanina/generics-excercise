package com.github.drxaos.edu;
import java.sql.*;

public class Program
{
    public static void main( String args[] )
    {
        try {
            SqliteSavedList<Integer> list = new SqliteSavedList<Integer>("mylist");
            list.add(0,155);
            list.add(1,333);
            list.add(2,896);

            list.remove(1);

            list.set(0,111);
        }
        catch (SQLException ex) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}