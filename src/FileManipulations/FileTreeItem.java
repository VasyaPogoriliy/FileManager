package FileManipulations;

import Attachment.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

public class FileTreeItem extends TreeItem<AddingFile> {
    private Boolean hasLoadedChildren;

    public FileTreeItem(AddingFile file) {
        super(file);
        hasLoadedChildren = false;
        if (!isLeaf()) {
            if (file.getAbsolutePath().charAt(0) == 'D') {
                Controller.D_Folders.put(file.getName(), file.getAbsolutePath());
            } else {
                Controller.C_Folders.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    public FileTreeItem(AddingFile file, Node graphic) {
        super(file, graphic);
        hasLoadedChildren = false;
        if (!isLeaf()) {
            if (file.getAbsolutePath().charAt(0) == 'D') {
                Controller.D_Folders.put(file.getName(), file.getAbsolutePath());
            } else {
                Controller.C_Folders.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    @Override
    public boolean isLeaf() {
        return getValue().isFile();
    }

    @Override
    public ObservableList<TreeItem<AddingFile>> getChildren() {
        if (!hasLoadedChildren) {
            hasLoadedChildren = true;
            try {
                addChildes();
                setGraphic(Icons.openFolder());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return super.getChildren();
    }

    public void addNewChild(AddingFile file) throws MalformedURLException {
        if (hasLoadedChildren) {
            FileTreeItem newFolderItem = createItem(file);
            super.getChildren().addAll(newFolderItem);
        }
    }

    public void removeChildItem(AddingFile file) {
        TreeItem<AddingFile> itemToRemove = null;
        for (TreeItem<AddingFile> fileExtensionTreeItem : getChildren()) {
            String fileName = fileExtensionTreeItem.getValue().getFile().getName();
            if (file.getName().equals(fileName)) {
                itemToRemove = fileExtensionTreeItem;
                break;
            }
        }

        if (itemToRemove != null) {
            getChildren().removeAll(itemToRemove);
        }
    }

    public void removeThisFromParent() {
        getParent().getChildren().removeAll(this);
    }

    private void addChildes() throws MalformedURLException {
        if (!isLeaf()) {
            File[] childs = getValue().getFile().listFiles();
            ObservableList<TreeItem<AddingFile>> childList = FXCollections.observableArrayList();

            if (childs != null) {
                for (File child : childs) {
                    childList.add(createItem(new AddingFile(child)));
                }
            }
            super.getChildren().addAll(childList);
        }
    }

    private FileTreeItem createItem(AddingFile child) throws MalformedURLException {
        ImageView image;
        if (!child.isFile()) {
            image = Icons.openFolder();
        } else {
            image = Icons.file();
        }

        FileTreeItem item = new FileTreeItem(child, image);

        item.expandedProperty().addListener((observable, oldValue, newValue) ->
        {

            if (newValue) {
                try {
                    item.setGraphic(Icons.openFolder());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    item.setGraphic(Icons.openFolder());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        return item;
    }
}