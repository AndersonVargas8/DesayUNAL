package com.example.desayunal.controller;

import com.example.desayunal.model.Usuario;
import com.example.desayunal.services.ServicioUsuario;
import com.example.desayunal.web.dto.RegistroUsuarioDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class ControladorLogin {

    @Autowired
    private ServicioUsuario servicio;

    @ModelAttribute("Usuario")
    public RegistroUsuarioDto registroUsuarioDto(){

        return new RegistroUsuarioDto();
    }

    @GetMapping
    public String showLogin(String userName, String password, Model model) {
        if(servicio.getEstadoLogin()){
            return "redirect:desayunal";
        }
        return "login";
    }

    @PostMapping
    public String loggear(@ModelAttribute("Usuario")RegistroUsuarioDto registroDto){
        String realPassword =  servicio.userPassword(registroDto.getUserName());

        if(registroDto.getPassword().equals("") || registroDto.getUserName().equals(""))
        {
            return "redirect:login?invalidInputError";
        }
        
        if(realPassword != null)
        {
            if(realPassword.equals(registroDto.getPassword())) 
            {
                Usuario usuarioConectado = servicio.buscarUserName(registroDto.getUserName()).get(0);
                servicio.actualizarEstadoLogin(true);
                servicio.actualizarUsuarioConectado(usuarioConectado);
                return "redirect:desayunal";
            }

            return "redirect:login?userError"; // No coinciden el usuario y la contraseña
        }
        
        return "redirect:login?noUser"; // No existe el usuario
    }
    
    

}