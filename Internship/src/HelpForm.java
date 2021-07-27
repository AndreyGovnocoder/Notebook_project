import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpForm
{
    private Stage _helpFormStage;
    private Scene _helpFormScene;
    private Button _okButton;
    private HBox _centerHBox;
    private TitledPane _centerTitledPane;
    private ListView<String> _hotKeysListView;
    private ListView<String> _descriptionListView;
    private ListView<String> _userGuideListView;
    private ListView<String> _infoListView;
    private Separator _separator;
    private String _form = "main";
    private final TextArea _generalHotKeysTextArea = new TextArea(ReferencesTexts.GENERAL_HOTKEYS);
    private final TextArea _mainFormHotKeysTextArea = new TextArea(ReferencesTexts.MAINFORM_HOTKEYS);
    private final TextArea _personFormHotKeysTextArea = new TextArea(ReferencesTexts.PERSONFORM_HOTKEYS);
    private final TextArea _printFormHotKeysTextArea = new TextArea(ReferencesTexts.PRINTFORM_HOTKEYS);
    private final TextArea _systemRequirementsTextArea = new TextArea(ReferencesTexts.SYSTEM_REQUIREMENTS);
    private final TextArea _functionsTextArea = new TextArea(ReferencesTexts.FUNCIONS);
    private final TextArea _mainFormDescriptionTextArea = new TextArea(ReferencesTexts.MAIN_FORM);
    private final TextArea _addEditFormDescriptionTextArea = new TextArea(ReferencesTexts.ADDEDIT_FORM);
    private final TextArea _printCongrFormDescriptionTextArea = new TextArea(ReferencesTexts.PRINTCONGR_FORM);
    private final TextArea _browsePersonTextArea = new TextArea(ReferencesTexts.BROWSE_PERSON);
    private final TextArea _findPersonsTextArea = new TextArea(ReferencesTexts.FIND_PERSONS);
    private final TextArea _sortPersonsTextArea = new TextArea(ReferencesTexts.SORT_PERSONS);
    private final TextArea _addPersonTextArea = new TextArea(ReferencesTexts.ADD_PERSON);
    private final TextArea _editPersonTextArea = new TextArea(ReferencesTexts.EDIT_PERSON);
    private final TextArea _removePersonTextArea = new TextArea(ReferencesTexts.REMOVE_PERSON);
    private final TextArea _printCongratulationTextArea = new TextArea(ReferencesTexts.PRINT_CONGRATULATION);
    private final TextArea _infoAboutTextArea = new TextArea(ReferencesTexts.INFO_ABOUT);
    private final TextArea _instrumentsTextArea = new TextArea(ReferencesTexts.INSTRUMENTS);
    private final TextArea _contactsTextArea = new TextArea(ReferencesTexts.CONTACTS);
    private final ObservableList<String> _hotKeysList = FXCollections.observableArrayList(
            "Основные",
            "Главное окно",
            "Окно создания/редактирования",
            "Окно печати поздравления");
    private final ObservableList<String> _descriptionList = FXCollections.observableArrayList(
            "Системные требования",
            "Функции и назначение",
            "Главное окно",
            "Окно редактирования/создания записи",
            "Окно вывода поздравления на печать");
    private final ObservableList<String> _userGuideList = FXCollections.observableArrayList(
            "Просмотр записи",
            "Поиск записи",
            "Сортировка записей",
            "Добавление записи",
            "Редактирование записи",
            "Удаление записи",
            "Вывод поздравления на печать");
    private final ObservableList<String> _infoList = FXCollections.observableArrayList(
            "Информация о разработчике",
            "Использованные средства разработки",
            "Контактные данные и техподдержка");

    HelpForm(){}

    HelpForm(String form)
    {
        this._form = form;
    }

    void showAndWait(Stage primaryStage)
    {
        _helpFormStage = new Stage();
        BorderPane helpFormBorderPane = new BorderPane();
        _helpFormScene = new Scene(helpFormBorderPane, 800, 500);
        _helpFormScene.addEventFilter(KeyEvent.KEY_RELEASED, this::bindingKeys);

        helpFormBorderPane.setCenter(getCenter());
        helpFormBorderPane.setBottom(getBottom());
        helpFormBorderPane.setLeft(getLeft());

        _helpFormStage.setScene(_helpFormScene);
        _helpFormStage.initModality(Modality.WINDOW_MODAL);
        _helpFormStage.initOwner(primaryStage);
        _helpFormStage.setTitle("Помощь");
        _helpFormStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        _helpFormStage.showAndWait();
    }

    private HBox getCenter()
    {
        _centerHBox = new HBox();
        _centerTitledPane = new TitledPane();
        TextArea emptyTextArea = new TextArea();

        _separator = new Separator();
        _separator.setOrientation(Orientation.VERTICAL);

        setTextAreas();

        emptyTextArea.setEditable(false);
        emptyTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _centerHBox.getChildren().addAll(_separator, emptyTextArea);
        HBox.setMargin(emptyTextArea, new Insets(15));
        _centerHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(_centerHBox, Priority.ALWAYS);
        VBox.setVgrow(emptyTextArea, Priority.ALWAYS);
        HBox.setHgrow(emptyTextArea, Priority.ALWAYS);

        return _centerHBox;
    }

    private VBox getLeft()
    {
        VBox leftVBox = new VBox();
        Accordion helpAccordion = new Accordion();
        TitledPane hotKeysTitledPane = new TitledPane();
        TitledPane userGuideTitledPane = new TitledPane();
        TitledPane descriptionTitledPane = new TitledPane();
        TitledPane guideTitledPane = new TitledPane();
        TitledPane infoTitledPane = new TitledPane();
        VBox userGuideVBox = new VBox();
        Accordion referenceAccordion = new Accordion();
        _hotKeysListView = new ListView<>(_hotKeysList);
        _descriptionListView = new ListView<>(_descriptionList);
        _userGuideListView = new ListView<>(_userGuideList);
        _infoListView = new ListView<>(_infoList);
        setListViews();

        VBox.setVgrow(helpAccordion, Priority.ALWAYS);
        VBox.setVgrow(_hotKeysListView, Priority.ALWAYS);
        VBox.setVgrow(leftVBox, Priority.ALWAYS);

        hotKeysTitledPane.setText("Горячие клавиши");
        hotKeysTitledPane.setContent(_hotKeysListView);

        userGuideTitledPane.setText("Справка");
        userGuideTitledPane.setContent(userGuideVBox);

        descriptionTitledPane.setText("Описание программы");
        descriptionTitledPane.setContent(_descriptionListView);

        guideTitledPane.setText("Руководство пользователя");
        guideTitledPane.setContent(_userGuideListView);

        infoTitledPane.setText("Общие сведения");
        infoTitledPane.setContent(_infoListView);

        referenceAccordion.getPanes().addAll(descriptionTitledPane, guideTitledPane, infoTitledPane);
        userGuideVBox.getChildren().addAll(referenceAccordion);
        helpAccordion.getPanes().addAll(userGuideTitledPane, hotKeysTitledPane);

        switch (_form)
        {
            case "main":
                break;

            case "search":
                helpAccordion.setExpandedPane(userGuideTitledPane);
                referenceAccordion.setExpandedPane(guideTitledPane);
                guideTitledPane.setExpanded(true);
                guideTitledPane.setExpanded(true);
                _userGuideListView.getSelectionModel().select(1);
                break;

            case "add person":
                helpAccordion.setExpandedPane(userGuideTitledPane);
                referenceAccordion.setExpandedPane(guideTitledPane);
                guideTitledPane.setExpanded(true);
                _userGuideListView.getSelectionModel().select(3);
                break;

            case "edit person":
                helpAccordion.setExpandedPane(userGuideTitledPane);
                referenceAccordion.setExpandedPane(guideTitledPane);
                guideTitledPane.setExpanded(true);
                guideTitledPane.setExpanded(true);
                _userGuideListView.getSelectionModel().select(4);
                break;

            case "print congratulation":
                helpAccordion.setExpandedPane(userGuideTitledPane);
                referenceAccordion.setExpandedPane(guideTitledPane);
                guideTitledPane.setExpanded(true);
                guideTitledPane.setExpanded(true);
                _userGuideListView.getSelectionModel().select(6);
                break;
        }

        leftVBox.setStyle("-fx-background-color: #f0f8ff;");
        leftVBox.setPadding(new Insets(10));
        leftVBox.getChildren().addAll(helpAccordion);
        return leftVBox;
    }

    private VBox getBottom()
    {
        HBox bottomHBox = new HBox();
        VBox bottomVBox = new VBox();
        _okButton = new Button("Ок");
        ImageView okBtnImageView = new ImageView(MainForm.getIcon("ok_btn_32.png"));

        _okButton.setOnAction(event -> _helpFormStage.close());
        _okButton.setGraphic(okBtnImageView);

        bottomHBox.setPadding(new Insets(10));
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.getChildren().addAll(_okButton);

        bottomVBox.getChildren().addAll(new Separator(), bottomHBox);
        return bottomVBox;
    }

    private void bindingKeys(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER)
            _okButton.fire();
    }

    private void setListViews()
    {
        _hotKeysListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _descriptionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _userGuideListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _infoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        _hotKeysListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            switch (Integer.parseInt(String.valueOf(newValue)))
            {
                case 0:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_generalHotKeysTextArea);
                    VBox.setVgrow(_generalHotKeysTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_generalHotKeysTextArea, Priority.ALWAYS);
                    HBox.setMargin(_generalHotKeysTextArea, new Insets(15));
                    break;
                case 1:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_mainFormHotKeysTextArea);
                    VBox.setVgrow(_mainFormHotKeysTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_mainFormHotKeysTextArea, Priority.ALWAYS);
                    HBox.setMargin(_mainFormHotKeysTextArea, new Insets(15));
                    break;
                case 2:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_personFormHotKeysTextArea);
                    VBox.setVgrow(_personFormHotKeysTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_personFormHotKeysTextArea, Priority.ALWAYS);
                    HBox.setMargin(_personFormHotKeysTextArea, new Insets(15));
                    break;
                case 3:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_printFormHotKeysTextArea);
                    VBox.setVgrow(_printFormHotKeysTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_printFormHotKeysTextArea, Priority.ALWAYS);
                    HBox.setMargin(_printFormHotKeysTextArea, new Insets(15));
                    break;
            }
        });

        _descriptionListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            switch (Integer.parseInt(String.valueOf(newValue)))
            {
                case 0:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_systemRequirementsTextArea);
                    VBox.setVgrow(_systemRequirementsTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_systemRequirementsTextArea, Priority.ALWAYS);
                    HBox.setMargin(_systemRequirementsTextArea, new Insets(15));
                    break;
                case 1:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_functionsTextArea);
                    VBox.setVgrow(_functionsTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_functionsTextArea, Priority.ALWAYS);
                    HBox.setMargin(_functionsTextArea, new Insets(15));
                    break;
                case 2:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_mainFormDescriptionTextArea);
                    VBox.setVgrow(_mainFormDescriptionTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_mainFormDescriptionTextArea, Priority.ALWAYS);
                    HBox.setMargin(_mainFormDescriptionTextArea, new Insets(15));
                    break;
                case 3:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_addEditFormDescriptionTextArea);
                    VBox.setVgrow(_addEditFormDescriptionTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_addEditFormDescriptionTextArea, Priority.ALWAYS);
                    HBox.setMargin(_addEditFormDescriptionTextArea, new Insets(15));
                    break;
                case 4:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_printCongrFormDescriptionTextArea);
                    VBox.setVgrow(_printCongrFormDescriptionTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_printCongrFormDescriptionTextArea, Priority.ALWAYS);
                    HBox.setMargin(_printCongrFormDescriptionTextArea, new Insets(15));
                    break;
            }
        });

        _userGuideListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            switch (Integer.parseInt(String.valueOf(newValue)))
            {
                case 0:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_browsePersonTextArea);
                    VBox.setVgrow(_browsePersonTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_browsePersonTextArea, Priority.ALWAYS);
                    HBox.setMargin(_browsePersonTextArea, new Insets(15));
                    break;
                case 1:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_findPersonsTextArea);
                    VBox.setVgrow(_findPersonsTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_findPersonsTextArea, Priority.ALWAYS);
                    HBox.setMargin(_findPersonsTextArea, new Insets(15));
                    break;
                case 2:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_sortPersonsTextArea);
                    VBox.setVgrow(_sortPersonsTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_sortPersonsTextArea, Priority.ALWAYS);
                    HBox.setMargin(_sortPersonsTextArea, new Insets(15));
                    break;
                case 3:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_addPersonTextArea);
                    VBox.setVgrow(_addPersonTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_addPersonTextArea, Priority.ALWAYS);
                    HBox.setMargin(_addPersonTextArea, new Insets(15));
                    break;
                case 4:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_editPersonTextArea);
                    VBox.setVgrow(_editPersonTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_editPersonTextArea, Priority.ALWAYS);
                    HBox.setMargin(_editPersonTextArea, new Insets(15));
                    break;
                case 5:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_removePersonTextArea);
                    VBox.setVgrow(_removePersonTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_removePersonTextArea, Priority.ALWAYS);
                    HBox.setMargin(_removePersonTextArea, new Insets(15));
                    break;
                case 6:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_printCongratulationTextArea);
                    VBox.setVgrow(_printCongratulationTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_printCongratulationTextArea, Priority.ALWAYS);
                    HBox.setMargin(_printCongratulationTextArea, new Insets(15));
                    break;
            }
        });

        _infoListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            switch (Integer.parseInt(String.valueOf(newValue)))
            {
                case 0:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_infoAboutTextArea);
                    VBox.setVgrow(_infoAboutTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_infoAboutTextArea, Priority.ALWAYS);
                    HBox.setMargin(_infoAboutTextArea, new Insets(15));
                    break;
                case 1:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_instrumentsTextArea);
                    VBox.setVgrow(_instrumentsTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_instrumentsTextArea, Priority.ALWAYS);
                    HBox.setMargin(_instrumentsTextArea, new Insets(15));
                    break;
                case 2:
                    _centerHBox.getChildren().clear();
                    _centerHBox.getChildren().addAll(_separator,_contactsTextArea);
                    VBox.setVgrow(_contactsTextArea, Priority.ALWAYS);
                    HBox.setHgrow(_contactsTextArea, Priority.ALWAYS);
                    HBox.setMargin(_contactsTextArea, new Insets(15));
                    break;
            }
        });
    }

    private void setTextAreas()
    {
        _generalHotKeysTextArea.setEditable(false);
        _generalHotKeysTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _generalHotKeysTextArea.setPadding(new Insets(15));
        _generalHotKeysTextArea.setWrapText(true);
        _generalHotKeysTextArea.setContextMenu(new ContextMenu());

        _mainFormHotKeysTextArea.setEditable(false);
        _mainFormHotKeysTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _mainFormHotKeysTextArea.setPadding(new Insets(15));
        _mainFormHotKeysTextArea.setWrapText(true);
        _mainFormHotKeysTextArea.setContextMenu(new ContextMenu());

        _personFormHotKeysTextArea.setEditable(false);
        _personFormHotKeysTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _personFormHotKeysTextArea.setPadding(new Insets(15));
        _personFormHotKeysTextArea.setWrapText(true);
        _personFormHotKeysTextArea.setContextMenu(new ContextMenu());

        _printFormHotKeysTextArea.setEditable(false);
        _printFormHotKeysTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _printFormHotKeysTextArea.setPadding(new Insets(15));
        _printFormHotKeysTextArea.setWrapText(true);
        _printFormHotKeysTextArea.setContextMenu(new ContextMenu());

        _systemRequirementsTextArea.setEditable(false);
        _systemRequirementsTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _systemRequirementsTextArea.setPadding(new Insets(15));
        _systemRequirementsTextArea.setWrapText(true);
        _systemRequirementsTextArea.setContextMenu(new ContextMenu());

        _functionsTextArea.setEditable(false);
        _functionsTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _functionsTextArea.setPadding(new Insets(15));
        _functionsTextArea.setWrapText(true);
        _functionsTextArea.setContextMenu(new ContextMenu());

        _mainFormDescriptionTextArea.setEditable(false);
        _mainFormDescriptionTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _mainFormDescriptionTextArea.setPadding(new Insets(15));
        _mainFormDescriptionTextArea.setWrapText(true);
        _mainFormDescriptionTextArea.setContextMenu(new ContextMenu());

        _addEditFormDescriptionTextArea.setEditable(false);
        _addEditFormDescriptionTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _addEditFormDescriptionTextArea.setPadding(new Insets(15));
        _addEditFormDescriptionTextArea.setWrapText(true);
        _addEditFormDescriptionTextArea.setContextMenu(new ContextMenu());

        _printCongrFormDescriptionTextArea.setEditable(false);
        _printCongrFormDescriptionTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _printCongrFormDescriptionTextArea.setPadding(new Insets(15));
        _printCongrFormDescriptionTextArea.setWrapText(true);
        _printCongrFormDescriptionTextArea.setContextMenu(new ContextMenu());

        _browsePersonTextArea.setEditable(false);
        _browsePersonTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _browsePersonTextArea.setPadding(new Insets(15));
        _browsePersonTextArea.setWrapText(true);
        _browsePersonTextArea.setContextMenu(new ContextMenu());

        _findPersonsTextArea.setEditable(false);
        _findPersonsTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _findPersonsTextArea.setPadding(new Insets(15));
        _findPersonsTextArea.setWrapText(true);
        _findPersonsTextArea.setContextMenu(new ContextMenu());

        _sortPersonsTextArea.setEditable(false);
        _sortPersonsTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _sortPersonsTextArea.setPadding(new Insets(15));
        _sortPersonsTextArea.setWrapText(true);
        _sortPersonsTextArea.setContextMenu(new ContextMenu());

        _addPersonTextArea.setEditable(false);
        _addPersonTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _addPersonTextArea.setPadding(new Insets(15));
        _addPersonTextArea.setWrapText(true);
        _addPersonTextArea.setContextMenu(new ContextMenu());

        _editPersonTextArea.setEditable(false);
        _editPersonTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _editPersonTextArea.setPadding(new Insets(15));
        _editPersonTextArea.setWrapText(true);
        _editPersonTextArea.setContextMenu(new ContextMenu());

        _removePersonTextArea.setEditable(false);
        _removePersonTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _removePersonTextArea.setPadding(new Insets(15));
        _removePersonTextArea.setWrapText(true);
        _removePersonTextArea.setContextMenu(new ContextMenu());

        _printCongratulationTextArea.setEditable(false);
        _printCongratulationTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _printCongratulationTextArea.setPadding(new Insets(15));
        _printCongratulationTextArea.setWrapText(true);
        _printCongratulationTextArea.setContextMenu(new ContextMenu());

        _infoAboutTextArea.setEditable(false);
        _infoAboutTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _infoAboutTextArea.setPadding(new Insets(15));
        _infoAboutTextArea.setWrapText(true);
        _infoAboutTextArea.setContextMenu(new ContextMenu());

        _instrumentsTextArea.setEditable(false);
        _instrumentsTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _instrumentsTextArea.setPadding(new Insets(15));
        _instrumentsTextArea.setWrapText(true);
        _instrumentsTextArea.setContextMenu(new ContextMenu());

        _contactsTextArea.setEditable(false);
        _contactsTextArea.setStyle("-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;");
        _contactsTextArea.setPadding(new Insets(15));
        _contactsTextArea.setWrapText(true);
        _contactsTextArea.setContextMenu(new ContextMenu());
    }
}
