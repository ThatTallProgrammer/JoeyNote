package controller;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;
import org.apache.ibatis.jdbc.ScriptRunner;
import utils.FileSystemUtils;

public enum DBManager {

    INSTANCE;

    private String homeDir = "";
    private String dbDir = "database/";
    private String dbFile = "JoeyNote.db";
    private String setupFile = "setup.sqlite";
    private String url = "jdbc:sqlite:";

    private Connection conn = null;

    private DBManager() {

        this.init();

    }

    private void init() {

        this.homeDir = FileSystemUtils.getExecutionDirectory();
        this.dbDir = Paths.get(this.homeDir, this.dbDir).toString();
        this.dbFile = Paths.get(this.dbDir, this.dbFile).toString();
        this.setupFile = Paths.get(this.homeDir, this.setupFile).toString();
        this.url = this.url + this.dbFile;

        File dir = new File(this.dbDir);
        if(!dir.exists())
            dir.mkdirs();

        File db = new File(this.dbFile);
        if(!db.exists()) {
            this.runScript(this.setupFile);
            this.retrieveLocation();
            this.createNewTopic(new Topic("ROOT"));
            this.createNewThread(new TopicThread("ROOT"));
        }

        this.retrieveLocation();

    }

    private void runScript(String scriptPath) {

        this.connect();

        ScriptRunner scriptRunner = new ScriptRunner(this.conn);

        Reader reader = null;
        try {
            reader = new BufferedReader(
                    new FileReader(scriptPath)
            );
            scriptRunner.runScript(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    private void connect() {


        try {
            conn = DriverManager.getConnection(this.url);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void closeConnection() {

        if (conn != null) {

            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public void retrieveLocation() {

        this.connect();

        LocationManager location = LocationManager.INSTANCE;
        try {

            String sql = "SELECT topicID, threadID, noteID FROM Location WHERE locationID = 0";

            Statement stmt = this.conn.createStatement();

            ResultSet resultSet = stmt.executeQuery(sql);

            location.setTopicID(resultSet.getInt("topicID"));
            location.setThreadID(resultSet.getInt("threadID"));
            location.setNoteID(resultSet.getInt("noteID"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            this.closeConnection();
        }

    }

    public void updateLocation() {

        this.connect();

        LocationManager location = LocationManager.INSTANCE;

        String sql = "UPDATE Location SET topicID = ?, threadID = ?, noteID = ? WHERE locationID = 0";

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setInt(1, location.getTopicID());
            pstmt.setInt(2, location.getThreadID());
            pstmt.setInt(3, location.getNoteID());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

    }

    private boolean topicTitleTaken(Topic topic) throws SQLException{

        String sql = "SELECT COUNT(*) FROM Topic WHERE title = ?;";

        PreparedStatement pstmt = this.conn.prepareStatement(sql);
        pstmt.setString(1, topic.getTitle().trim());
        ResultSet resultSet = pstmt.executeQuery();

        return resultSet.getInt(1) != 0;
    }

    public void createNewTopic(Topic topic) {

        this.connect();

        String sql = "INSERT INTO Topic (title, timeCreated, lastUpdated) Values (?, ?, ?);";

        try {
            if(topicTitleTaken(topic)) {
                System.out.printf(
                        "Couldn't create topic because topic '%s' already exists\n",
                        topic.getTitle()
                );
                return;
            }

            PreparedStatement pstmt = this.conn.prepareStatement(sql);

            pstmt.setString(1, topic.getTitle().trim());
            pstmt.setTimestamp(2, Timestamp.valueOf(topic.getCreatedAt()));
            pstmt.setTimestamp(3, Timestamp.valueOf(topic.getUpdatedAt()));

            pstmt.execute();

            System.out.printf(
                    "Topic '%s' successfully created\n",
                    topic.getTitle()
            );
        } catch (SQLException e) {
            System.out.printf(
                    "A SQLException occurred while attempting to create topic '%s'\n",
                    topic.getTitle()
            );
        } finally {
            this.closeConnection();
        }

    }

    private boolean threadTitleTaken(TopicThread thread) throws SQLException{

        LocationManager location = LocationManager.INSTANCE;

        String sql = "SELECT COUNT(*) FROM Thread WHERE title = ? AND topicID = ?;";

        PreparedStatement pstmt = this.conn.prepareStatement(sql);
        pstmt.setString(1, thread.getTitle().trim());
        pstmt.setInt(2, location.getTopicID());
        ResultSet resultSet = pstmt.executeQuery();

        return resultSet.getInt(1) != 0;
    }

    public void createNewThread(TopicThread thread) {

        this.connect();

        LocationManager location = LocationManager.INSTANCE;

        String sql = "INSERT INTO Thread (topicID, title, timeCreated, lastUpdated) Values (?, ?, ?, ?);";

        try {
            if(threadTitleTaken(thread)) {
                System.out.printf(
                        "Couldn't create thread because thread '%s' already exists in this topic\n",
                        thread.getTitle()
                );
                return;
            }

            PreparedStatement pstmt = this.conn.prepareStatement(sql);

            pstmt.setInt(1, location.getTopicID());
            pstmt.setString(2, thread.getTitle().trim());
            pstmt.setTimestamp(3, Timestamp.valueOf(thread.getCreatedAt()));
            pstmt.setTimestamp(4, Timestamp.valueOf(thread.getUpdatedAt()));

            pstmt.execute();

            System.out.printf(
                    "Thread '%s' successfully created\n",
                    thread.getTitle()
            );
        } catch (SQLException e) {
            System.out.printf(
                    "A SQLException occurred while attempting to create thread '%s'\n",
                    thread.getTitle()
            );
        } finally {
            this.closeConnection();
        }

    }

    public boolean noteTitleTaken(String title) throws SQLException{

        LocationManager location = LocationManager.INSTANCE;

        String sql = "SELECT COUNT(*) FROM Note WHERE title = ? AND threadID = ?;";

        PreparedStatement pstmt = this.conn.prepareStatement(sql);
        pstmt.setString(1, title.trim());
        pstmt.setInt(2, location.getThreadID());
        ResultSet resultSet = pstmt.executeQuery();

        return resultSet.getInt(1) != 0;
    }

    public void createNewNote(Note note) {

        this.connect();

        LocationManager location = LocationManager.INSTANCE;

        String sql = "INSERT INTO Note (threadID, title, directory, body, timeCreated, lastUpdated) " +
                "Values (?, ?, ?, ?, ?, ?);";

        try {

            if(this.noteTitleTaken(note.getTitle())) {
                System.out.printf(
                        "Couldn't create thread because note '%s' already exists in this topic\n",
                        note.getTitle()
                );
                return;
            }

            PreparedStatement pstmt = this.conn.prepareStatement(sql);

            pstmt.setInt(1, location.getThreadID());
            pstmt.setString(2, note.getTitle().trim());
            pstmt.setString(3, note.getDirectory());
            pstmt.setString(4, note.getBody());
            pstmt.setTimestamp(5, Timestamp.valueOf(note.getCreatedAt()));
            pstmt.setTimestamp(6, Timestamp.valueOf(note.getUpdatedAt()));

            pstmt.execute();

            System.out.printf(
                    "Note %ssuccessfully created\n",
                    note.getTitle().equals("") ? "" : "'" + note.getTitle() + "' "
            );
        } catch (SQLException e) {
            System.out.printf(
                    "A SQLException occurred while attempting to create note %s\n",
                    note.getTitle().equals("") ? "" : "'" + note.getTitle() + "'"
            );
        } finally {
            this.closeConnection();
        }

    }

    public Retrievable retrieveSectionFromTitle(String title, String fullyQualifiedTableName) {

        this.connect();

        String sql = "SELECT * FROM " + fullyQualifiedTableName + " WHERE title = ? ";
        switch(fullyQualifiedTableName) {

            case "Topic":
                break;
            case "Thread":
                sql = sql + String.format("AND topicID = %d", LocationManager.INSTANCE.getTopicID());
                break;
            case "Note":
                sql = sql + String.format("AND threadID = %d", LocationManager.INSTANCE.getThreadID());
                break;
            default:
                System.out.println("Invalid table");
                return null;
        }
        sql = sql + ";";

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet resultSet = pstmt.executeQuery();

            List<Map<String, Object>> dbos = this.createDbos(resultSet);

            if(dbos.size() == 0) {
                System.out.println("No Results Returned");
                return null;
            }

            return RetrievableFactory.restore(fullyQualifiedTableName, dbos.get(0));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<Retrievable> retrieveAllAtLocationByTableName(String fullyQualifiedTableName, Integer foreignKey) {

        this.connect();

        String sql = "SELECT * FROM " + fullyQualifiedTableName;
        switch(fullyQualifiedTableName) {

            case "Topic":
                break;
            case "Thread":
                sql = sql + String.format(
                        " WHERE topicID = %d", foreignKey == null ? LocationManager.INSTANCE.getTopicID() : foreignKey
                );
                break;
            case "Note":
                sql = sql + String.format(
                        " WHERE threadID = %d", foreignKey == null ? LocationManager.INSTANCE.getThreadID() : foreignKey
                );
                break;
            default:
                System.out.println("Invalid table");
                return null;
        }
        sql = sql + ";";

        ArrayList<Retrievable> retrievables = null;
        try {

            Statement stmt = this.conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            List<Map<String, Object>> dbos = createDbos(resultSet);
            retrievables = new ArrayList<>();

            for(Map<String, Object> dbo : dbos) {
                retrievables.add(RetrievableFactory.restore(fullyQualifiedTableName, dbo));
//                Topic nextTopic = new Topic(dbo);
//                retrievables.add(nextTopic);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            this.closeConnection();
        }

        return retrievables;

    }

    private List<Map<String, Object>> createDbos(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> dbos = new ArrayList<>();

        while(resultSet.next()){

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Map<String, Object> dbo = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) dbo.put(rsmd.getColumnName(i), resultSet.getObject(i));

            dbos.add(dbo);

        }

        return dbos;
    }

    //TODO: Abstract DBO creation into a function
    // Implement conditional class that generates sql conditional string
    // Look into generalizing creation methods with reflection
    // Log both no result and more than one result returned from retrieve from title methods

}