package controller;

import model.*;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public enum ActionManager {

    INSTANCE;

    private DBManager dbManager;
    private LocationManager locationManager;
    private TextEditorManager textEditorManager;

    private ActionManager() {

        this.dbManager = DBManager.INSTANCE;
        this.locationManager = LocationManager.INSTANCE;
        this.textEditorManager = TextEditorManager.INSTANCE;

    }

    public void routeAction(String[] args) {

        if(args.length == 0) {
            newNote(null);
            return;
        }

        String action = args[0];
        switch(action) {

            case "newtopic":
                this.createNewTopic(null);
                break;
            case "newthread":
                this.createNewThread(null);
                break;
            case "topiclist":
                this.listAllAtLocationByType("Topic");
                break;
            case "threadlist":
                this.listAllAtLocationByType("Thread");
                break;
            case "notelist":
                this.listAllAtLocationByType("Note");
                break;
            case "tree":
                this.printTree();
            default:
                System.out.printf("Unrecognized action: %s\n", action);

        }

        dbManager.updateLocation();

    }

    private String getTitleFromUser() {

        Scanner scanner = new Scanner(System.in);

        String title = "";
        while(title.trim().length() == 0) {
            System.out.printf("Title: ");
            title = scanner.nextLine().trim();
        }

        return title;

    }

    private void createNewTopic(String title) {

        System.out.println("--------------Creating New Topic--------------");
        if(title == null || title.trim().length() == 0)
            title = getTitleFromUser();

        Topic topic = new Topic(title);
        dbManager.createNewTopic(topic);

        topic = (Topic)dbManager.retrieveSectionFromTitle(title, "Topic");
        locationManager.setTopicID(topic.getId());

        createNewThread("CATCH_ALL");

        locationManager.setNoteID(0);
    }

    private void createNewThread(String title) {

        System.out.println("--------------Creating New Thread--------------");
        if(title == null || title.trim().length() == 0)
            title = getTitleFromUser();

        TopicThread thread = new TopicThread(title);
        dbManager.createNewThread(thread);

        thread = (TopicThread)dbManager.retrieveSectionFromTitle(title, "Thread");

        locationManager.setThreadID(thread.getId());
        locationManager.setNoteID(0);

    }

    private void newNote(String title) {
        System.out.println("--------------Creating New Note--------------");
        if(title == null || title.trim().length() == 0)
            title = getTitleFromUser();

        Note note = new Note(title);
        note.setBody(
                textEditorManager.getStringFromEditor(title)
        );

        dbManager.createNewNote(note);
        note = (Note)dbManager.retrieveSectionFromTitle(title, "Note");
        locationManager.setNoteID(note.getId());

    }

    private void listAllAtLocationByType(String fullyQualifiedTableName) {
        List<Retrievable> retrievables = dbManager.retrieveAllAtLocationByTableName(fullyQualifiedTableName, null);
        for(Retrievable retrievable : retrievables) System.out.println(retrievable);
    }

    private void printTree() {
        List<Retrievable> topics = dbManager.retrieveAllAtLocationByTableName("Topic", null);
        for(Retrievable topic : topics) {
            System.out.println(((Topic) topic).getTitle());
            List<Retrievable> threads = dbManager.retrieveAllAtLocationByTableName("Thread", ((Topic) topic).getId());
            for(Retrievable thread : threads) {
                System.out.println("|\t" + ((TopicThread) thread).getTitle());
                List<Retrievable> notes = dbManager.retrieveAllAtLocationByTableName("Note", ((TopicThread) thread).getId());
                for(Retrievable note : notes) {
                    System.out.println("|\t|\t" + ((Note) note).getTitle());
                }
            }
        }
    }

}