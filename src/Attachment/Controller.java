package Attachment;

import FileManipulations.AddingFile;
import FileManipulations.FileTreeCell;
import FileManipulations.FileTreeItem;
import FileManipulations.Searcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.util.HashMap;


public class Controller {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ScrollPane C_FileScrollPane;

    @FXML
    private ScrollPane D_FileScrollPane;

    @FXML
    private Button C_SearchButton;

    @FXML
    private TextField C_SearchTextField;

    @FXML
    private TextField D_SearchTextField;

    @FXML
    private Button D_SearchButton;

    @FXML
    private ScrollPane D_FileSearchScrollPane;

    @FXML
    private MenuButton D_FolderMenu;

    @FXML
    private ScrollPane C_FileSearchScrollPane;

    @FXML
    private MenuButton C_FolderMenu;

    private TreeView<AddingFile> C_FileView;
    private TreeView<AddingFile> D_FileView;
    private FileTreeItem C_FileTreeItem;
    private FileTreeItem D_FileTreeItem;
    public static HashMap<String, String> C_Folders = new HashMap<>();
    public static HashMap<String, String> D_Folders = new HashMap<>();
    private static final int SCROLL_PANE_PREF_WIDTH = 350;
    private static final int VIEW_ITEM_PREF_HEIGHT = 60;

    @FXML
    void initialize() {
        if (C_Folders.size() != 0 || D_Folders.size() != 0) {
            D_Folders.clear();
            C_Folders.clear();
        }

        initializeC();
        initializeD();
        D_SetFolderMenu();
        C_SetFolderMenu();

        C_SearchButton.setOnAction(actionEvent ->
        {
            String C_Folder = C_FolderMenu.getText();
            C_Search(C_Folder);
        });

        D_SearchButton.setOnAction(actionEvent ->
        {
            String D_Folder = D_FolderMenu.getText();
            D_Search(D_Folder);
        });

    }

    private void initializeC() {
        AddingFile C_Root = new AddingFile("C:/");
        C_FileTreeItem = new FileTreeItem(C_Root);
        C_FileView = new TreeView<>(C_FileTreeItem);
        C_FileView.setShowRoot(false);
        C_FileView.setEditable(true);
        C_FileView.setCellFactory(param -> new FileTreeCell());
        C_FileScrollPane.setContent(C_FileView);
        C_FileScrollPane.setFitToHeight(true);
        C_FileScrollPane.setFitToWidth(true);
    }

    private void initializeD() {
        AddingFile D_Root = new AddingFile("D:/");
        D_FileTreeItem = new FileTreeItem(D_Root);
        D_FileView = new TreeView<>(D_FileTreeItem);
        D_FileView.setShowRoot(false);
        D_FileView.setEditable(true);
        D_FileView.setCellFactory(param -> new FileTreeCell());
        D_FileScrollPane.setContent(D_FileView);
        D_FileScrollPane.setFitToHeight(true);
        D_FileScrollPane.setFitToWidth(true);
    }

    private void C_Search(String CFolderToSearch) {
        String limitation = C_SearchTextField.getText();
        search(C_FileSearchScrollPane, new AddingFile(new File(C_Folders.get(CFolderToSearch))), limitation);
    }

    private void D_Search(String DFolderToSearch) {
        String limitation = D_SearchTextField.getText();
        search(D_FileSearchScrollPane, new AddingFile(new File(D_Folders.get(DFolderToSearch))), limitation);
    }

    private void C_SetFolderMenu() {
        for (String folder : C_Folders.keySet()) {
            if (folder.equals(""))
                continue;
            MenuItem item = new MenuItem(folder);
            item.setOnAction(actionEvent -> {
                C_FolderMenu.setText(folder);
            });
            C_FolderMenu.getItems().add(item);
        }
    }

    private void D_SetFolderMenu() {
        for (String folder : D_Folders.keySet()) {
            if (folder.equals(""))
                continue;
            MenuItem item = new MenuItem(folder);
            item.setOnAction(actionEvent -> {
                D_FolderMenu.setText(folder);
            });
            D_FolderMenu.getItems().add(item);
        }
    }

    private void search(ScrollPane fileSearchScrollPane, AddingFile fileRootSearch, String limitation) {
        FileTreeItem fileTreeItemSearch = new FileTreeItem(fileRootSearch);
        Searcher fileSearcher = new Searcher(fileTreeItemSearch, limitation);
        final FlowPane container = new FlowPane();
        container.setPrefWidth(SCROLL_PANE_PREF_WIDTH);

        for (FileTreeItem fileItem : fileSearcher.search()) {
            TreeView<AddingFile> fileSearchView = new TreeView<>(fileItem);
            fileSearchView.setShowRoot(true);
            fileSearchView.setEditable(true);
            fileSearchView.setCellFactory(param -> new FileTreeCell());
            fileSearchView.setPrefWidth(SCROLL_PANE_PREF_WIDTH);
            fileSearchView.setPrefHeight(VIEW_ITEM_PREF_HEIGHT);
            container.getChildren().add(fileSearchView);
        }

        fileSearchScrollPane.setContent(container);
        fileSearchScrollPane.contentProperty();
        fileSearchScrollPane.setFitToHeight(true);
        fileSearchScrollPane.setFitToWidth(true);
    }
}