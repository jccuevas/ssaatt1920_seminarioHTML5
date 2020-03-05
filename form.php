<!DOCTYPE html>
<html lang="es" dir="ltr">
    <head>
        <meta charset="utf-8">
        <title>Ejemplos seminario HTML5</title>
    </head>
    <body> 
        <?php
        
        if ($_GET == null) {
            echo "No hay parámetros por GET" . "<br>";
        } else {
            $valor = "";
            $clave = "";
            foreach ($_GET as $clave => $valor) {
                echo "Parámetro GET: " . $clave . " valor=" . $valor . "<br>";
            }
        }

        if ($_POST == null) {
            echo "No hay parámetros por POST" . "<br>";
        } else {
            $valor = "";
            $clave = "";
            foreach ($_POST as $clave => $valor) {
                echo "Parámetro POST: " . $clave . " valor=" . $valor . "<br>";
            }
        }
        ?>
    </body> 
</html> 


