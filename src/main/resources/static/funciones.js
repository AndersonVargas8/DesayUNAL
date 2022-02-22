function eliminar(id){
    swal({
        title: "¿Está seguro de eliminar?",
        text: "Una vez eliminado, no podrá recuperar la información",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((OK) => {
            if (OK) {
                $.ajax({
                    url:"/eliminar/"+id,
                    success: function(res){
                        Console.log(res);
                    }
                });
                swal("El producto ha sido eliminado", {
                    icon: "success",
                }).then((ok)=>{
                    if(ok){
                        location.href="/listar";
                    }
                });
            } else {
            }
        });
}

function agregar(){
    swal("Agregado al carrito", {
        icon: "success",
    }).then((ok)=>{
        if(ok){
        }
    });
}

function compraExitosa(){
    swal("Compra exitosa!!!", {
        icon: "success",
    }).then((ok)=>{
        if(ok){
            parent.location.href="generarCompra";
        }else{       
            parent.location.href="generarCompra";
        }
    });

}

function loguearse(){
    swal("Debe iniciar sesión", {
        icon: "error",
    }).then((ok)=>{
        if(ok){
        }
    });
}


function carritoVacio(){
    swal("No hay productos en el carrito", {
        icon: "error",
    }).then((ok)=>{
        if(ok){
        }
    });
}

$(document).ready(function(){
    //Agregar producto al carrito
    $("div #addCarrito").click(function (){    
        agregar();
        var idp = $(this).parent().find("#idProducto").val();
        
        var url="agregarAlCarrito";
        $.ajax({
            type: 'POST',
            url: url,
            data: "idp="+idp,
            success: function(data, textStatus, jqXHR){
                $("#contCarrito").html('('+data+')');
            },
            error: function(data,textStatus,jqXHR){
                actContador();
            }
        })
    });

    //Actualiza el contador del carrito
    function actContador(){
        $.ajax({
            type: 'POST',
            url: "/actContador",
            processData: false,
            contentType: false,
            success: function(res){
                $("#contCarrito").html('('+res+')');
            },
            error:function(){
                $("#contCarrito").html('⦿');
            }
        })
        //document.getElementById('contCarrito').innerHTML = '+';
      
    }
    //Eliminar los elementos del carrito
    $("tr #btnDelete").click(function (){
    
        var idp = $(this).parent().find("#idp").val();
        swal({
            title: "¿Está seguro de eliminar?",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((OK) => {
                if (OK) {
                    eliminar(idp);
                    swal("El producto ha sido eliminado", {
                        icon: "success",
                    }).then((ok)=>{
                        if(ok){
                            parent.location.href="carrito";
                        }
                    });
                } else {
                }
            });

    });

    //Llama la función del controlador
    function eliminar(idp){
        var url="eliminarDelCarrito";
        $.ajax({
            type: 'POST',
            url: url,
            data: "idp="+idp,
            success: function(data, textStatus, jqXHR){
            }
        })
    }
    
    //Actualizar los totales en función de la cantidad en el carrito
    $("tr #cantidad").change(function(){
        var idp = $(this).parent().find("#idpro").val();
        var cantidad = $(this).parent().find("#cantidad").val();
        if(cantidad < 1){
            $(this).parent().find("#cantidad").val(1);
            cantidad = 1;
        }
        cantidad = parseInt(cantidad);
        $(this).parent().find("#cantidad").val(cantidad);
        
        var url = "actualizarCarrito";
        $.ajax({
            type: 'POST',
            url: url,
            data: "idp="+idp+"&cantidad="+cantidad,
            success: function(data, textStatus, jqXHR){
            }
        });

        
        
        var precio = $(this).parent().parent().find("#precio").val(); 
        var subtotal = $(this).parent().parent().find("#subtVal").val();
        var nSubtotal = precio* cantidad;

        $(this).parent().parent().find("#subtVal").val(nSubtotal);
        $(this).parent().parent().find("#subt").html('$'+nSubtotal);

        var total = parseInt(document.getElementById('totalVal').value) -subtotal + nSubtotal;

        document.getElementById('total').value = '$'+parseInt(total);       
        document.getElementById('totalVal').value = total;
    })

    function act(){
        $("tr #idpros").val(function(){
            return 3333;
        })
    }

    /*Modifica la cantidad de un producto del carrito
    $("tr #cantidad").change(function(){
        var idp = $(this).parent().find("#idpro").val();
        var cantidad = $(this).parent().find("#cantidad").val();
        var url = "actualizarCarrito";
        $.ajax({
            type: 'POST',
            url: url,
            data: "idp="+idp+"&cantidad="+cantidad,
            success: function(data, textStatus, jqXHR){
            }
        });
        
        parent.location.href="carrito";
    })*/

    
    $('.collapse').collapse();
});

function cancelarCompra(){
    swal({
        title: "¿Está seguro de cancelar la compra?",
        text: "El carrito será eliminado",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((OK) => {
            if (OK) {
                parent.location.href="vaciarCarrito";
            } else {
            }
        });
}
