

function abrirEtiqueta(evt, nombreEtiqueta) {
    var i, tabcontent, tablinks;

    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(nombreEtiqueta).style.display = "block";
    evt.currentTarget.className += " active";
}

//Al abrir la ventana de Login se ejecuta la funcion startTab
window.onload = function () {
    startTab();
}

//Función para que aparezca por defecto el contenido de la pestaña SIGN IN al abrir la página de Login
function startTab() {
    document.getElementById("defaultOpen").click();
}
