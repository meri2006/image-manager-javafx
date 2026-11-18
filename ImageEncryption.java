package com.imagemanager.model.security;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ImageEncryption {

    public WritableImage process(WritableImage image, String password, boolean encrypt) {
        int width  = (int) image.getWidth();
        int height = (int) image.getHeight();
        int total  = width * height;

        // Générer la graine depuis le mot de passe
        long seed = getSeed(password);//transforme le mot de passe en nombre
        Random rng = new Random(seed);//Random(seed)  générateur qui produit toujours la même séquence

        // Construire la permutation: Créer le tableau de positions
        int[] permutation = new int[total];
        for (int i = 0; i < total; i++) permutation[i] = i;

        // mélange le tableau
      //On échange deux positions aléatoires
        //→ On répète jusqu'à tout mélanger
        for (int i = total - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1); //choisit une position aléatoire
            int tmp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = tmp;
        }

        // Lire tous les pixels
        PixelReader reader = image.getPixelReader();
        int[] pixels = new int[total];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                pixels[y * width + x] = reader.getArgb(x, y);//On les met dans  1D

        // Appliquer la permutation
        int[] result = new int[total];
        if (encrypt) {
            for (int i = 0; i < total; i++)
                result[permutation[i]] = pixels[i];
        } else {
            for (int i = 0; i < total; i++)
                result[i] = pixels[permutation[i]];
        }

        // Écrire dans la nouvelle image

        WritableImage output = new WritableImage(width, height);
        PixelWriter writer = output.getPixelWriter();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                writer.setArgb(x, y, result[y * width + x]);

        return output;
    }

    private long getSeed(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");//SHA-256 = algorithme standard
            // qui transforme n'importe quel texte en 32 octets
            byte[] hash = digest.digest(password.getBytes());
            long seed = 0;
            for (int i = 0; i < 8; i++)
                seed = (seed << 8) | (hash[i] & 0xFF);
            return seed;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 non disponible", e);
        }
    }

    public String getName() { return "Encryption"; }
}