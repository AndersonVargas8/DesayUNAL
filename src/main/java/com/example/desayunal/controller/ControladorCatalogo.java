package com.example.desayunal.controller;



import java.util.List;

import com.example.desayunal.model.Producto;
import com.example.desayunal.services.ServicioProducto;
import com.example.desayunal.services.ServicioUsuario;
import com.example.desayunal.web.dto.RegistroUsuarioDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping
public class ControladorCatalogo{
    @Autowired
    private ServicioUsuario sUsuario;

    @Autowired
    private ServicioProducto sProducto;

    @Autowired
    private JdbcTemplate jdbc;

    @GetMapping("/desayunal")
    public String listarDisponibles(Model model){
        return listarDisponibles(model, "todos");
    }



    @RequestMapping("/desayunal")
    public String listarDisponibles(Model model, @RequestParam String filtro){
        List<Producto> productos = sProducto.listarDisponibles();
        String nLista = "Todos los productos";

        switch(filtro){
            case "ofertas":
                productos = sProducto.listarEstado("Oferta");
                nLista = "Ofertas";
                break;
            case "desayunos":
                productos = sProducto.listarCategoriaDisponible("Desayuno");
                nLista = "Desayunos";
                break;
            case "postres":
                productos  = sProducto.listarCategoriaDisponible("Postre");
                nLista = "Postres";
                break;
            case "onces":
                productos = sProducto.listarCategoriaDisponible("Onces");
                nLista = "Onces";
                break;
        }

        model.addAttribute("productos",productos);
        model.addAttribute("nLista", nLista);
        model.addAttribute("page", "catalogo");
        model = sUsuario.actualizarEstados(model);
        model.addAttribute("contador", ControladorCarrito.cantidadCarrito);
        return "catalogo";
    }
    
    @GetMapping("/logout")
    public String logout(Model model){
        sUsuario.actualizarEstadoLogin(false);
        return "redirect:vaciarCarrito";
    }



}