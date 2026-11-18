package com.imagemanager.util;

import com.imagemanager.dao.IMetadataDAO;
import com.imagemanager.dao.JsonMetadataDAO;
import com.imagemanager.dao.MetadataDTO;
import com.imagemanager.model.ImageLibrary;
import com.imagemanager.model.ImageModel;
import com.imagemanager.model.filter.*;
import com.imagemanager.model.security.ImageEncryption;
import com.imagemanager.model.transform.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageService {

    private final ImageLibrary library;
    private final IMetadataDAO metadataDAO;
    private final ImageEncryption encryption;

    public ImageService() {
        this.library = new ImageLibrary();
        this.metadataDAO = new JsonMetadataDAO();
        this.encryption = new ImageEncryption();
    }

    public ImageModel loadImage(File file) {
        //Charge l'image depuis le disque
        //→ Crée un ImageModel avec cette image
        Image image = new Image(file.toURI().toString());
        ImageModel model = new ImageModel(file.getAbsolutePath(), image);
        List<MetadataDTO> dtos = metadataDAO.loadAll();
        for (MetadataDTO dto : dtos) {
            if (dto.getFilePath().equals(file.getAbsolutePath())) {
                model.setTags(dto.getTags());
                model.setAppliedTransformations(dto.getTransformations());
                break;
            }
        }//Charge les métadonnées depuis metadata.json
        // Ajoute l'image à la bibliothèque
        library.addImage(model);
        return model;
    }

    public WritableImage applyFilter(ImageModel model, String filterName) {
        IFilter filter = createFilter(filterName);
        if (filter == null) return model.getCurrentImage();
        WritableImage result = filter.apply(model.getCurrentImage());
        model.setCurrentImage(result);
        model.addTransformation(filter.getName());
        return result;
    }

    private IFilter createFilter(String name) {
        return switch (name) {
            case "SwapRGB"   -> new SwapRGBFilter();
            case "Grayscale" -> new GrayscaleFilter();
            case "Sepia"     -> new SepiaFilter();
            case "Prewitt"   -> new PrewittFilter();
            default          -> null;
        };
    }

    public WritableImage applyRotation(ImageModel model, int degrees) {
        ITransformation t = new RotationTransformation(degrees);
        WritableImage result = t.apply(model.getCurrentImage());
        model.setCurrentImage(result);
        model.addTransformation(t.getName());
        return result;
    }

    public WritableImage applyFlip(ImageModel model, FlipTransformation.FlipDirection direction) {
        ITransformation t = new FlipTransformation(direction);
        WritableImage result = t.apply(model.getCurrentImage());
        model.setCurrentImage(result);
        model.addTransformation(t.getName());
        return result;
    }

    public WritableImage encryptImage(ImageModel model, String password) {
        WritableImage result = encryption.process(model.getCurrentImage(), password, true);
        model.setCurrentImage(result);
        model.addTransformation(encryption.getName());
        return result;
    }

    public WritableImage decryptImage(ImageModel model, String password) {
        WritableImage result = encryption.process(model.getCurrentImage(), password, false);
        model.setCurrentImage(result);
        return result;
    }

    public void addTag(ImageModel model, String tag) { model.addTag(tag); }
    public void removeTag(ImageModel model, String tag) { model.removeTag(tag); }

    public List<ImageModel> searchByTag(String tag) { return library.searchByTag(tag); }
    public List<ImageModel> searchByName(String name) { return library.searchByName(name); }

    public void saveMetadata() { metadataDAO.saveAll(library.getImages()); }
    public void saveMetadata(ImageModel model) { metadataDAO.save(model); }
    public void resetImage(ImageModel model) { model.resetToOriginal(); }

    public void saveImageToFile(ImageModel model, File outputFile) throws IOException {
        WritableImage wImg = model.getCurrentImage();
        BufferedImage bImg = SwingFXUtils.fromFXImage(wImg, null);
        ImageIO.write(bImg, "png", outputFile);
    }

    public ImageLibrary getLibrary() { return library; }
}