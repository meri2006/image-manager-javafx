package com.imagemanager.controller;

import com.imagemanager.model.ImageModel;
import com.imagemanager.model.transform.FlipTransformation;
import com.imagemanager.util.ImageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ImageView mainImageView;
    @FXML private Label fileNameLabel;
    @FXML private Label imageSizeLabel;
    @FXML private ListView<String> libraryListView;
    @FXML private TextField tagInputField;
    @FXML private ListView<String> tagListView;
    @FXML private TextField searchTagField;
    @FXML private ListView<String> searchResultListView;
    @FXML private ListView<String> transformHistoryList;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final ImageService service = new ImageService();
    private ImageModel currentModel = null;
    private final ObservableList<String> libraryNames  = FXCollections.observableArrayList();
    private final ObservableList<String> tagItems      = FXCollections.observableArrayList();
    private final ObservableList<String> historyItems  = FXCollections.observableArrayList();
    private final ObservableList<String> searchResults = FXCollections.observableArrayList();


    private final List<ImageModel> searchResultImages = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        libraryListView.setItems(libraryNames);
        tagListView.setItems(tagItems);
        transformHistoryList.setItems(historyItems);
        searchResultListView.setItems(searchResults);

        // Quand on clique sur une image dans la bibliotheque → la selectionner
        libraryListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (idx >= 0 && idx < service.getLibrary().getImages().size())
                selectImage(service.getLibrary().getImages().get(idx));
        });

        // NOUVEAU : quand on clique sur un resultat de recherche → afficher l'image
        // On recupere l'index du resultat clique et on selectionne l'ImageModel correspondant
        searchResultListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (idx >= 0 && idx < searchResultImages.size())
                selectImage(searchResultImages.get(idx));
        });
    }

    @FXML
    public void handleOpenImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Ouvrir une image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
        );
        File file = chooser.showOpenDialog(getStage());
        if (file != null) {
            ImageModel model = service.loadImage(file);
            libraryNames.add(model.getFileName());
            selectImage(model);
            setStatus("Image chargée : " + model.getFileName());
        }
    }

    @FXML
    public void handleSaveImage() {
        if (currentModel == null) { setStatus("Aucune image sélectionnée."); return; }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Sauvegarder l'image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        chooser.setInitialFileName(currentModel.getFileName().replaceAll("\\.[^.]+$", "") + "_modifié.png");
        File file = chooser.showSaveDialog(getStage());
        if (file != null) {
            try {
                service.saveImageToFile(currentModel, file);
                setStatus("Image sauvegardée : " + file.getName());
            } catch (IOException e) {
                setStatus("Erreur : " + e.getMessage());
            }
        }
    }

    @FXML public void handleSaveMetadata() { service.saveMetadata(); setStatus("Métadonnées sauvegardées."); }

    @FXML public void handleResetImage() {
        if (currentModel == null) return;
        service.resetImage(currentModel);
        refreshImageView();
        historyItems.clear();
        setStatus("Image réinitialisée.");
    }

    @FXML public void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Gestionnaire d'Images - POO Java 2025-2026");
        alert.setContentText("Projet réalisé en JavaFX avec architecture MVC.");
        alert.showAndWait();
    }

    @FXML public void handleFilterSwapRGB()   { applyFilter("SwapRGB"); }
    @FXML public void handleFilterGrayscale() { applyFilter("Grayscale"); }
    @FXML public void handleFilterSepia()     { applyFilter("Sepia"); }
    @FXML public void handleFilterPrewitt()   { applyFilter("Prewitt"); }

    private void applyFilter(String filterName) {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        service.applyFilter(currentModel, filterName);
        refreshImageView();
        historyItems.add("Filtre : " + filterName);
        setStatus("Filtre appliqué : " + filterName);
    }

    @FXML public void handleRotate90()  { applyRotation(90); }
    @FXML public void handleRotate180() { applyRotation(180); }
    @FXML public void handleRotate270() { applyRotation(270); }

    private void applyRotation(int degrees) {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        service.applyRotation(currentModel, degrees);
        refreshImageView();
        historyItems.add("Rotation : " + degrees + "°");
        setStatus("Rotation de " + degrees + "° appliquée.");
    }

    @FXML public void handleFlipHorizontal() {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        service.applyFlip(currentModel, FlipTransformation.FlipDirection.HORIZONTAL);
        refreshImageView();
        historyItems.add("Symétrie horizontale");
        setStatus("Symétrie horizontale appliquée.");
    }

    @FXML public void handleFlipVertical() {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        service.applyFlip(currentModel, FlipTransformation.FlipDirection.VERTICAL);
        refreshImageView();
        historyItems.add("Symétrie verticale");
        setStatus("Symétrie verticale appliquée.");
    }

    @FXML public void handleEncrypt() {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        String pwd = passwordField.getText();
        if (pwd == null || pwd.isBlank()) { setStatus("Veuillez saisir un mot de passe."); return; }
        service.encryptImage(currentModel, pwd);
        refreshImageView();
        historyItems.add("Chiffrement appliqué");
        passwordField.clear();
        setStatus("Image chiffrée.");
    }

    @FXML public void handleDecrypt() {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        String pwd = passwordField.getText();
        if (pwd == null || pwd.isBlank()) { setStatus("Veuillez saisir le mot de passe."); return; }
        service.decryptImage(currentModel, pwd);
        refreshImageView();
        historyItems.add("Déchiffrement appliqué");
        passwordField.clear();
        setStatus("Image déchiffrée.");
    }

    @FXML public void handleAddTag() {
        if (currentModel == null) { setStatus("Veuillez d'abord charger une image."); return; }
        String tag = tagInputField.getText();
        if (tag == null || tag.isBlank()) return;
        service.addTag(currentModel, tag.trim());
        tagItems.setAll(currentModel.getTags());
        tagInputField.clear();
        setStatus("Tag ajouté : " + tag.trim());
    }

    @FXML public void handleRemoveTag() {
        if (currentModel == null) return;
        String selected = tagListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        service.removeTag(currentModel, selected);
        tagItems.setAll(currentModel.getTags());
        setStatus("Tag supprimé : " + selected);
    }

    @FXML public void handleSearchByTag() {
        String tag = searchTagField.getText();
        if (tag == null || tag.isBlank()) return;
        List<ImageModel> results = service.searchByTag(tag.trim().toLowerCase());

        // On vide les deux listes avant de remplir
        searchResults.clear();
        searchResultImages.clear(); // NOUVEAU : vider aussi la liste des objets

        for (ImageModel img : results) {
            searchResults.add(img.getFileName() + " [" + String.join(", ", img.getTags()) + "]");
            searchResultImages.add(img); // NOUVEAU : garder l'objet ImageModel
        }
        setStatus(results.isEmpty() ? "Aucun résultat." : results.size() + " image(s) trouvée(s).");
    }

    private void selectImage(ImageModel model) {
        this.currentModel = model;
        refreshImageView();
        fileNameLabel.setText(model.getFileName());
        imageSizeLabel.setText((int)model.getCurrentImage().getWidth() + " x " + (int)model.getCurrentImage().getHeight());
        tagItems.setAll(model.getTags());
        historyItems.setAll(model.getAppliedTransformations());
    }

    private void refreshImageView() {
        if (currentModel != null) {
            WritableImage img = currentModel.getCurrentImage();
            mainImageView.setImage(img);
            imageSizeLabel.setText((int)img.getWidth() + " x " + (int)img.getHeight());
        }
    }

    private void setStatus(String msg) { statusLabel.setText(msg); }
    private Stage getStage() { return (Stage) mainImageView.getScene().getWindow(); }
}