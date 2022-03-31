package modele;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import java.util.*;

/**
 * Classe qui permet de creer des ImageSaveFilter en fonction de ce que le systeme dis etre capable
 */
public class ImageSaveFilterBuilder {

    /**
     * Filtre stockee, par default a null
     * initialise lors de l'appel a getFilters
     */
    private static Collection<ImageSaveFilter> filters = null;

    /**
     * on parcours les services Providers de IO pour recuperer quels type de fichier sont accepete dans notre version de java
     * On les recupere du IIORegistry et pas de ImageIO pour qu'ils soit regroupe
     */
    private static void chargerExtentionAcceptee(){
        List<ImageSaveFilter> imageSaveFilterBuilder = new ArrayList<>();
        Iterator<ImageWriterSpi> serviceProviders = IIORegistry.getDefaultInstance().getServiceProviders(ImageWriterSpi.class, false);
        while (serviceProviders.hasNext()) {
            ImageWriterSpi next = serviceProviders.next();
            imageSaveFilterBuilder.add(new ImageSaveFilter(next.getFormatNames()));
        }
        filters = imageSaveFilterBuilder;
    }

    /**
     * permet de recuperer les filters applicable
     * @return ImageSaveFilter, FileFilter utiliser pour exporter des image
     */
    public static Collection<ImageSaveFilter> getFilters() {
        if (ImageSaveFilterBuilder.filters == null) chargerExtentionAcceptee();
        return filters;
    }
}
