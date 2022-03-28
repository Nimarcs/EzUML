package modele;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Locale;

public class EzumlSaveFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if(f.isDirectory()){
            return false;
        }
        String s = f.getName().toLowerCase();
        return s.endsWith(".ezuml");
    }

    @Override
    public String getDescription() {
        return "*.ezuml";
    }
}
