package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingsService
{

        private Connection dbConnection;

        public SettingsService() throws SQLException {
                this.dbConnection= DatabaseConnection
                        .getInstance().getConnection();
        }

        public boolean setSetting(String name, String value) throws SQLException {
                String query= "update settings set value = ? where name = ? ";
                PreparedStatement preparedStatement = this.dbConnection.prepareStatement(query);
                preparedStatement.setString(1, value);
                preparedStatement.setString(2, name);
                int result = preparedStatement.executeUpdate();
                if (result != 0){
                        return true;
                }
                return false;
        }
        
        public String getSetting(String name) throws SQLException {
                String query = "SELECT value from settings where name = ?";
                PreparedStatement preparedStatement = this.dbConnection.prepareStatement(query);
                preparedStatement.setString(1, name);
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()){
                        return rs.getString("value");
                } else {
                        return "";
                }
        }

        public List<String> printList() {
                List<String> ls = new ArrayList<>();
                PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
                for (PrintService ps : services) {
                        ls.add(ps.getName());
                }
                return ls;
        }
}
