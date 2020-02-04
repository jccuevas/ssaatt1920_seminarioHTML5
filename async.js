/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


let contenedor = document.getElementById("contenedor");
let p = document.createElement("p");
p.innerHTML = "Soy un párrafo que no se mostrará porque el script se ha cargado con el atributo async";
p.setAttribute("id", "parrafo");
contenedor.appendChild(p);