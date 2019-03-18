MAXCOMMENTLENGTH=1500;
MINCOMMENTLENGTH=1;
$("#article").keyup
(
    function ()
    { var texte=$("#article").val();
        if(texte.lenght<MINCOMMENTLENGTH||texte.length>MAXCOMMENTLENGTH)
        {   $("#button").prop("disabled",true);
        }
        else
        {   $("#button").prop("disabled",false);
        }
        console.log(texte);
    }
);