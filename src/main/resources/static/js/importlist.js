var str=document.getElementById("str").value;
function dosearch()
{
    search(1);
}
function search(page) {
    str=document.getElementById("str").value;
    $.ajax({
        url: 'https://api.themoviedb.org/3/search/movie?api_key=3ef450de0d048793a243988121b65901&language=fr-FR&query='+str+'&page='+page+'&include_adult=false',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json',
        success: onSuccessPage,
        error: onError
    });
}
function clearlines()
{ tablebody = document.getElementById("body");
    while(tablebody.rows.length>2)
    {   console.log(tablebody.rows.length);
        tablebody.deleteRow(1);
    }
}
function buildlines(result) {
    for (i = 0; i < result.results.length; i++) {
        film = result.results[i];
        buildline(film);
    }
}

function buildline(film) {
    body=document.getElementById("body");
    tr=document.createElement("tr");
    td=document.createElement("td");
    a=document.createElement("a");
    a.text=film.id+' '+film.title;
    a.setAttribute("href","/film/smallDetail/"+film.id)
    body.appendChild(tr);
    tr.appendChild(td);
    td.appendChild(a)
}
function onSuccessPage(result) {
    console.log(result);
    clearlines();
    buildlines(result);
    $('#pagination').twbsPagination({
        totalPages: 50,
        visiblePages: 7,
        startPage: 1,
        onPageClick: function (event, page) {
            search(page,str);
            console.log(page)
        }
    });
}