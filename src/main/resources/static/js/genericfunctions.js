$('.ui.dropdown.item')
    .dropdown();

    function setLang(lang) {
        var currentUrl=window.location.toString();
        if(currentUrl.includes("lang="))
        {   currentUrl=currentUrl.substring(0,currentUrl.length-2);
        }
        else if(!currentUrl.includes("?"))
        {   currentUrl=currentUrl+"?lang=";
        }
        else
        {   currentUrl=currentUrl+"&lang=";
        }
        currentUrl=currentUrl+lang;
        window.location=currentUrl;
    }

    function onError(jqXHR, textStatus, errorThrown) {
        console.log('jqXHR:');
        console.log(jqXHR);
        console.log("message d'erreur: " + jqXHR.responseJSON.message);
        console.log('textStatus:');
        console.log(textStatus);
        console.log('errorThrown:');
        console.log(errorThrown);
        alert("Echec de l'opération\n" + jqXHR.responseJSON.message);
    }
    /*l'identifiant body correspond au tbody de la table contenant les donnees*/

