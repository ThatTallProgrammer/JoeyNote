import controller.ActionManager;
import controller.DBManager;
import controller.LocationManager;
import controller.TextEditorManager;
import model.Note;
import model.Topic;

import javax.swing.*;
import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        DBManager db = DBManager.INSTANCE;
        db.retrieveLocation();
        ActionManager.INSTANCE.routeAction(args);


//        List<Topic> topics = db.retrieveAllTopics();

//        for(Topic topic : topics) System.out.println(topic);

//        List<Topic> topics = db.retrieveAllTopics();
//
//        for(Topic topic : topics) System.out.println(topic);

//        TextEditorManager noteManager = TextEditorManager.INSTANCE;
//        LocationManager location = LocationManager.INSTANCE;
//        ActionManager actionManager = ActionManager.INSTANCE;


//        Path joeyNote = noteManager.createNewNote();

//        Topic test = new Topic("My Badass Topic");

//        db.creteNewTopic(test);

//        TopicThread thread = new TopicThread("Test is best");

//        db.createNewThread(thread);

//        Note note = new Note("New Title", "This is a note");
//        db.createNewNote(note);
//        System.out.println(LocationManager.INSTANCE.getTopicID());
//        System.out.println(LocationManager.INSTANCE.getThreadID());
//        System.out.println(LocationManager.INSTANCE.getNoteID());

    }

}
