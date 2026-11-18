# Gestionnaire d'images – Java / JavaFX

Application de bureau en **Java** avec une interface graphique **JavaFX**, réalisée en **programmation orientée objet** (L2 Informatique, Université de Limoges, 2025-2026).

Elle permet d'ouvrir des images, de leur appliquer des filtres et des transformations, de les organiser avec des tags et de les **brouiller avec un mot de passe**.

<!-- Ajoute ici une capture d'écran de l'application : glisse l'image "capture.png" dans le dépôt, puis garde la ligne ci-dessous -->
![Capture de l'application](capture.png)

## Fonctionnalités

| Domaine | Ce que fait l'application |
|---|---|
| Images | Ouvrir une image (PNG, JPG, BMP, GIF), l'afficher, l'enregistrer en PNG, revenir à l'original |
| Filtres | Noir et blanc, sépia, échange des couleurs (R, G, B → G, B, R), détection de contours (filtre de Prewitt) |
| Transformations | Rotation de 90°, 180° ou 270°, symétrie horizontale ou verticale |
| Historique | Liste des filtres et transformations appliqués à chaque image |
| Organisation | Ajouter ou retirer des tags, rechercher une image par tag ou par nom |
| Sauvegarde | Tags et historique enregistrés dans un fichier JSON (bibliothèque Jackson) |
| Sécurité | Brouillage et restauration d'une image avec un mot de passe |

## Brouillage par mot de passe

1. Le mot de passe est **haché avec SHA-256**.
2. Les premiers octets du hachage servent de **graine** à un générateur de nombres pseudo-aléatoires.
3. Ce générateur produit un **mélange des pixels** (permutation de Fisher-Yates), toujours le même pour un même mot de passe.
4. Avec le bon mot de passe, l'application applique la permutation inverse et l'image revient à l'identique.

**Limites connues :** c'est un brouillage, pas un chiffrement fort. Les couleurs des pixels ne changent pas, seule leur position change, et le générateur utilisé (`java.util.Random`) n'est pas conçu pour la cryptographie. Pour aller plus loin, on pourrait utiliser un vrai algorithme de chiffrement comme AES.

## Choix de conception

- **Architecture MVC** : le contrôleur `MainController` gère seulement l'interface. Toute la logique est dans `ImageService`.
- **Héritage** : la classe abstraite `AbstractPixelFilter` regroupe le parcours des pixels. Chaque filtre redéfinit seulement le calcul d'un pixel.
- **Interfaces** : `IFilter` et `ITransformation` permettent de traiter tous les filtres et toutes les transformations de la même façon.
- **Patron DAO** : `IMetadataDAO` sépare la sauvegarde du reste du code. L'implémentation JSON (`JsonMetadataDAO`) pourrait être remplacée par une base de données H2 sans toucher au reste.

## Organisation du code

```
src/main/java/com/imagemanager/
├── MainApp.java              Point d'entrée JavaFX
├── controller/               MainController (interface graphique)
├── model/                    ImageModel, ImageLibrary
│   ├── filter/               IFilter, AbstractPixelFilter et les 4 filtres
│   ├── transform/            ITransformation, rotation, symétrie
│   └── security/             ImageEncryption (brouillage par mot de passe)
├── dao/                      IMetadataDAO, MetadataDTO, JsonMetadataDAO
└── util/                     ImageService (logique de l'application)
src/main/resources/com/imagemanager/fxml/
├── MainView.fxml             Description de l'interface
└── style.css                 Style de l'interface
```

## Lancer le projet

**Prérequis :** JDK 17 ou plus récent, et Maven (déjà inclus dans IntelliJ IDEA).

**Avec IntelliJ IDEA :**
1. `File > Open`, puis choisir le dossier du projet. IntelliJ détecte le fichier `pom.xml`.
2. Attendre que Maven télécharge les dépendances.
3. Ouvrir le terminal d'IntelliJ et taper `mvn javafx:run`.

**En ligne de commande :**
```
mvn javafx:run
```

## Technologies

- **Java 17**
- **JavaFX 21** : interface graphique (FXML et CSS)
- **Jackson 2.16** : lecture et écriture du fichier JSON
- **Maven** : gestion du projet et des dépendances

## Auteur

**Mariem El Babidi** – Licence Informatique
