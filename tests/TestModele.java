import modele.Modele;
import org.junit.*;

import static org.junit.Assert.*;

public class TestModele
{

    private Modele m;

    @Before
    public void setUp(){
        m = new Modele();

    }

    /**
     * L'affichage des packages doit alterner entre visible et invisible
     */
    @Test
    public void test_changerAffichagePackage_casNormal(){
        for (int i = 0; i < 5; i++) { // on teste de changer 10 fois l'affichage des packages
            assertTrue(m.isAfficherPackage());
            m.changerAffichagePackage();
            assertFalse(m.isAfficherPackage());
            m.changerAffichagePackage();
        }
    }

    @Test
    public void test_chargerArborescenceProjet_casFichier(){

    }

    @Test
    public void test_chargerArborescenceProjet_casRepertoire(){

    }

    @Test
    public void test_chargerArborescenceProjet_casNull(){

    }

    @Test
    public void test_chargerArborescenceProjet_casRepertoireSansClass(){

    }





}
