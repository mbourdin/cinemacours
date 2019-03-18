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
    /*la fonction buildline doit etre redefinie pour chaque page selon le type de donnes recues*/
    function buildlines(result) {
        for (i = 0; i < result.content.length; i++) {
            film = result.content[i];
            buildline(film);
        }
    }
    /*l'identifiant body correspond au tbody de la table contenant les donnees*/
    function clearlines()
    { tablebody = document.getElementById("body");
        while(tablebody.rows.length>1)
        {   tablebody.deleteRow(1);
        }
    }
    function onSuccessPage(result) {
        console.log(result);
        clearlines();
        buildlines(result);
        $('#pagination').twbsPagination({
            totalPages: result.totalPages,
            visiblePages: 7,
            startPage: 1,
            onPageClick: function (event, page) {
                search(page-1,requestString);
                console.log(page)
            }
        });
    }