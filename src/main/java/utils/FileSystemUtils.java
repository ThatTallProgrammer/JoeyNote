package utils;

import java.io.File;
import java.net.URISyntaxException;

public abstract class FileSystemUtils {

    public static String getExecutionDirectory() {

        try {

            File mainClass = new File(
                    FileSystemUtils.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );

            return mainClass.getParentFile().getPath();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

}
