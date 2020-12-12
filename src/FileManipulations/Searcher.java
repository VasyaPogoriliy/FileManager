package FileManipulations;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Searcher {
    private FileTreeItem fileTreeItem;
    private String limitation;

    public Searcher(FileTreeItem fileTreeItem, String limitation) {
        this.limitation = limitation;
        this.fileTreeItem = fileTreeItem;
    }

    public List<FileTreeItem> search() {
        LinkedList<FileTreeItem> list = new LinkedList<>();
        search(fileTreeItem, list);
        return list;
    }

    private void search(FileTreeItem item, LinkedList<FileTreeItem> list) {
        if (item.isLeaf()) {
            if (checkLimitation(item.getValue().getFile())) {
                list.add(item);
            }
        } else {
            if (item.getChildren() != null) {
                for (TreeItem<AddingFile> item1 : item.getChildren()) {
                    FileTreeItem fileTreeItem = (FileTreeItem) item1;
                    search(fileTreeItem, list);
                }
            }
        }
    }

    private boolean checkLimitation(File file) {
        if (limitation.equals(""))
            return true;
        if (limitation.contains(file.getName()) || file.getName().contains(limitation))
            return true;
        else {
            if (limitation.substring(0, 1).equals(".")) {
                if (FileChanges.getFileExtension(file).equals(limitation.substring(1))) {
                    return true;
                }
            }
        }
        return false;
    }
}
