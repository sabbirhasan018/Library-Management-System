// Modified on 2026-11-08 to initialize local account files and validate remembered sessions.
package Main;

import Frame.LoginFrame;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Init {

    public Init() {
        Path dataDirectory = Path.of("src", "data");
        String line;
        try {
            Files.createDirectories(dataDirectory);
            createIfMissing(dataDirectory.resolve("student.txt"));
            createIfMissing(dataDirectory.resolve("librarian.txt"));
            Path rememberFile = dataDirectory.resolve("remember.txt");
            createIfMissing(rememberFile);
            try (var reader = Files.newBufferedReader(rememberFile)) {
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize local library data.", e);
        }

        if (line != null) {
            String[] data = line.split(",", -1);
            if (data.length == 3
                    && ("Student".equals(data[0]) || "Librarian".equals(data[0]))
                    && !data[1].isBlank() && !data[2].isBlank()) {
                new LoginFrame(data[0], data[1], data[2]);
                return;
            }
        }
        new LoginFrame();
    }

    private static void createIfMissing(Path file) throws IOException {
        try {
            Files.createFile(file);
        } catch (FileAlreadyExistsException e) {
            if (!Files.isRegularFile(file)) {
                throw e;
            }
        }
    }
}
