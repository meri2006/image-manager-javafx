package com.imagemanager.dao;

import com.imagemanager.model.ImageModel;
import java.util.List;

public interface IMetadataDAO {
    void saveAll(List<ImageModel> images);
    List<MetadataDTO> loadAll();// Retourne une liste de MetadataDTO
    void save(ImageModel image);
}