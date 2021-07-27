import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PrintCongratulation
{
    private final Person _person;
    private Stage _printCongrStage;
    private Scene _printCongrScene;
    private BorderPane _printCongrBorderPane;
    private HBox _navigationBtnsHBox;
    private StackPane _printStackPane;
    private Button _nextButton;
    private Button _previousButton;
    private Button _printButton;
    private Button _cancelButton;
    private int _page = 1;
    private int _image = 0;
    private String _hint1 = "Выберите макет поздравления";
    private String _hint2 = "Заполните заголовок, текст и подпись поздравления";
    private String _hint3 = "Макет поздравления на печать";
    private ToggleGroup _imagesToggleGroup;
    private Label _hintTopLabel;
    private final ArrayList<ToggleButton> _toggleButtonsList;
    private final ToggleButton _image1 = new ToggleButton();
    private final ToggleButton _image2 = new ToggleButton();
    private final ToggleButton _image3 = new ToggleButton();
    private final ToggleButton _image4 = new ToggleButton();
    private final ToggleButton _image5 = new ToggleButton();
    private final ToggleButton _image6 = new ToggleButton();
    private final TextField _headingTextField = new TextField();
    private final TextArea _congratulationsTextArea = new TabIgnoringTextArea();
    private final TextField _signatureTextField = new TextField();
    private final ComboBox<String> _nameComboBox;
    private final Font _fontForTopAndBottom = Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 14);
    private final Font _headFont = Font.font("Segoe Script", FontWeight.BOLD, FontPosture.REGULAR, 16);
    private final Font _congrFont = Font.font("Segoe Script", FontWeight.NORMAL, FontPosture.REGULAR, 16);
    private final Font _signagureFont = Font.font("Segoe Script", FontWeight.NORMAL, FontPosture.ITALIC, 12);

    PrintCongratulation(Person person)
    {
        this._person = person;
        _image1.setGraphic(getImageView("1.jpg"));
        _image2.setGraphic(getImageView("2.jpg"));
        _image3.setGraphic(getImageView("3.jpg"));
        _image4.setGraphic(getImageView("4.jpg"));
        _image5.setGraphic(getImageView("5.jpg"));
        _image6.setGraphic(getImageView("6.jpg"));
        String fullName = null;
        String name = null;
        String nameSecondName = null;
        String namePatronymic = null;
        String fio = null;

        if(person.get_name()!=null && person.get_secondName() != null)
            nameSecondName = person.get_name() + " " + person.get_secondName();
        if(person.get_name() != null)
            name = person.get_name();
        if(person.get_name() != null && person.get_patronymic() != null && person.get_secondName() != null)
        {
            fullName = person.get_name() + " " + person.get_patronymic() + " " + person.get_secondName();
            fio = person.toString();
        }
        if(person.get_name() != null && person.get_patronymic() != null)
            namePatronymic = person.get_name() + " " + person.get_patronymic();

        _nameComboBox = new ComboBox<>();
        if(fullName != null)
            _nameComboBox.getItems().add(fullName);
        if(name != null)
            _nameComboBox.getItems().add(name);
        if(nameSecondName != null)
            _nameComboBox.getItems().add(nameSecondName);
        if(namePatronymic != null)
            _nameComboBox.getItems().add(namePatronymic);
        if(fio != null)
            _nameComboBox.getItems().add(fio);

        _nameComboBox.setEditable(true);
        _nameComboBox.getEditor().setContextMenu(new ContextMenu());

        if(fullName != null)
            _nameComboBox.setValue(fullName);
        else if(namePatronymic != null)
            _nameComboBox.setValue(namePatronymic);
        else if(nameSecondName != null)
            _nameComboBox.setValue(nameSecondName);
        else if(name != null)
            _nameComboBox.setValue(name);
        else
            _nameComboBox.setValue(person.get_secondName());

        _toggleButtonsList = new ArrayList<>(FXCollections.observableArrayList(
                _image1, _image2, _image3, _image4, _image5, _image6));

        _headingTextField.setContextMenu(new ContextMenu());
        _congratulationsTextArea.setContextMenu(new ContextMenu());
        _signatureTextField.setContextMenu(new ContextMenu());

        _headingTextField.setText("Уважаемый, ");
        _congratulationsTextArea.setWrapText(true);

        _congratulationsTextArea.setText("От всей души желаем счастья, " +
                "крепкого здоровья, ещё больших успехов в Вашей нелегкой работе, " +
                "долгих лет жизни, благополучия в семье и всегда отличного настроения.");
        _signatureTextField.setText("С уважением, Ваш ________________________");

        _headingTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _nameComboBox.getEditor().requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _cancelButton.requestFocus();
                else
                    _nameComboBox.requestFocus();
            }
        });

        _nameComboBox.getEditor().setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _congratulationsTextArea.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _headingTextField.requestFocus();
                else
                    _congratulationsTextArea.requestFocus();
            }
        });

        _signatureTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _nextButton.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if (event.isShiftDown())
                    _congratulationsTextArea.requestFocus();
                else
                    _nextButton.requestFocus();
            }
        });
    }

    void showAndWait(Stage primaryStage)
    {
        _printCongrStage = new Stage();
        _printCongrBorderPane = new BorderPane();

        _printCongrScene = new Scene(_printCongrBorderPane, 625, 600);
        _printCongrScene.addEventFilter(KeyEvent.KEY_RELEASED, this::bindingKeys);

        _printCongrBorderPane.setTop(getTop());
        _printCongrBorderPane.setCenter(getPage1());
        _printCongrBorderPane.setBottom(getBottom());

        _printCongrStage.setScene(_printCongrScene);
        _printCongrStage.initModality(Modality.WINDOW_MODAL);
        _printCongrStage.initOwner(primaryStage);
        _printCongrStage.setResizable(true);
        _printCongrStage.setTitle("Печать поздравления");
        _printCongrStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        _printCongrStage.setMinWidth(650);
        _printCongrStage.setMaxWidth(650);
        _printCongrStage.setOnCloseRequest(event ->
        {
            if(!MainForm.getAlertAskConfirmationDialog("Вы уверены, что хотите закрыть окно создания поздравления?"))
                event.consume();
        });
        _printCongrStage.showAndWait();
    }

    private VBox getBottom()
    {
        VBox bottomVBox = new VBox();
        HBox bottomHBox = new HBox();
        _navigationBtnsHBox = new HBox();
        _nextButton = new Button("Вперед");
        _previousButton = new Button("Назад");
        _printButton = new Button("Распечатать");
        _cancelButton = new Button("Отмена");
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        ImageView nextImageView = new ImageView(MainForm.getIcon("next_btn_32.png"));
        ImageView previousImageView = new ImageView(MainForm.getIcon("previous_btn_32.png"));
        ImageView printImageView = new ImageView(MainForm.getIcon("print_btn_32.png"));
        ImageView cancelImageView = new ImageView(MainForm.getIcon("cancel_btn_32.png"));
        final String pageString = "Страница ";
        Label pageLabel = new Label(pageString + _page);
        pageLabel.setFont(_fontForTopAndBottom);

        cancelImageView.setFitHeight(32);
        cancelImageView.setFitWidth(32);
        nextImageView.setFitHeight(32);
        nextImageView.setFitWidth(32);
        previousImageView.setFitHeight(32);
        previousImageView.setFitWidth(32);
        printImageView.setFitHeight(32);
        printImageView.setFitWidth(32);

        _nextButton.setPrefWidth(120);
        _previousButton.setPrefWidth(120);
        _printButton.setPrefWidth(120);

        _previousButton.setGraphic(previousImageView);
        _previousButton.setDisable(true);
        _previousButton.setOnAction(event ->
        {
            switch (_page)
            {
                case 2:
                    _page = 1;
                    pageLabel.setText(pageString + _page);
                    _printCongrBorderPane.setCenter(getPage1());
                    _hintTopLabel.setText(_hint1);
                    _previousButton.setDisable(true);
                    _imagesToggleGroup.getSelectedToggle().setSelected(false);
                    _image = 0;
                    checkToSelectedImages();
                    break;

                case 3:
                    _page = 2;
                    pageLabel.setText(pageString + _page);
                    _printCongrBorderPane.setCenter(getPage2());
                    _hintTopLabel.setText(_hint2);
                    _navigationBtnsHBox.getChildren().remove(_printButton);
                    _navigationBtnsHBox.getChildren().addAll(_nextButton);
                    break;
            }
        });

        _nextButton.setGraphic(nextImageView);
        _nextButton.setOnAction(event ->
        {
            if(_imagesToggleGroup.getSelectedToggle() != null)
            {

                switch (_page)
                {
                    case 1:
                        _page = 2;
                        pageLabel.setText(pageString + _page);
                        _printCongrBorderPane.setCenter(getPage2());
                        _hintTopLabel.setText(_hint2);
                        _previousButton.setDisable(false);
                        break;

                    case 2:
                        _page = 3;
                        pageLabel.setText(pageString + _page);
                        _printCongrBorderPane.setCenter(getPage3());
                        _hintTopLabel.setText(_hint3);
                        _navigationBtnsHBox.getChildren().remove(_nextButton);
                        _navigationBtnsHBox.getChildren().addAll(_printButton);
                        break;
                }
            }
            else
                MainForm.getAlertWarningDialog("Макет не выбран");
        });

        _printButton.setGraphic(printImageView);
        _printButton.setOnAction(event ->
        {
            printJob();
        });

        _cancelButton.setCancelButton(true);
        _cancelButton.setGraphic(cancelImageView);
        _cancelButton.setOnAction(event ->
        {
            if (MainForm.getAlertAskConfirmationDialog("Вы уверены?"))
                _printCongrStage.close();
        });

        _nextButton.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _nextButton.fire();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _signatureTextField.requestFocus();
                else
                    _previousButton.requestFocus();
            }
        });

        _previousButton.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _previousButton.fire();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _nextButton.requestFocus();
                else
                    _cancelButton.requestFocus();
            }
        });

        _cancelButton.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _cancelButton.fire();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _previousButton.requestFocus();
                else
                    _headingTextField.requestFocus();
            }
        });

        pageLabel.setAlignment(Pos.CENTER);

        _navigationBtnsHBox.setAlignment(Pos.CENTER);
        _navigationBtnsHBox.setSpacing(50);
        _navigationBtnsHBox.getChildren().addAll(_previousButton, pageLabel, _nextButton);
        HBox.setHgrow(_navigationBtnsHBox, Priority.ALWAYS);

        bottomHBox.getChildren().addAll(_navigationBtnsHBox, _cancelButton);
        bottomHBox.setAlignment(Pos.CENTER_RIGHT);
        bottomHBox.setSpacing(30);
        HBox.setHgrow(bottomHBox, Priority.ALWAYS);
        bottomHBox.setPadding(new Insets(10));

        bottomVBox.getChildren().addAll(separator, bottomHBox);
        return bottomVBox;
    }

    private HBox getTop()
    {
        HBox topHBox = new HBox();

        _hintTopLabel = new Label(_hint1);
        _hintTopLabel.setFont(_fontForTopAndBottom);


        topHBox.setPadding(new Insets(10));
        HBox.setHgrow(topHBox, Priority.ALWAYS);
        topHBox.setAlignment(Pos.CENTER);
        topHBox.getChildren().addAll(_hintTopLabel);
        return topHBox;
    }

    private ScrollPane getPage1()
    {
        VBox page1VBox = new VBox();
        HBox images1HBox = new HBox();
        HBox images2HBox = new HBox();
        HBox images3HBox = new HBox();
        _imagesToggleGroup = new ToggleGroup();
        ScrollPane page1ScrollPane = new ScrollPane(page1VBox);

        _image1.setPadding(new Insets(15));
        _image2.setPadding(new Insets(15));
        _image3.setPadding(new Insets(15));
        _image4.setPadding(new Insets(15));
        _image5.setPadding(new Insets(15));
        _image6.setPadding(new Insets(15));

        _image1.setOnAction(event ->
        {
            checkToSelectedImages();
            _image = 1;
        });
        _image1.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_image1.isFocused())
            {
                checkToSelectedImages();
                page1ScrollPane.setVvalue(0.0);
            }
        });
        _image2.setOnAction(event ->
        {
            checkToSelectedImages();
            _image = 2;
        });
        _image2.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_image2.isFocused())
            {
                checkToSelectedImages();
                page1ScrollPane.setVvalue(0.0);
            }

        });
        _image3.setOnAction(event ->
        {
            checkToSelectedImages();
            _image = 3;
        });
        _image3.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_image3.isFocused())
            {
                checkToSelectedImages();
                page1ScrollPane.setVvalue(0.5);
            }

        });
        _image4.setOnAction(event ->
        {
            checkToSelectedImages();
            _image = 4;
        });
        _image4.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_image4.isFocused())
            {
                checkToSelectedImages();
                page1ScrollPane.setVvalue(0.5);
            }

        });
        _image5.setOnAction(event ->
        {
            checkToSelectedImages();
            _image = 5;
        });
        _image5.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_image5.isFocused())
            {
                checkToSelectedImages();
                page1ScrollPane.setVvalue(1.0d);
            }

        });
        _image6.setOnAction(event ->
        {
            checkToSelectedImages();
            _image = 6;
        });
        _image6.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_image6.isFocused())
            {
                checkToSelectedImages();
                page1ScrollPane.setVvalue(1.0d);
            }

        });

        _image1.setToggleGroup(_imagesToggleGroup);
        _image2.setToggleGroup(_imagesToggleGroup);
        _image3.setToggleGroup(_imagesToggleGroup);
        _image4.setToggleGroup(_imagesToggleGroup);
        _image5.setToggleGroup(_imagesToggleGroup);
        _image6.setToggleGroup(_imagesToggleGroup);

        images1HBox.setSpacing(20);
        images1HBox.getChildren().addAll(_image1, _image2);
        images2HBox.setSpacing(20);
        images2HBox.getChildren().addAll(_image3, _image4);
        images3HBox.setSpacing(20);
        images3HBox.getChildren().addAll(_image5, _image6);

        page1VBox.setSpacing(20);
        page1VBox.setPadding(new Insets(15));
        page1VBox.setStyle("-fx-background-color: #f0f8ff");
        HBox.setHgrow(page1VBox, Priority.ALWAYS);
        page1VBox.getChildren().addAll(images1HBox, images2HBox, images3HBox);

        return page1ScrollPane;
    }

    private VBox getPage2()
    {
        VBox page2VBox = new VBox();
        TitledPane headingTitledPane = new TitledPane();
        TitledPane congratulationsTitledPane = new TitledPane();
        TitledPane signatureTitledPane = new TitledPane();
        HBox headingHBox = new HBox();

        _headingTextField.setMinWidth(350);
        _headingTextField.requestFocus();

        headingHBox.setSpacing(10);
        HBox.setHgrow(headingHBox, Priority.ALWAYS);
        headingHBox.getChildren().addAll(_headingTextField, _nameComboBox);

        MainForm.setTitledPane(headingTitledPane, headingHBox, "Заголовок поздравления");
        MainForm.setTitledPane(congratulationsTitledPane, _congratulationsTextArea, "Текст поздравления");
        MainForm.setTitledPane(signatureTitledPane, _signatureTextField, "Подпись поздравления");

        switch (_image)
        {
            case 1:
                addTextLimiter(_headingTextField, 50);
                addTextLimiter(_congratulationsTextArea, 290, 6);
                addTextLimiter(_signatureTextField, 100);
                break;
            case 2:
                addTextLimiter(_headingTextField, 80);
                addTextLimiter(_congratulationsTextArea, 340, 8);
                addTextLimiter(_signatureTextField, 130);
                break;
            case 3:
                addTextLimiter(_headingTextField, 80);
                addTextLimiter(_congratulationsTextArea, 360, 7);
                addTextLimiter(_signatureTextField, 130);
                break;
            case 4:
                addTextLimiter(_headingTextField, 40);
                addTextLimiter(_congratulationsTextArea, 300, 5);
                addTextLimiter(_signatureTextField, 90);
                break;
            case 5:
                addTextLimiter(_headingTextField, 80);
                addTextLimiter(_congratulationsTextArea, 380, 9);
                addTextLimiter(_signatureTextField, 100);
                break;
            case 6:
                addTextLimiter(_headingTextField, 60);
                addTextLimiter(_congratulationsTextArea, 250, 6);
                addTextLimiter(_signatureTextField, 100);
                break;
        }

        VBox.setVgrow(page2VBox, Priority.ALWAYS);
        page2VBox.setStyle("-fx-background-color: #f0f8ff");
        page2VBox.setPadding(new Insets(15));
        page2VBox.setAlignment(Pos.CENTER);
        page2VBox.setSpacing(100);
        page2VBox.getChildren().addAll(headingTitledPane, congratulationsTitledPane, signatureTitledPane);
        return page2VBox;
    }

    private ScrollPane getPage3()
    {
        StackPane imageStackPane = new StackPane();
        ScrollPane page3ScrollPane = new ScrollPane(imageStackPane);
        Text headText = new Text(_headingTextField.getText() + " " + _nameComboBox.getEditor().getText() + "!");
        Text congrText = new Text(_congratulationsTextArea.getText());
        Text signagureText = new Text(_signatureTextField.getText());
        StackPane topStackPane = new StackPane();
        StackPane centerStackPane = new StackPane();
        StackPane bottomStackPane = new StackPane();
        ImageView backgroundImageView;

        headText.setFont(_headFont);
        congrText.setFont(_congrFont);
        signagureText.setFont(_signagureFont);

        topStackPane.getChildren().addAll(headText);
        centerStackPane.getChildren().addAll(congrText);
        bottomStackPane.getChildren().addAll(signagureText);

        topStackPane.setAlignment(Pos.TOP_LEFT);
        centerStackPane.setAlignment(Pos.TOP_LEFT);
        bottomStackPane.setAlignment(Pos.TOP_LEFT);

        imageStackPane.setAlignment(Pos.TOP_LEFT);

        switch (_image)
        {
            case 1:
                headText.setWrappingWidth(340);
                congrText.setWrappingWidth(340);
                signagureText.setWrappingWidth(220);
                topStackPane.setPadding(new Insets(200,0,0,150));
                centerStackPane.setPadding(new Insets(290, 0, 0, 130));
                bottomStackPane.setPadding(new Insets(580,0,120,150));
                backgroundImageView = new ImageView(getImage("1.jpg"));
                backgroundImageView.setFitWidth(600);
                backgroundImageView.setFitHeight(848);
                imageStackPane.getChildren().addAll(backgroundImageView, topStackPane, centerStackPane, bottomStackPane);
                StackPane.setAlignment(backgroundImageView, Pos.CENTER);
                _printStackPane = imageStackPane;
                break;
            case 2:
                headText.setWrappingWidth(380);
                congrText.setWrappingWidth(360);
                signagureText.setWrappingWidth(285);
                topStackPane.setPadding(new Insets(70,0,0,150));
                centerStackPane.setPadding(new Insets(170, 0, 0, 190));
                bottomStackPane.setPadding(new Insets(540,0,120,260));
                backgroundImageView = new ImageView(getImage("2.jpg"));
                backgroundImageView.setFitWidth(600);
                backgroundImageView.setFitHeight(848);
                imageStackPane.getChildren().addAll(backgroundImageView, topStackPane, centerStackPane, bottomStackPane);
                StackPane.setAlignment(backgroundImageView, Pos.CENTER);
                _printStackPane = imageStackPane;
                break;
            case 3:
                headText.setWrappingWidth(340);
                congrText.setWrappingWidth(360);
                signagureText.setWrappingWidth(200);
                topStackPane.setPadding(new Insets(200,0,0,210));
                centerStackPane.setPadding(new Insets(300, 0, 0, 180));
                bottomStackPane.setPadding(new Insets(560,0,0,300));
                backgroundImageView = new ImageView(getImage("3.jpg"));
                backgroundImageView.setFitWidth(600);
                backgroundImageView.setFitHeight(848);
                imageStackPane.getChildren().addAll(backgroundImageView, topStackPane, centerStackPane, bottomStackPane);
                StackPane.setAlignment(backgroundImageView, Pos.CENTER);
                _printStackPane = imageStackPane;
                break;
            case 4:
                headText.setWrappingWidth(200);
                congrText.setWrappingWidth(320);
                signagureText.setWrappingWidth(220);
                topStackPane.setPadding(new Insets(212,0,0,207));
                centerStackPane.setPadding(new Insets(300, 0, 0, 160));
                bottomStackPane.setPadding(new Insets(480,0,0,125));
                backgroundImageView = new ImageView(getImage("4.jpg"));
                backgroundImageView.setFitWidth(600);
                backgroundImageView.setFitHeight(848);
                imageStackPane.getChildren().addAll(backgroundImageView, topStackPane, centerStackPane, bottomStackPane);
                StackPane.setAlignment(backgroundImageView, Pos.CENTER);
                _printStackPane = imageStackPane;
                break;
            case 5:
                headText.setWrappingWidth(320);
                congrText.setWrappingWidth(355);
                signagureText.setWrappingWidth(230);
                topStackPane.setPadding(new Insets(105,0,0,200));
                centerStackPane.setPadding(new Insets(200, 0, 0, 170));
                bottomStackPane.setPadding(new Insets(520,0,0,280));
                backgroundImageView = new ImageView(getImage("5.jpg"));
                backgroundImageView.setFitWidth(600);
                backgroundImageView.setFitHeight(848);
                imageStackPane.getChildren().addAll(backgroundImageView, topStackPane, centerStackPane, bottomStackPane);
                StackPane.setAlignment(backgroundImageView, Pos.CENTER);
                _printStackPane = imageStackPane;
                break;
            case 6:
                headText.setWrappingWidth(270);
                congrText.setWrappingWidth(260);
                signagureText.setWrappingWidth(200);
                topStackPane.setPadding(new Insets(240,0,0,150));
                centerStackPane.setPadding(new Insets(340, 0, 0, 180));
                bottomStackPane.setPadding(new Insets(550,0,0,270));
                backgroundImageView = new ImageView(getImage("6.jpg"));
                backgroundImageView.setFitWidth(600);
                backgroundImageView.setFitHeight(848);
                imageStackPane.getChildren().addAll(backgroundImageView, topStackPane, centerStackPane, bottomStackPane);
                StackPane.setAlignment(backgroundImageView, Pos.CENTER);
                _printStackPane = imageStackPane;
                break;
        }

        return page3ScrollPane;
    }

    private void printJob()
    {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        Printer printer = printerJob.getPrinter();
        JobSettings jobSettings = printerJob.getJobSettings();
        PageLayout pageLayout = jobSettings.getPageLayout();

        pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.EQUAL);
        jobSettings.setPageLayout(pageLayout);

        if(printerJob == null){
            return;
        }

        boolean proceed = printerJob.showPrintDialog(_printCongrStage);

        if(proceed)
        {
            boolean printed = printerJob.printPage(_printStackPane);
            if(printed)
            {
                printerJob.endJob();
                _printCongrStage.close();
            }
        }
    }

    private ImageView getImageView(final String imageName)
    {
        Image image = null;
        try
        {
            FileInputStream fs = new FileInputStream(Database.path + "\\images\\congratulations\\" + imageName);
            image = new Image(fs);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(353);
        imageView.setFitWidth(250);

        return imageView;
    }

    private Image getImage(final String imageName)
    {
        Image image = null;
        try
        {
            FileInputStream fs = new FileInputStream(Database.path + "\\images\\congratulations\\" + imageName);
            image = new Image(fs);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return image;
    }

    private void checkToSelectedImages()
    {
        for (ToggleButton button : _toggleButtonsList)
        {
            if(button.isSelected())
            {
                button.setStyle("-fx-background-color: #3090f7");
                _image = _toggleButtonsList.indexOf(button) + 1;
            }
            else
                button.setStyle("");
        }
    }

    private void bindingKeys(KeyEvent event)
    {
        final KeyCombination keyCombCtrlN = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlB = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);

        if(event.getCode() == KeyCode.ESCAPE)
            _cancelButton.fire();

        if(event.getCode() == KeyCode.F1)
        {
            HelpForm helpForm = new HelpForm("print congratulation");
            helpForm.showAndWait(_printCongrStage);
        }

        if(keyCombCtrlN.match(event))
        {
            if(_page != 3)
                _nextButton.fire();
            else _printButton.fire();
            event.consume();
        }

        if(keyCombCtrlB.match(event))
        {
            if(!_previousButton.isDisable())
                _previousButton.fire();
            event.consume();
        }
    }

    class TabIgnoringTextArea extends TextArea
    {
        final TextArea myTextArea = this;

        TabIgnoringTextArea()
        {
            addEventFilter(KeyEvent.KEY_PRESSED, new TabAndEnterHandler());
        }

        class TabAndEnterHandler implements EventHandler<KeyEvent>
        {
            private KeyEvent recodedEvent;

            @Override
            public void handle(KeyEvent event)
            {
                if (recodedEvent != null)
                {
                    recodedEvent = null;
                    return;
                }

                Parent parent = myTextArea.getParent();
                if (parent != null)
                {
                    switch (event.getCode())
                    {
                        case TAB:
                            if (event.isControlDown())
                            {
                                recodedEvent = recodeWithoutControlDown(event);
                                myTextArea.fireEvent(recodedEvent);
                            } else if(event.isShiftDown())
                            {
                                _nameComboBox.requestFocus();
                            } else
                            {
                                _signatureTextField.requestFocus();
                            }
                            event.consume();
                            break;

                        case ENTER:
                            if (!event.isControlDown())
                            {
                                recodedEvent = recodeWithoutControlDown(event);
                                myTextArea.fireEvent(recodedEvent);
                            }
                            event.consume();
                            break;
                    }
                }
            }

            private KeyEvent recodeWithoutControlDown(KeyEvent event)
            {
                return new KeyEvent(
                        event.getEventType(),
                        event.getCharacter(),
                        event.getText(),
                        event.getCode(),
                        event.isShiftDown(),
                        false,
                        event.isAltDown(),
                        event.isMetaDown()
                );
            }
        }
    }

    public static void addTextLimiter(final TextArea textArea, final int maxLength, final int maxLines) {
        textArea.textProperty().addListener((ov, oldValue, newValue) ->
        {
            if (textArea.getText().length() > maxLength)
            {
                textArea.setText(oldValue);
            }
            int currLines = 0;
            for(char c : textArea.getText().toCharArray())
                if(c == '\n')
                    currLines++;

            if(currLines+1 >= maxLines)
                textArea.setText(oldValue);

        });
    }

    public static void addTextLimiter(final TextField textField, final int maxLength)
    {
        textField.textProperty().addListener((ov, oldValue, newValue) ->
        {
            if (textField.getText().length() > maxLength)
                textField.setText(oldValue);
        });
    }
}
