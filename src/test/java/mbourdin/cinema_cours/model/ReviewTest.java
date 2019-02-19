package mbourdin.cinema_cours.model;

import org.junit.Test;

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
        assertEquals(Review.NEW,review.getEtat());

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
        assertEquals(Review.NEW,review.getEtat());

    }
    @Test
    public void publishOkTest()
    {   review=new Review();
        try{
        review.validByModerator();
        assertEquals(Review.PUBLISHED,review.getEtat());}
        catch (IllegalTransitionException e){fail("Transition attendue");}
    }
    @Test
    public void publishKoTest()
    {   review=new Review();
        try{
        review.validByModerator();}
        catch (IllegalTransitionException e){fail("echec de premiere publication");}
        try{
        review.validByModerator();
        fail("exception expected");

        }catch (IllegalTransitionException e){assertEquals(Review.PUBLISHED,review.getEtat());}

    }
    @Test
    public void deleteOkTest()
    {   review=new Review();
        try{
        review.validByModerator();}catch (IllegalTransitionException e){fail("echec premiere publication");}
        try{
        review.deleteByUser();}catch (IllegalTransitionException e){fail("echec de suppression");}
        assertEquals(Review.DELETED,review.getEtat());
    }
    @Test
    public void deleteKoTest()
    {   review=new Review();
        try{
            review.deleteByUser();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertEquals(Review.NEW,review.getEtat());}

    }

    @Test
    public void rejectOkTest()
    {   review=new Review();
        try{
        review.reject();}catch (IllegalTransitionException e){fail("echec de rejet");}
        assertEquals(Review.REJECTED,review.getEtat());
    }

    @Test
    public void rejectKoTest()
    {   review=new Review();
        try{
        review.validByModerator();}catch(IllegalTransitionException e){fail("echec de publication");}

        try{
            review.reject();
            fail("exception expected in review.reject()");

        }catch (IllegalTransitionException e){assertEquals(Review.PUBLISHED,review.getEtat());}

    }


    @Test
    public void editFromNewOkTest() {
        review = new Review();
        try {
            review.edit();
        } catch (IllegalTransitionException e) {
            fail("echec edition nouveau commentaire");
        }
        assertEquals(Review.NEW, review.getEtat());
    }
    @Test
    public void editFromPublishedOkTest() {
        review = new Review();
        try {
            review.validByModerator();
        } catch (IllegalTransitionException e) {
            fail("echec de publication");
        }
        try {
            review.edit();
        } catch (IllegalTransitionException e) {
            fail("echec d'edition commentaire publié");
        }
        assertEquals(Review.NEW, review.getEtat());
    }
    @Test
    public void editFromKeepOkTest(){
        review = new Review();
    try{
        review.keepForEdit();}catch (IllegalTransitionException e){fail("echec de retenir_pour_modif");}
    try{
        review.edit();}catch (IllegalTransitionException e){fail("echec d'edition d'un commentaire à modifier");}
        assertEquals(Review.NEW,review.getEtat());
    }
    @Test
    public void editerTest()
    {   review=new Review();
        try{
        review.reject();}catch (IllegalTransitionException e){fail("echec de rejet d'un commentaire");}
        try{
            review.edit();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertEquals(Review.REJECTED,review.getEtat());}

    }
    @Test
    public void keepOkTest()
    {   review=new Review();
        try{
        review.keepForEdit();}catch (IllegalTransitionException e){fail("echec de retetinr_pour_modif");}
        assertEquals(Review.TO_BE_MODIFIED, review.getEtat());
    }
    @Test
    public void keepKoTest()
    {   review=new Review();
        try{
        review.reject();}catch (IllegalTransitionException e){fail("echec de rejet d'un commentaire");}
        try{
            review.keepForEdit();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertEquals(Review.REJECTED,review.getEtat());}

    }


    @Test
    public void abandonOkTest()
    {   review=new Review();
        try{
        review.keepForEdit();}catch (IllegalTransitionException e){fail("echec de retenir pour modification");}
        try{
        review.abandon();}catch (IllegalTransitionException e){fail("echec d'abandon d'un commentaire" );}
        assertEquals(Review.ABANDONNED, review.getEtat());
    }
    @Test
    public void abandonKoTest()
    {   review=new Review();
        try{
            review.abandon();
            fail("exception expected");

        }catch (IllegalTransitionException e){assertEquals(Review.NEW,review.getEtat());}

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
