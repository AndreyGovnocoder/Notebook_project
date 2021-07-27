import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AboutForm
{
    private Button _okButton;
    private final Font _headFont = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20);
    private final Font _font10 = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 10);
    private final Font _authorFont = Font.font("Arial", FontWeight.LIGHT, FontPosture.REGULAR, 14);
    private final Font _descriptionFont = Font.font("Arial", FontWeight.MEDIUM, FontPosture.ITALIC, 12);
    private final String _nameProg = "Записная книжка";
    private final String _version = ReferencesTexts.VERSION;
    private final String _copyright = "Copyright © 2020 Андрей Гулий\nВсе права защищены";
    private final String _author = "Автор программы:\n " +
            "    студент 3ИСП-1 Андрей Гулий\n ";
    private final String _signature = "ГБПОУ \"Магаданский политехнический техникум\"";
    private final String _description = "Выполнено и разработано по заданию производственной практики:\n " +
            "Экзаменационный билет № 1";

    void showAndWait(Stage primaryStage)
    {
        Stage aboutStage = new Stage();
        StackPane aboutBorderPane = new StackPane();
        Scene aboutScene = new Scene(aboutBorderPane, 490, 300);
        aboutScene.addEventFilter(KeyEvent.KEY_RELEASED, this::bindingKeys);
        StackPane centerStackPane = new StackPane();
        VBox rightVBox = new VBox();
        VBox topVBox = new VBox();
        HBox buttonHBox = new HBox();
        _okButton = new Button("Ок");
        StackPane imageStackPane = new StackPane();
        final Label nameProgLabel = new Label(_nameProg);
        final Label versionLabel = new Label(_version);
        final Label copyRightLabel = new Label(_copyright);
        final Label authorLabel = new Label(_author);
        final Label descriptionLabel = new Label(_description);
        final Label signatureLabel = new Label(_signature);
        ImageView okBtnImageView = new ImageView(MainForm.getIcon("ok_btn_32.png"));
        versionLabel.setFont(_font10);
        copyRightLabel.setFont(_font10);
        copyRightLabel.setPadding(new Insets(-15,0,0,0));
        nameProgLabel.setFont(_headFont);
        descriptionLabel.setFont(_descriptionFont);
        descriptionLabel.setWrapText(true);
        authorLabel.setFont(_authorFont);
        topVBox.setAlignment(Pos.TOP_CENTER);
        topVBox.setPadding(new Insets(10));
        topVBox.getChildren().add(nameProgLabel);

        imageStackPane.setAlignment(Pos.CENTER_LEFT);
        imageStackPane.setPadding(new Insets(10,0,0,0));
        imageStackPane.setStyle("-fx-background-color: white");
        imageStackPane.getChildren().add(getLogoGoose());

        rightVBox.setAlignment(Pos.TOP_LEFT);
        rightVBox.setSpacing(15);
        rightVBox.setPadding(new Insets(40,15,0,190));
        rightVBox.getChildren().addAll(
                versionLabel,
                copyRightLabel,
                authorLabel,
                descriptionLabel,
                signatureLabel);

        centerStackPane.getChildren().addAll(imageStackPane, topVBox, rightVBox);
        centerStackPane.setStyle("-fx-background-color: white");

        _okButton.setOnAction(event -> aboutStage.close());
        _okButton.setGraphic(okBtnImageView);

        buttonHBox.setPadding(new Insets(10));
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.getChildren().addAll(_okButton);

        aboutBorderPane.getChildren().addAll(centerStackPane, _okButton);
        StackPane.setAlignment(_okButton, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(centerStackPane, Pos.TOP_CENTER);
        StackPane.setMargin(_okButton, new Insets(10));

        aboutStage.setScene(aboutScene);
        aboutStage.setTitle("Записная книжка");
        aboutStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        aboutStage.initModality(Modality.WINDOW_MODAL);
        aboutStage.initOwner(primaryStage);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }

    private void bindingKeys(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ESCAPE)
            _okButton.fire();
    }

    private ImageView getLogoGoose()
    {
        Image image = null;
        try
        {
            FileInputStream fs = new FileInputStream(Database.path + "\\images\\logo\\logo_goose.png");
            image = new Image(fs);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        return imageView;
    }
}
