package model;

import java.util.Map;

public abstract class RetrievableFactory {
    public static Retrievable restore(String fullyQualifiedTableName, Map<String, Object> dbo) {

        switch(fullyQualifiedTableName) {
            case "Topic":
                return new Topic(dbo);
            case "Thread":
                return new TopicThread(dbo);
            case "Note":
                return new Note(dbo);
            default:
                return null;
        }

    }
}
