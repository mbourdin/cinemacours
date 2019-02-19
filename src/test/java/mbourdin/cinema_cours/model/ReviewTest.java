package mbourdin.cinema_cours.model;

import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ReviewTest {
    Review review;
    @Test
    public void noParamConstructorTest()
    {   review=new Review();
        assertNull(review.getId());
        assertNull(review.getFilm());
        assertNull(review.getUtilisateur());
        assertNull(review.getArticle());
        assertNull(review.getDate());
        assertEquals(Review.NOUVEAU,review.getEtat());

    }
    @Test
    public void twoParamConstructorTest()
    {   Film film=new Film();
        Utilisateur utilisateur=new Utilisateur();
        review =new Review(film,utilisateur);
        assertNull(review.getId());
        assertEquals(film,review.getFilm());
        assertEquals(utilisateur,review.getUtilisateur());
        assertNull(review.getArticle());
        assertNotNull(review.getDate());
        assertEquals(Review.NOUVEAU,review.getEtat());

    }
    @Test
    public void publierOkTest()
    {   review=new Review();
        try{
        review.publier();
        assertEquals(Review.PUBLIE,review.getEtat());}
        catch (IllegalTransitionException e){fail("Transition attendue");}
    }
    @Test
    public void publierKoTest()
    {   review=new Review();
        try{
        review.publier();}
        catch (IllegalTransitionException e){fail("echec de premiere publication");}
        try{
        review.publier();
        fail("exception expected");

        }catch (IllegalTransitionException e){assertTrue(true);}

    }
    @Test
    public void supprimerOkTest()
    {   review=new Review();
        try{
        review.publier();}catch (IllegalTransitionException e){fail("echec premiere publication");}
        try{
        review.supprimer();}catch (IllegalTransitionException e){fail("echec de suppression");}
        assertEquals(Review.SUPPRIME,review.getEtat());
    }
    @Test
    public void supprimerKoTest()
    {   review=new Review();
        try{
            review.supprimer();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertTrue(true);}

    }

    @Test
    public void rejeterOkTest()
    {   review=new Review();
        try{
        review.rejeter();}catch (IllegalTransitionException e){fail("echec de rejet");}
        assertEquals(Review.REJETE,review.getEtat());
    }

    @Test
    public void rejeterKoTest()
    {   review=new Review();
        try{
        review.publier();}catch(IllegalTransitionException e){fail("echec de publication");}

        try{
            review.rejeter();
            fail("exception expected in review.rejeter()");

        }catch (IllegalTransitionException e){assertEquals(Review.PUBLIE,review.getEtat());}

    }


    @Test
    public void editerOkTest()
    {   review=new Review();
    try{
        review.editer();} catch (IllegalTransitionException e){fail("echec edition nouveau commentaire");}
        assertEquals(Review.NOUVEAU,review.getEtat());
    try{
        review.publier();}catch (IllegalTransitionException e){fail("echec de publication");}
    try{
        review.editer();}catch (IllegalTransitionException e){fail("echec d'edition commentaire publié");}
        assertEquals(Review.NOUVEAU,review.getEtat());
    try{
        review.retenirPourModif();}catch (IllegalTransitionException e){fail("echec de retenir_pour_modif");}
    try{
        review.editer();}catch (IllegalTransitionException e){fail("echec d'edition d'un commentaire à modifier");}
        assertEquals(Review.NOUVEAU,review.getEtat());
    }
    @Test
    public void editerKoTest()
    {   review=new Review();
        try{
        review.rejeter();}catch (IllegalTransitionException e){fail("echec de rejet d'un commentaire");}
        try{
            review.editer();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertTrue(true);}

    }
    @Test
    public void retenirPourModifOkTest()
    {   review=new Review();
        try{
        review.retenirPourModif();}catch (IllegalTransitionException e){fail("echec de retetinr_pour_modif");}
        assertEquals(Review.AMODIFIER, review.getEtat());
    }
    @Test
    public void retenirPourModifKoTest()
    {   review=new Review();
        try{
        review.rejeter();}catch (IllegalTransitionException e){fail("echec de rejet d'un commentaire");}
        try{
            review.retenirPourModif();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertTrue(true);}

    }


    @Test
    public void abandonnerOkTest()
    {   review=new Review();
        try{
        review.retenirPourModif();}catch (IllegalTransitionException e){fail("echec de retenir pour modification");}
        try{
        review.abandonner();}catch (IllegalTransitionException e){fail("echec d'abandon d'un commentaire" );}
        assertEquals(Review.ABANDONNE, review.getEtat());
    }
    @Test
    public void abandonnerKoTest()
    {   review=new Review();
        try{
            review.abandonner();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertTrue(true);}

    }

    @Test
    public void setArticle() {
        review=new Review();
        review.setArticle("CECI EST UN TEST");
        assertEquals("CECI EST UN TEST",review.getArticle());
    }

    @Test
    public void setDate() {
        LocalDateTime debut=LocalDateTime.now().minusMinutes(1);
        review=new Review();
        review.setDate(LocalDateTime.now());
        boolean test=debut.isBefore(review.getDate())&&review.getDate().isBefore(debut.plusMinutes(2));
        assertTrue(test);

    }
}
