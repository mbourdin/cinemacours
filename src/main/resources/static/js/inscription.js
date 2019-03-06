$("#valider").click
(   function() {
        var resultat= estComplet();
        //console.log("complet=");
        //console.log(resultat);
        //recuperation des valeurs
        var email=$("#email").val();
        var email2=$("#email2").val();
        var password=$("#password").val();
        var password2=$("#password2").val();
        var emailOk = concordance(email, email2);
        var passOk = concordance(password, password2);
        if (!emailOk) {
            console.log("email pas ok");
            $("#email,#email2").addClass("redbackground");
        }
        if (!passOk) {
            console.log("password pas ok");
            $("#password,#password2").addClass("redbackground");
        }
        if (emailOk && passOk && resultat) {
            console.log("ok");
            $(".saisie").removeClass("redbackground").addClass("greenbackground");

        }
        ;
    }
);
//clic sur un des éléments de saisie
$(".saisie").focus
(
    function()
    {   $(this).removeClass("redbackground").removeClass("greenbackground");
    }
);
function concordance(champ1,champ2)
{   //console.log(champ1===champ2);
    return champ1===champ2;
};
function estComplet()
{
    var resultat = true;
    //parcours de tous les éléments de la classe 'saisie'.
    $(".saisie").each
    (
        function()
        {   $(this).removeClass("greenbackground");
            //on recupère la valuer de l'objet courant
            var valeur = $(this).val();
            if(valeur==null || valeur.length<2){
                resultat=false;
                $(this).addClass('redbackground');
            }
        }
    );
    return resultat;
}