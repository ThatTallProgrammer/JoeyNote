import controller.ActionManager;
import controller.DBManager;
import controller.LocationManager;
import controller.TextEditorManager;
import model.Note;
import model.Topic;
import utils.FileSystemUtils;

import javax.swing.*;
import javax.xml.stream.Location;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {



    public static void main(String[] args) {

        ActionManager.INSTANCE.routeAction(args);

    }

}
