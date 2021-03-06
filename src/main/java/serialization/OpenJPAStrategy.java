package serialization;

import interfaces.Playlist;
import interfaces.SerializableStrategy;
import interfaces.Song;
import model.Model;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import view.ShowError;

import javax.persistence.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


public class OpenJPAStrategy implements SerializableStrategy {
    Model model;

    public String toString(){
        return "OpenJPA";
    }

    @Override
    public void openWritableLibrary() throws IOException {

    }

    @Override
    public void openReadableLibrary() throws IOException {

    }

    @Override
    public void openWritablePlaylist() throws IOException {

    }

    @Override
    public void openReadablePlaylist() throws IOException {

    }

    @Override
    public void writeSong(Song s) throws IOException {

    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        return null;
    }

    private static void createLibrary(Connection con) {

        try (PreparedStatement pstmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS Library (" +
                "id integer, " +
                "title text, " +
                "album text, " +
                "interpret text, " +
                "path text);");) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement pstmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS Library (" +
                "id integer, " +
                "title text, " +
                "album text, " +
                "interpret text, " +
                "path text);");PreparedStatement drop = con.prepareStatement("DELETE FROM Library")) {
            drop.executeUpdate();
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeLibrary(Playlist p) throws IOException {
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:musicplayer.db")) {
            createLibrary(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //EntityManagerFactory fac = getWithoutConfig();
        EntityManagerFactory fac = getWithConfig();
        EntityManager e = fac.createEntityManager();
        EntityTransaction t = e.getTransaction();
        t.begin();
        for(Song s : p){
            e.persist(s);
        }
        t.commit();

        e.close();
        fac.close();
    }

    @Override
    public Playlist readLibrary() throws IOException, ClassNotFoundException {
        File f = new File("musicplayer.db");
        if(!f.exists()) {
            DatenbankEmptyError();
            return null;
        }
        Playlist returnLib = new model.Playlist();
        //EntityManagerFactory fac = getWithoutConfig();
        EntityManagerFactory fac = getWithConfig();
        EntityManager e = fac.createEntityManager();
        EntityTransaction t = e.getTransaction();

        t.begin();

        for(Object o : e.createQuery("SELECT x FROM Song x").getResultList()){
            Song s = (model.Song) o;
            returnLib.addSong(s);
        }
        System.out.println(returnLib);

        t.commit();

        e.close();
        fac.close();
        return returnLib;

    }
    private void DatenbankEmptyError(){
        ShowError.infoBox("Datenbank muss vorher gefüllt werden.", "Datenbank ist leer.");
    }
    @Override
    public void writePlaylist(Playlist p) throws IOException {

    }

    @Override
    public Playlist readPlaylist() throws IOException, ClassNotFoundException {

        return null;
    }



    @Override
    public void load(Model model) {
        this.model = model;
        try {
            Playlist p = readLibrary();
            if(p != null){
                model.getLibrary().clearPlaylist();
                model.getLibrary().addAll(p.getList());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Model model) {

        try {
            writeLibrary(model.getLibrary());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWritableLibrary() {

    }

    @Override
    public void closeReadableLibrary() {

    }

    @Override
    public void closeWritablePlaylist() {

    }

    @Override
    public void closeReadablePlaylist() {

    }

    private  static EntityManagerFactory getWithConfig() {
        return Persistence.createEntityManagerFactory("openjpa");
         }

    public static EntityManagerFactory getWithoutConfig() {

        Map<String, String> map = new HashMap<String, String>();

        map.put("openjpa.ConnectionURL","jdbc:sqlite:musicplayer.db");
        map.put("openjpa.ConnectionDriverName", "org.sqlite.JDBC");
        map.put("openjpa.RuntimeUnenhancedClasses", "supported");
        map.put("openjpa.jdbc.SynchronizeMappings", "false");

        // find all classes to registrate them
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.add(model.Song.class);

        if (!types.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            for (Class<?> c : types) {
                if (buf.length() > 0)
                    buf.append(";");
                buf.append(c.getName());
            }
            // <class>Pizza</class>
            map.put("openjpa.MetaDataFactory", "jpa(Types=" + buf.toString()+ ")");
        }

        return OpenJPAPersistence.getEntityManagerFactory(map);

    }
}
