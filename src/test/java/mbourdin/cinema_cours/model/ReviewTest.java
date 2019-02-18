package mbourdin.cinema_cours.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest {
    Review review;
    @Test
    public void noParamConstructorTest()
    {   review=new Review();
        assertEquals(null,review.getId());
        assertEquals(null,review.getFilm());
        assertEquals(null,review.getUtilisateur());
        assertEquals(null,review.getArticle());
        assertEquals(null,review.getDate());
        assertEquals(Review.NOUVEAU,review.getEtat());

    }
    @Test
    public void twoParamConstructorTest()
    {   Film film=new Film();
        Utilisateur utilisateur=new Utilisateur();
        review =new Review(film,utilisateur);
        assertEquals(null,review.getId());
        assertEquals(film,review.getFilm());
        assertEquals(utilisateur,review.getUtilisateur());
        assertEquals(null,review.getArticle());
        assertNotNull(review.getDate());
        assertEquals(Review.NOUVEAU,review.getEtat());

    }
    @Test
    public void publierOkTest()
    {   review=new Review();
        review.publier();
        assertEquals(Review.PUBLIE,review.getEtat());
    }
    @Test
    public void publierKoTest()
    {   review=new Review();
        review.publier();
        try{
        review.publier();
        fail("exception expected");

        }catch (IllegalStateException e){assertTrue(true);}

    }
    @Test
    public void supprimerOkTest()
    {   review=new Review();
        review.publier();
        review.supprimer();
        assertEquals(Review.SUPPRIME,review.getEtat());
    }
    @Test
    public void supprimerKoTest()
    {   review=new Review();
        try{
            review.supprimer();
            fail("exception expected");

        }catch (IllegalStateException e){assertTrue(true);}

    }

    @Test
    public void rejeterOkTest()
    {   review=new Review();
        review.rejeter();
        assertEquals(Review.REJETE,review.getEtat());
    }

    @Test
    public void rejeterKoTest()
    {   review=new Review();
        review.publier();

        try{
            review.rejeter();
            fail("exception expected");

        }catch (IllegalStateException e){assertTrue(true);}

    }


    @Test
    public void editerOkTest()
    {   review=new Review();
        review.editer();
        assertEquals(Review.NOUVEAU,review.getEtat());
        review.publier();
        review.editer();
        assertEquals(Review.NOUVEAU,review.getEtat());
        review.retenirPourModif();
        review.editer();
        assertEquals(Review.NOUVEAU,review.getEtat());
    }
    @Test
    public void editerKoTest()
    {   review=new Review();
        review.rejeter();
        try{
            review.editer();
            fail("exception expected");

        }catch (IllegalStateException e){assertTrue(true);}

    }
    @Test
    public void retenirPourModifOkTest()
    {   review=new Review();
        review.retenirPourModif();
        assertEquals(Review.AMODIFIER, review.getEtat());
    }
    @Test
    public void retenirPourModifKoTest()
    {   review=new Review();
        review.rejeter();
        try{
            review.retenirPourModif();
            fail("exception expected");

        }catch (IllegalStateException e){assertTrue(true);}

    }


    @Test
    public void abandonnerOkTest()
    {   review=new Review();
        review.retenirPourModif();
        review.abandonner();
        assertEquals(Review.ABANDONNE, review.getEtat());
    }
    @Test
    public void abandonnerKoTest()
    {   review=new Review();
        try{
            review.abandonner();
            fail("exception expected");

        }catch (IllegalStateException e){assertTrue(true);}

    }

}
