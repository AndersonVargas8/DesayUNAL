package com.example.desayunal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.desayunal.model.DetallesOrden;
import com.example.desayunal.model.Orden;
import com.example.desayunal.model.Usuario;
import com.example.desayunal.services.ServicioOrden;
import com.example.desayunal.services.ServicioUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ControladorCompras {

    @Autowired
    ServicioOrden sOrden;

    @Autowired
    ServicioUsuario sUsuario;

    @RequestMapping("/misCompras")
    public String misCompras(Model model){
        if(!sUsuario.getEstadoLogin()){
            return "redirect:desayunal";
        }
        Usuario usuario = sUsuario.getUsuarioConectado();
        List<Orden> lCompras = sOrden.listarPorUsuario(usuario);
        ArrayList<List<DetallesOrden>> lDetalles = new ArrayList<>();
         
        Collections.reverse(lCompras);
        model.addAttribute("compras", lCompras);
        model = sUsuario.actualizarEstados(model);

        for(Orden o: lCompras){
            lDetalles.add(sOrden.listarDetalles(o));
        }

        model.addAttribute("detalles", lDetalles);
        return "misCompras";
    }
}


