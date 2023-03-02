package app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FoobarFile {
    private String content;
    private List<FoobarFileMemento> history = new ArrayList<>();


    private final File fileObj;
    private static final String FILE_DIR = "src/files";

    public FoobarFile(String fileName) throws IOException {
        this.content = "";

        var pathDir = Paths.get(FILE_DIR);
        if (!pathDir.toFile().exists()) {
            Files.createDirectories(pathDir);
        }

        this.fileObj = new File(FILE_DIR, fileName);
        if (!this.fileObj.exists()) {
            var valid = this.fileObj.createNewFile();
            if (!valid) {
                throw new IOException("Can't create file");
            }
        }
    }

    public void setContent(String content) {
        FoobarFileMemento memento = createMemento();
        history.add(memento);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void save() {
        try (var fileWriter = new FileWriter(this.fileObj.toString());
             var bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(this.content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void undo() {
        if (history.size() > 0) {
            var memento = history.remove(history.size() - 1);
            restoreMemento(memento);
        }
    }

    public FoobarFileMemento createMemento() {
        return new FoobarFileMemento(content);
    }

    private void restoreMemento(FoobarFileMemento memento) {
        this.content = memento.getContent();
    }
}
