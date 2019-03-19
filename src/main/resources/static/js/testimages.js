tableau=["image1","image2","image3","image4","image5","image6"];
//au chargement du document
$(document).ready(
    function()
    {
        for(var i=0;i<tableau.length;i++)
        {   console.log("i="+i);
            var src="/images/"+tableau[i]+".jpg";
            chaine="<img src='"+src+"' width='200' heigth='150'>";
            $("#album").append(chaine);
        }
    }
)