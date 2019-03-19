var parameter="";
var requestString="all";
dosearch();
function dosearch()
{   rs=document.getElementById("requestTypeId");
    if(rs.value!==undefined)
    {   requestString=rs.value;
    }
    par=document.getElementById("parameter");
    if(
        (par.value!==undefined ) &&
        (requestString!=="all")
    )
    {   parameter="?str="+par.value;
    }
    search(0,requestString);
}
function search(page,requestType) {
    var film = $('#search').val();
    $.ajax({
        //url: '/api/film/bypage/' + film+'/'+page,
        url: '/api/film/'+requestType+'/'+page+parameter,
        type: 'get',
        dataType: 'json',
        contentType: 'application/json',
        success: onSuccessPage,
        error: onError
    });
}




function buildline(film) {

    tablebody = document.getElementById("body");

    tr0 = body.firstChild.nextSibling;
    tr = tr0.cloneNode(3);
    tr.removeAttribute("hidden");

    tablebody.appendChild(tr);
    td=tr.firstChild.nextSibling;

    a=td.firstChild;

    if(a!=null) {
        a.setAttribute("href", "/film/detail/" + film.id);
        a.text = film.titre;

    }
    td=td.nextSibling.nextSibling;
    console.log("td=");
    console.log(td);
    if(td!=null) {
        b = td.firstChild.nextSibling;
        if (b != null) {
            b.setAttribute("href", "/film/delete/" + film.id);
        }

    }
    td=td.nextSibling.nextSibling
    if(td!=null) {
        c = td.firstChild.nextSibling;
        if (c != null) {

            c.setAttribute("href", "/film/mod/" + film.id);

        }
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
            onPageClick: function (event,page) {
                search(page-1,requestString);
                console.log(page)
            }
        });
    }
function clearlines()
{ tablebody = document.getElementById("body");
    while(tablebody.rows.length>1)
    {   console.log(tablebody.rows.length);
        tablebody.deleteRow(1);
    }
}

function buildlines(result) {
    for (i = 0; i < result.content.length; i++) {
        film = result.content[i];
        buildline(film);
    }
}
