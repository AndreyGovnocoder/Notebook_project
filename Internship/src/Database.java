import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class Database
{
    public static File file = new File("");
    public static String path = file.getAbsolutePath();
    private static final String DB_URL = "jdbc:sqlite://" + path + "\\database\\Notebook_database.db";
    static final String PERSONALDATA_TABLE = "personal_data";
    static final String POSITONS_TABLE = "positions";

    static void closePrRsAndConnection(PreparedStatement pr, ResultSet rs, Connection conn)
    {

        if(rs != null)
            try
            {
                rs.close();
            } catch(Exception e)
            {
                e.printStackTrace();
            }

        if(pr != null)
            try
            {
                pr.close();
            } catch(Exception e)
            {
                e.printStackTrace();
            }

        if(conn != null)
            try
            {
                conn.close();
            } catch(Exception e)
            {
                e.printStackTrace();
            }

    }

    static boolean testConnection()
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("SELECT " +
                    " _id " +
                    " FROM " +  PERSONALDATA_TABLE);
            rs = pr.executeQuery();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("connection error");
            return false;
        }
        finally
        {
            closePrRsAndConnection(pr, rs, connection);
            System.out.println("connection is successful");

        }
        return true;
    }

    static int getLastId(final String TABLE)
    {
        int lastId = -1;
        PreparedStatement pr = null;
        ResultSet rs = null;

        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("SELECT _id " +
                    " FROM " + TABLE);
            rs = pr.executeQuery();
            while (rs.next())
            {
                lastId = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }
        return lastId;
    }

    static ArrayList<Person> getPersonsList()
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        ArrayList<Person> personsList = new ArrayList<>();

        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("SELECT _id, name, second_name, patronymic, " +
                    "address, phone, workStudPlace, datingsNature, businessQuality, position FROM " +
                    PERSONALDATA_TABLE +
                    " ORDER BY name");
            rs = pr.executeQuery();
            while (rs.next())
            {
                Person person = new Person();
                person.set_id(rs.getInt(1));
                person.set_name(rs.getString(2));
                person.set_secondName(rs.getString(3));
                person.set_patronymic(rs.getString(4));
                person.set_address(rs.getString(5));
                person.set_phone(rs.getString(6));
                person.set_workStudPlace(rs.getString(7));
                person.set_datingsNature(rs.getString(8));
                person.set_businessQuality(rs.getString(9));
                person.set_position(rs.getInt(10));
                personsList.add(person);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }

        return personsList;
    }

    static boolean addPosition(String position)
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("INSERT INTO " +
                    POSITONS_TABLE +
                    "(position) " +
                    "VALUES (?)");
            pr.setString(1,position);
            pr.execute();
        }
        catch (SQLException e)
        {
            System.out.println("Ошибка SQL");
            e.printStackTrace();
            return false;
        } catch (Exception ex){
            System.out.println("Ошибка соединения");
            return false;
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }
        return true;
    }

    static ArrayList<Position> getPositionsList()
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        ArrayList<Position> positionsList = new ArrayList<>();

        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("SELECT _id, position " +
                    " FROM " + POSITONS_TABLE);
            rs = pr.executeQuery();
            while (rs.next())
            {
                Position position = new Position();
                position.set_id(rs.getInt(1));
                position.set_position(rs.getString(2));
                positionsList.add(position);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }

        return positionsList;
    }

    static boolean addPerson(Person person)
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("INSERT INTO " +
                    PERSONALDATA_TABLE +
                    "(name, second_name, patronymic, address, " +
                    "phone, workStudPlace, datingsNature, businessQuality, position) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)");
            pr.setString(1, person.get_name());
            pr.setString(2, person.get_secondName());
            pr.setString(3, person.get_patronymic());
            pr.setString(4, person.get_address());
            pr.setString(5, person.get_phone());
            pr.setString(6, person.get_workStudPlace());
            pr.setString(7, person.get_datingsNature());
            pr.setString(8, person.get_businessQuality());
            pr.setInt(9, person.get_position());

            pr.execute();
        }
        catch (SQLException e)
        {
            System.out.println("Ошибка SQL");
            e.printStackTrace();
            return false;
        } catch (Exception ex){
            System.out.println("Ошибка соединения");
            return false;
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }
        return true;
    }

    static boolean editPerson(Person person)
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("UPDATE " +
                    PERSONALDATA_TABLE +
                    " SET name=?, second_name=?, patronymic=?, address=?, " +
                    "phone=?, workStudPlace=?, datingsNature=?, businessQuality=?, " +
                    "position=?" +
                    " WHERE _id = "+ person.get_id());
            pr.setString(1, person.get_name());
            pr.setString(2, person.get_secondName());
            pr.setString(3, person.get_patronymic());
            pr.setString(4, person.get_address());
            pr.setString(5, person.get_phone());
            pr.setString(6, person.get_workStudPlace());
            pr.setString(7, person.get_datingsNature());
            pr.setString(8, person.get_businessQuality());
            pr.setInt(9, person.get_position());
            pr.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Ошибка SQL");
            e.printStackTrace();
            return false;
        } catch (Exception ex){
            System.out.println("Ошибка соединения");
            return false;
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }

        return true;
    }

    static boolean editPosition(Position position)
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("UPDATE " +
                    POSITONS_TABLE +
                    " SET position=? " +
                    " WHERE _id = "+ position.get_id());
            pr.setString(1, position.get_position());
            pr.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Ошибка SQL");
            e.printStackTrace();
            return false;
        } catch (Exception ex){
            System.out.println("Ошибка соединения");
            return false;
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }

        return true;
    }

    static boolean removeObject(int id, final String TABLE)
    {
        PreparedStatement pr = null;
        ResultSet rs = null;
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(DB_URL);
            pr = connection.prepareStatement("DELETE FROM " +
                    TABLE +
                    " WHERE _id = " + id);
            pr.executeUpdate();
        } catch (SQLException e)
        {
            System.out.println("Ошибка SQL");
            e.printStackTrace();
            return false;
        } catch (Exception ex)
        {
            System.out.println("Ошибка соединения");
            return false;
        } finally
        {
            closePrRsAndConnection(pr, rs, connection);
        }

        return true;
    }
}
