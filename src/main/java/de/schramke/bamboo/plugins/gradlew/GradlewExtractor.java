package de.schramke.bamboo.plugins.gradlew;

import com.atlassian.util.concurrent.LazyReference;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class GradlewExtractor {
    private static final Logger log = Logger.getLogger(GradlewExtractor.class);
    // ------------------------------------------------------------------------------------------------------- Constants
    private static File workingDirectory;

    private static final LazyReference<String> GRADLEW_HOME = new LazyReference<String>() {
        @Override
        protected String create() throws Exception {
            File file = new File(workingDirectory, "gradle/wrapper/");
            file.mkdirs();
            return file.getAbsolutePath();
        }
    };

    private static final LazyReference<File> DEVENV_RUNNER_REFERENCE = new LazyReference<File>() {
        @Override
        protected File create() throws Exception {
            extract("/gradlew/gradle/wrapper/gradle-wrapper.properties", new File(GRADLEW_HOME.get(), "gradle-wrapper.properties"));
            extract("/gradlew/gradle/wrapper/gradle-wrapper.jar", new File(GRADLEW_HOME.get(), "gradle-wrapper.jar"));

            File dstFile = null;
            if(SystemUtils.IS_OS_WINDOWS){
                dstFile = new File(workingDirectory, "gradlew.bat");
                extract("/gradlew/gradlew.bat", dstFile);
            }else{
                dstFile = new File(workingDirectory, "gradlew");
                extract("/gradlew/gradlew", dstFile, true);
            }

            return dstFile;
        }
    };

    // ------------------------------------------------------------------------------------------------- Type Properties
    // ---------------------------------------------------------------------------------------------------- Dependencies
    // ---------------------------------------------------------------------------------------------------- Constructors

    private GradlewExtractor() {
    }

    // ----------------------------------------------------------------------------------------------- Interface Methods
    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods

    /**
     * Get the path to gradlw batch file
     *
     * @return path
     */
    public static String getGradlewPath(String workingDirectory) {

        GradlewExtractor.workingDirectory = new File(workingDirectory);
        return DEVENV_RUNNER_REFERENCE.get().getAbsolutePath();
    }

    /**
     * Get the gradlw batch file
     *
     * @return path
     */
    public static File getGradlew(String workingDirectory) {

        GradlewExtractor.workingDirectory = new File(workingDirectory);
        return DEVENV_RUNNER_REFERENCE.get();
    }

    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators

    private static String extract(@NotNull String resourceName, @NotNull File dstFile) throws IOException {
        return extract(resourceName, dstFile, false);
    }

    private static String extract(@NotNull String resourceName, @NotNull File dstFile, boolean executable) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        if(dstFile.exists()){
            return dstFile.getAbsolutePath();
        }

        try {
            inputStream = GradlewExtractor.class.getResourceAsStream(resourceName);
            if (inputStream == null) {
                throw new IllegalStateException("Could not find '" + resourceName + "' on classpath");
            }

            outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);

            if (executable) {
                dstFile.setExecutable(true);
            }

            return dstFile.getAbsolutePath();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }
}
