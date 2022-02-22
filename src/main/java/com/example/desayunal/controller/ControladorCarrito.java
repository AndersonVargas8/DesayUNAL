package com.example.desayunal.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import com.example.desayunal.model.Carrito;
import com.example.desayunal.model.DetallesOrden;
import com.example.desayunal.model.Orden;
import com.example.desayunal.model.Producto;
import com.example.desayunal.model.Usuario;
import com.example.desayunal.services.ServicioOrden;
import com.example.desayunal.services.ServicioProducto;
import com.example.desayunal.services.ServicioUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class ControladorCarrito {
    @Autowired
    private ServicioProducto sProducto;
    
    @Autowired 
    private ServicioUsuario sUsuario;

    @Autowired
    private ServicioOrden sOrden;

    private Stack<Carrito> listaCarrito = new Stack<>();
    private int item;
    private int totalPagar = 0;
    private int cantidad = 1;
    public static int cantidadCarrito = 0;
    private String usuario;
    
    @PostMapping("/agregarAlCarrito")
    public ResponseEntity<?> agregarAlCarrito(@RequestParam int idp){
        agregarCarrito(idp);
        return new ResponseEntity(cantidadCarrito,HttpStatus.OK);
    }

    @GetMapping("/agregarCarrito/{id}")
    public String agregarCarrito(@PathVariable int id){
        int pos = 0;
        cantidad = 1;
        Producto p = sProducto.listarId(id).get();
        

        if(!listaCarrito.empty()){
            for(int i = 0; i < listaCarrito.size(); i++){
                if(id == listaCarrito.get(i).getIdProducto()){
                    pos = i;
                }
            }
            if(id == listaCarrito.get(pos).getIdProducto()){
                cantidad += listaCarrito.get(pos).getCantidad();
                int subTotal = listaCarrito.get(pos).getPrecioCompra()*cantidad;
                listaCarrito.get(pos).setCantidad(cantidad);
                listaCarrito.get(pos).setSubTotal(subTotal);
            }else{
                Carrito car = new Carrito();
                car.setId(++item);
                car.setIdProducto(id);
                car.setNombre(p.getNombre());
                car.setDescripcion(p.getDescripcion());
                car.setPrecioCompra(p.getPrecio());
                car.setCantidad(cantidad);
                car.setSubTotal(cantidad*p.getPrecio());
                listaCarrito.add(car);
            }
        }else{
            Carrito car = new Carrito();
            car.setId(++item);
            car.setIdProducto(id);
            car.setNombre(p.getNombre());
            car.setDescripcion(p.getDescripcion());
            car.setPrecioCompra(p.getPrecio());
            car.setCantidad(cantidad);
            car.setSubTotal(cantidad*p.getPrecio());
            listaCarrito.add(car);
        }
        cantidadCarrito = listaCarrito.size();
       /* for(int i = 0 ; i < 10; i++)
            System.out.println(p.getNombre());*/
    
        return "redirect:../desayunal?agregado";
    }

    @GetMapping("/carrito")
    public String carrito(Model model){
        if(!sUsuario.getEstadoLogin()){
            vaciarCarrito();
            return "redirect:desayunal";
        }

        totalPagar = 0;
        for (Carrito c : listaCarrito){
            totalPagar += c.getSubTotal();
        }
        model.addAttribute("carrito", listaCarrito);
        model.addAttribute("totalPagar",totalPagar);
        model = sUsuario.actualizarEstados(model);
        return "carrito";   
    }

    @PostMapping("/eliminarDelCarrito")
    public void eliminarDelCarrito(@RequestParam int idp){
        for(int i = 0; i < listaCarrito.size(); i++){
            if(listaCarrito.get(i).getId() == idp)
                listaCarrito.remove(i);
        }
        actualizarLista();
        cantidadCarrito = listaCarrito.size();
    }

    @PostMapping("/actualizarCarrito")
    public void actualizarCarrito(@RequestParam int idp, @RequestParam int cantidad){
        for(int i = 0; i < listaCarrito.size(); i++){
            if(listaCarrito.get(i).getId() == idp){
                listaCarrito.get(i).setCantidad(cantidad);
                int subTotal = listaCarrito.get(i).getPrecioCompra()*cantidad;
                listaCarrito.get(i).setSubTotal(subTotal);
            }
        }
    }

    private void actualizarLista(){
        item = 1;
        for(Carrito c: listaCarrito){
            c.setId(item++);
        }
        item = listaCarrito.size();
    }

    @RequestMapping("/vaciarCarrito")
    public String vaciarCarrito(){
        listaCarrito.clear();
        item = 0;
        cantidadCarrito = 0;
        return "redirect:desayunal";
    }

    @RequestMapping("/generarCompra")
    public String generarCompra(Model model) {

        Usuario usuario = sUsuario.getUsuarioConectado();
        usuario = sUsuario.buscarUserName(usuario.getuserName()).get(0);
        totalPagar = 0;
        for (Carrito c : listaCarrito){
            totalPagar += c.getSubTotal();
        }
        String[] fecha = {"1","1","2022"};
        fecha = obtenerFecha().split("/");
        Orden orden = new Orden(Integer.valueOf(fecha[0]), Integer.valueOf(fecha[1]), Integer.valueOf(fecha[2]), obtenerHora(),obtenerMin(),"",totalPagar,"",usuario);
        int res = sOrden.guardar(orden);

        guardarDetalles(orden);
        vaciarCarrito();
        return "redirect:desayunal";
    }

    
    private void guardarDetalles(Orden orden){
        for(Carrito c: listaCarrito){
            Producto producto = sProducto.listarId(c.getIdProducto()).get();
            DetallesOrden dOrden = new DetallesOrden(orden,producto,c.getCantidad(),c.getSubTotal());
            int res = sOrden.guardarDetalles(dOrden);  
        }
        
    }
    
    private String obtenerFecha(){
        Calendar calendario = Calendar.getInstance();
        String dia = completar(Integer.toString(calendario.get(Calendar.DATE)));
        String mes = completar(Integer.toString(calendario.get(Calendar.MONTH) % 12 + 1));
        String annio = Integer.toString(calendario.get(Calendar.YEAR));
        
        return dia.concat("/" + mes + "/" + annio);
    }
    
    private int obtenerHora(){
        Calendar calendario = Calendar.getInstance();
        int hora = (calendario.get(Calendar.HOUR) + 12*calendario.get(Calendar.AM_PM ));
        //String minuto = completar(Integer.toString(calendario.get(Calendar.MINUTE)));
        //String segundo = completar(Integer.toString(calendario.get(Calendar.SECOND)));
        //return hora.concat(":" + minuto + ":" + segundo);
        return hora;
    }
    
    private String obtenerMin() {
        Calendar calendario = Calendar.getInstance();
        int minuto = (calendario.get(Calendar.MINUTE) );
        String strMin = minuto < 10 ? "0"+Integer.toString(minuto) : Integer.toString(minuto);
        return strMin;
    }

    private String completar(String dato){
        if(dato.length() == 1)
            dato = "0".concat(dato);

        return dato;
    }

    @PostMapping("/actContador")
    public ResponseEntity<?> actContador(){
        cantidadCarrito = listaCarrito.size();
        return new ResponseEntity(cantidadCarrito,HttpStatus.OK);
    }
}
