<?php

if ($_GET == null) {
    echo "No hay parámetros por GET" . "<br>";

} else {
    $valor = "";
    $clave = "";
    foreach ($_GET as $clave => $valor) {
        echo "Parámetro GET " . $clave . " valor=" . $valor . "<br>";
    }
}

if ($_POST == null) {
    echo "No hay parámetros por POST" . "<br>";

} else {
    $valor = "";
    $clave = "";
    foreach ($_POST as $clave => $valor) {
        echo "Parámetro POST" . $clave . " valor=" . $valor . "<br>";
    }
}


