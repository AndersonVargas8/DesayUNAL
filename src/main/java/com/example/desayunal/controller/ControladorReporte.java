package com.example.desayunal.controller;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.desayunal.services.ServicioProducto;
import com.example.desayunal.services.ServicioUsuario;
import com.example.desayunal.model.DetallesOrden;
import com.example.desayunal.model.Orden;
import com.example.desayunal.model.Producto;
import com.example.desayunal.model.Usuario;
import com.example.desayunal.services.ServicioOrden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControladorReporte {
    @Autowired
    private ServicioOrden servicioO;

    @Autowired
    private ServicioUsuario sUsuario;

    @Autowired
    private ServicioProducto servicioP;

    @Autowired
    private ServicioOrden sOrden;

    private int mayorVentaMes =0;
    private int mes;
    private int dia;
    private int anio;
    private String [] topUsuarios = {"a","b","c","d","e"};
    private int [] topOrdenes = {1,2,3,4,5};

    @GetMapping("/reporteCompras/{id}")
    public String reporteCompras(Model model, @PathVariable String id){
        String titulo = "", ventas = "";
        List<Orden> lCompras = new ArrayList<>(); ;
        ArrayList<List<DetallesOrden>> lDetalles = new ArrayList<>();
        String[] tipo = id.split("-");
        if(tipo.length == 3){ //yyyy-mm-dd
            int dia = Integer.parseInt(tipo[2]);
            int mes = Integer.parseInt(tipo[1]);
            int anio = Integer.parseInt(tipo[0]);
            lCompras = sOrden.idsOrdenesPorFecha(dia,mes,anio);
            try{
                ventas = " - " + NumberFormat.getCurrencyInstance().format(sOrden.ventasDia(dia, mes, anio));
            }catch(Exception e){

            }
            titulo = dia + "-" + convertirMes(mes) + "-" + anio;
        }else if(tipo.length == 2){ //yyyy-mm
            int mes = Integer.parseInt(tipo[1]);
            int anio = Integer.parseInt(tipo[0]);
            lCompras = sOrden.ordenesDelMes(mes, anio);
            try{
                ventas = " - " + NumberFormat.getCurrencyInstance().format(sOrden.ventasMes(anio, mes));
            }catch(Exception e){

            }
            titulo = convertirMes(mes) + "-" + anio;
        }else if(tipo.length == 1){ //yyyy
             int anio = Integer.parseInt(tipo[0]);
             lCompras = sOrden.ordenesDelAnio(anio);
             try{
                ventas = " - "+NumberFormat.getCurrencyInstance().format(sOrden.ventasAnio(anio));
            }catch(Exception e){

            }
             titulo = Integer.toString(anio);
        }

        Collections.reverse(lCompras);
        

        for(Orden o: lCompras){
            lDetalles.add(sOrden.listarDetalles(o));
        }
        model.addAttribute("compras", lCompras);
        model.addAttribute("titulo", titulo + ventas);
        model.addAttribute("detalles", lDetalles);
        return "reporteCompras";
    }

    @GetMapping("/reporte")
    public String reporte(Model model){
        if(!sUsuario.getEstadoLogin() || !sUsuario.getUsuarioConectado().getRole().equals("Administrador")){
            return "redirect:desayunal";
        }

        mes = obtenerMes();
        dia = obtenerDia();
        anio = obtenerAnio();
        
        /*Prueba de velocidad
        List<Double> porcentajes = new ArrayList<>();
        porcentajes.add(2.0);
        porcentajes.add(48.0);
        porcentajes.add(50.0);
        String[] ultimasCompras = {"1a","2b","3c","4d","5e"};*/
        List<Producto> productos = productosMasVendidosMes();
        String ingresoDia = NumberFormat.getCurrencyInstance().format(ingresosTotalesDia());
        String ingresoMes = NumberFormat.getCurrencyInstance().format(ingresosTotalesMes());
        List<Double> porcentajes = porcentajeCategoria();
        
        usuariosMasFrecuentes();
        String[] ultimasCompras = ultimaCompra();
        
        List<Orden> lCompras = sOrden.idsOrdenesPorFecha(dia, mes, anio);
        ArrayList<List<DetallesOrden>> lDetalles = new ArrayList<>();
         
        Collections.reverse(lCompras);
        model.addAttribute("compras", lCompras);

        for(Orden o: lCompras){
            lDetalles.add(sOrden.listarDetalles(o));
        }

        model.addAttribute("detalles", lDetalles);

        model.addAttribute("masVendido", productos.get(0));
        model.addAttribute("mayorVentas", mayorVentaMes);
        model.addAttribute("ingresoMes", ingresoMes);
        model.addAttribute("ingresoDia", ingresoDia);
        model.addAttribute("desayunos", porcentajes.get(0));
        model.addAttribute("onces", porcentajes.get(1));
        model.addAttribute("postres", porcentajes.get(2));
        model.addAttribute("usuarioNombre", topUsuarios);
        model.addAttribute("usuarioOrden", topOrdenes);
        model.addAttribute("usuarioHora", ultimasCompras);

        model.addAttribute("titulo",dia+"-"+convertirMes(mes)+"-"+anio+" - "+ingresoDia);

        //Las siguientes 2 lineas se utilizan para que se muestre correctamente la info en la barra de navegación
        model.addAttribute("page", "admin");
        model = sUsuario.actualizarEstados(model);
        


        return "reporte";
    }

    public String convertirMes(int mes){
        switch(mes){
            case 1:
                return "Enero";
                
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "";
        }
    }
    public int obtenerMes(){
        LocalDateTime fechaActual=LocalDateTime.now();
        int mes=fechaActual.getMonthValue();
        return mes;
    }

    public int obtenerDia(){
        LocalDateTime fechaActual = LocalDateTime.now();
        int dia = fechaActual.getDayOfMonth();

        return dia;
    }
    public int obtenerAnio(){
        LocalDateTime fechaActual = LocalDateTime.now();
        int anio = fechaActual.getYear();
        return anio;
    }
    public String[] ultimaCompra(){
        String [] ultimaVez = new String[5];
        for (int i=0;i<topUsuarios.length;i++){
            List<Usuario> usuarios = sUsuario.buscarUserName(topUsuarios[i]);
            List<Orden> ordenesDeUsuario= servicioO.idsOrdenesPorUsuario(usuarios.get(0));
            String fecha= ordenesDeUsuario.get(ordenesDeUsuario.size()-1).getDia() +"/"+
                            ordenesDeUsuario.get(ordenesDeUsuario.size()-1).getMes()+"/"+
                             ordenesDeUsuario.get(ordenesDeUsuario.size()-1).getAnio();
            ultimaVez[i]=fecha;
        }
        return ultimaVez;
    }

    /* Actualiza el arreglo topUsuarios en orden (indice 0 el mas frecuente) y actualiza el arreglo topOrdenes para acceder a la cantidad de ordenes
        realizada por cada usuario en el mismo orden*/
    public void usuariosMasFrecuentes(){
        Iterator<Integer[]> listaIterator = servicioO.usuariosMasFrecuentes().iterator();
        Integer[] ordenesYusuario;

        int index = 0;

        while(listaIterator.hasNext()){
            ordenesYusuario = listaIterator.next();
            topUsuarios[index] = sUsuario.usuarioPorId(ordenesYusuario[1]).getuserName();
            topOrdenes[index] = ordenesYusuario[0];

            ++index;
        }
    }

    public int ingresosTotalesMes(){
        
        
        return sOrden.ventasMes(anio, mes);
    }

    public int ingresosTotalesDia(){
        int ingresosTotales = 0;
        Iterator<Orden> ordenesIterator = servicioO.idsOrdenesPorFecha(dia, mes, anio).iterator();

        while(ordenesIterator.hasNext()){
            ingresosTotales += ordenesIterator.next().getPrecio();
        }

        return ingresosTotales;
    }


    public List<Double> porcentajeCategoria(){
        ArrayList<Double> porcentajes = new ArrayList<>()  ;
        double desayunos = 0;
        double postres = 0;
        double onces = 0;
        double total = 1;
        Iterator<Orden> ordenesIterator = servicioO.idsOrdenesPorMes(mes).iterator();
        Iterator<DetallesOrden> detallesOrdenIterator;
        Orden orden;
        DetallesOrden detallesOrden;
        while (ordenesIterator.hasNext()){
            orden=ordenesIterator.next();
            detallesOrdenIterator = servicioO.detallesOrden(orden).iterator();
            while(detallesOrdenIterator.hasNext()){
                detallesOrden=detallesOrdenIterator.next();
                if(  detallesOrden.getProductoID().getCategoria().equals("Desayuno")){
                    desayunos += detallesOrden.getCantidadProducto();
                }else if( detallesOrden.getProductoID().getCategoria().equals("Onces")){
                    onces += detallesOrden.getCantidadProducto();
                }else if (detallesOrden.getProductoID().getCategoria().equals("Postre")){
                    postres += detallesOrden.getCantidadProducto();
                }
            }
        }
        total=desayunos+onces+postres;

        porcentajes.add( (desayunos/total)*100);
        porcentajes.add( (onces/total)*100);
        porcentajes.add( (postres/total)*100);

        return porcentajes;
    }
    public List<Producto> productosMasVendidosMes(){
        /*Iterator<DetallesOrden> detallesOrdenIterator;
        List<Orden> ordenes = servicioO.idsOrdenesPorMes(mes);
        //System.out.println(ordenes.size());
        HashMap<Producto, Integer> productosTotal = new HashMap<Producto, Integer>();*/
        List<Producto> productosMasVendidos = new LinkedList<Producto>();

        /*Iterator<Orden> iterator = ordenes.iterator();
        Iterator<Producto> iteratorP;


        DetallesOrden detallesOrden;
        
        Producto producto;
        int cantidadProducto;
        Orden orden;
        
        int ventasT = 0;
        int ventaP;


        while(iterator.hasNext()){
            orden = iterator.next();

            detallesOrdenIterator = servicioO.detallesOrden(orden).iterator();

            while(detallesOrdenIterator.hasNext()){
                detallesOrden = detallesOrdenIterator.next();
                producto = detallesOrden.getProductoID();

                cantidadProducto = detallesOrden.getCantidadProducto();

                if(productosTotal.containsKey(producto)){
                    productosTotal.replace(producto, productosTotal.get(producto) + cantidadProducto);
                }
                else{
                    productosTotal.put(producto, cantidadProducto);
                }
            }   
        }

        iteratorP = productosTotal.keySet().iterator();

        while(iteratorP.hasNext()){
            producto = iteratorP.next();

            ventaP = productosTotal.get(producto);

            if(ventaP > ventasT){
                productosMasVendidos.clear();
                productosMasVendidos.add(producto);
                ventasT = ventaP;
                mayorVentaMes=ventaP;
            }
            else if(ventaP == ventasT){
                productosMasVendidos.add(producto);
            }
        }*/
        List<Integer[]> resultado = sOrden.productoMasVendido(mes,anio); 
        mayorVentaMes = resultado.get(0)[0]; 
        Producto producto = servicioP.listarId(resultado.get(0)[1]).get();
        

        productosMasVendidos.add(producto);
        return productosMasVendidos;
    }

    /**
     * Esta función permite proyectar las ventas del próximo mes
     * @param arregloMeses: arreglo con las ventas de los meses anteriores (se recomienda máximo 3)
     * @return double con el valor de las ventas proyectadas para el próximo mes
     */
    private double proyectar(ArrayList<Double> arregloMeses){
        int n = arregloMeses.size();
        ArrayList<Double> x = new ArrayList<>();
        for(double i = 0; i<n; i++){
            x.add(i);
        }
        double result = 0;
       
        
        
        for(int i = 0; i< n; i++){
            double valor = 1;

            for(int j = 0; j< n; j++){
                if(j!=i){
                    valor = valor * (n - x.get(j))/(x.get(i)-x.get(j));
                }
            }
            result = result + arregloMeses.get(i)*valor;
        }
        return result;
    }
}
