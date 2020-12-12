package FileManipulations;

import Attachment.Controller;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;

public class FileTreeCell extends javafx.scene.control.TreeCell<AddingFile> {
    private static FileTreeItem itemToMove = null;

    private static Boolean isCopy = null;

    private MenuItem openFileItem = new MenuItem("Open");
    private MenuItem newFolderItem = new MenuItem("New Folder");
    private MenuItem newFileItem = new MenuItem("New File");
    private MenuItem copyItem = new MenuItem("Copy");
    private MenuItem pasteItem = new MenuItem("Paste");
    private MenuItem cutItem = new MenuItem("Cut");
    private MenuItem deleteItem = new MenuItem("Delete");

    private ContextMenu contextMenu = new ContextMenu();//menu with copy, delete, cut, paste, newFolder
    private TextField textField;


    public FileTreeCell() {
        super();
        newFolderItem.setOnAction(t -> createNewFolder());
        newFileItem.setOnAction(event -> createNewFile());
        copyItem.setOnAction(event -> setupForMovingFile(true));
        cutItem.setOnAction(event -> setupForMovingFile(false));
        deleteItem.setOnAction(event -> deleteFile());
        pasteItem.setOnAction(event ->
        {
            try {
                moveFile();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        openFileItem.setOnAction(event -> {
            try {
                openFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        contextMenu.getItems().addAll(copyItem, cutItem, deleteItem);
    }

    private void moveFile() throws MalformedURLException {
        AddingFile fileToMove = itemToMove.getValue();
        AddingFile folderToMove = getTreeItem().getValue();
        AddingFile destinationFile = FileChanges.getResultOfMovement(fileToMove, folderToMove);

        if (destinationFile.isExist()) {
            Character answer = Events.choiceIfExistsFile();
            switch (answer) {
                case 'p' -> {
                    try {
                        destinationFile.delete();
                        FileTreeItem item = (FileTreeItem) getTreeItem();
                        item.removeChildItem(destinationFile);
                    } catch (IOException e) {
                        error(e, "replacing");
                        return;
                    }
                }
                case 'n' -> {
                    destinationFile = FileChanges.getFileWithSuffix(destinationFile.getAbsolutePath());
                }
                default -> {
                    return;
                }
            }
        }

        if (isCopy) {
            try {
                fileToMove.copy(destinationFile);
            } catch (IOException e) {
                error(e, "moving");
                return;
            }
        } else {
            itemToMove.removeThisFromParent();
            try {
                fileToMove.cut(destinationFile);
            } catch (IOException e) {
                error(e, "moving");
                return;
            }
            itemToMove = null;
            isCopy = null;
        }

        FileTreeItem item = (FileTreeItem) getTreeItem();
        item.addNewChild(destinationFile);
        updateItem();
    }

    private void error(IOException e, String operation) {
        Events.errorDialog("Error with " + operation + " file");
        e.printStackTrace();
    }

    private void deleteFile() {
        AddingFile fileToRemove = getItem();

        if (itemToMove != null && fileToRemove.isEqualOrParent(itemToMove.getValue())) {
            itemToMove = null;
            isCopy = null;
        }

        try {
            if (!fileToRemove.isFile()) {
                if (fileToRemove.getFile().getAbsolutePath().charAt(0) == 'D') {
                    if (Controller.D_Folders.containsKey(fileToRemove.getFile().getName())) {
                        Controller.D_Folders.remove(fileToRemove.getFile().getName());
                    }
                } else {
                    if (Controller.C_Folders.containsKey(fileToRemove.getFile().getName())) {
                        Controller.C_Folders.remove(fileToRemove.getFile().getName());
                    }
                }
            }
            fileToRemove.delete();
            FileTreeItem item = (FileTreeItem) getTreeItem();
            item.removeThisFromParent();
        } catch (IOException e) {
            error(e, "deleting");
        }
    }

    private void openFile() throws Exception {
        AddingFile currentFile = getTreeItem().getValue();
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            desktop.open(currentFile.getFile());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void createNewFolder() {
        AddingFile currentFolder = getTreeItem().getValue();
        try {
            AddingFile newFolder = currentFolder.newFolder();
            FileTreeItem treeItem = (FileTreeItem) getTreeItem();
            treeItem.addNewChild(newFolder);
            treeItem.setExpanded(true);
        } catch (IOException ignored) {
        }
    }

    private void createNewFile() {
        AddingFile currentFolder = getTreeItem().getValue();
        try {
            AddingFile newFile = currentFolder.newFile();
            FileTreeItem treeItem = (FileTreeItem) getTreeItem();
            treeItem.addNewChild(newFile);
            treeItem.setExpanded(true);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        } else {
            textField.setText(getText());
        }

        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        updateItem(getItem(), isEmpty());
    }


    private void commitEdit(String newValue) {
        AddingFile fileExtension = getItem();
        if (newValue == null || newValue.isEmpty()) {
            cancelEdit();
        } else {
            AddingFile destinationFile = FileChanges.resultOfRename(fileExtension, newValue);
            if (destinationFile.isExist()) {
                Character answer = Events.choiceIfExistsFile();
                switch (answer) {
                    case 'p' -> {
                        try {
                            destinationFile.delete();
                        } catch (IOException e) {
                            error(e, "replacing");
                            return;
                        }
                    }
                    case 'n' -> {
                        destinationFile = FileChanges.getFileWithSuffix(destinationFile.getAbsolutePath());
                    }
                    default -> {
                        return;
                    }
                }
            }

            try {
                String previousName = fileExtension.getName();
                fileExtension.rename(destinationFile);
                updateItem();
                super.commitEdit(fileExtension);
            } catch (IOException e) {
                error(e, "renaming");
            }

        }

    }

    @Override
    protected void updateItem(AddingFile item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getTreeItem().getValue().toString());
            setGraphic(getTreeItem().getGraphic());
            setupMenu();
        }
    }


    private void setupMenu() {
        File file = getTreeItem().getValue().getFile();
        ObservableList<MenuItem> menuItems = contextMenu.getItems();

        if (!file.isFile()) {
            if (menuItems.size() == 4) {
                menuItems.remove(openFileItem);
            }
            if (menuItems.size() == 3) {
                menuItems.add(newFolderItem);
                menuItems.add(newFileItem);
            }

            if (menuItems.size() == 5 || menuItems.size() == 6) {
                menuItems.removeAll(pasteItem);
            }

            if (itemToMove != null) {
                pasteItem.setText("Paste (" + itemToMove.getValue().toString() + ")");
                menuItems.add(pasteItem);
            }

        } else {
            if (menuItems.size() < 4) {
                menuItems.add(openFileItem);
            }
            if (menuItems.size() > 4) {
                menuItems.removeAll(pasteItem, newFolderItem, newFileItem);
            }
        }

        setContextMenu(contextMenu);
    }

    private void updateScreen() {
        updateTreeView(getTreeView());
    }

    private void updateItem() {
        updateTreeItem(getTreeItem());
    }

    private void setupForMovingFile(Boolean isCopy) {
        FileTreeCell.isCopy = isCopy;
        itemToMove = (FileTreeItem) getTreeItem();
        updateScreen();
    }

    private void createTextField() {
        textField = new TextField(getText());
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
}
