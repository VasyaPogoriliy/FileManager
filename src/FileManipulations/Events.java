package FileManipulations;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Events {
    public static void errorDialog(String headMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(headMessage);
        alert.setHeaderText("Error");
        alert.setContentText("Maybe, you haven't permissions to do that action");

        alert.showAndWait();
    }

    public static Character choiceIfExistsFile()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Problems with renaming/moving file");
        alert.setHeaderText("There is equal name");
        alert.setContentText("Choose your option");

        ButtonType buttonTypeOne = new ButtonType("Replace");
        ButtonType buttonTypeTwo = new ButtonType("Rename");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne)
        {
            return 'p';
        }
        else if (result.get() == buttonTypeTwo)
        {
            return 'n';
        }
        else
        {
            return 'c';
        }
    }
}
