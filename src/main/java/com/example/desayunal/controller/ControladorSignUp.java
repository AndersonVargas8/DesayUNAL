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
@RequestMapping("/signUp")

public class ControladorSignUp {

    @Autowired
    private ServicioUsuario servicio;

    private boolean isPasswordWrong = false;


    @ModelAttribute("Usuario")
    public RegistroUsuarioDto registroUsuarioDto(){

        return new RegistroUsuarioDto();
    }

    @GetMapping
    public String showSignUp(Model registroDto) {

       /* registroDto.addAttribute("isPasswordWrong",isPasswordWrong); */
       if(servicio.getEstadoLogin()){
        return "redirect:desayunal";
    }
        return "signUp";

    }

    @PostMapping
    public String registroCuentaUsusario(@ModelAttribute("Usuario")RegistroUsuarioDto registroDto){
        registroDto.setEstado("Activo");
        registroDto.setRole("Cliente");
        
        String[] passwords = registroDto.getPassword().split(",");

        if(passwords.length < 2 || passwords[0].equals("") || registroDto.getUserName().equals(""))
        {
            return "redirect:signUp?invalidInputError";
        }

        String realPassword =  servicio.userPassword(registroDto.getUserName());
        
        if(realPassword == null)
        {
            if(passwords[0].equals(passwords[1]))
            {
                registroDto.setPassword(passwords[1]);
                servicio.save(registroDto);
                servicio.actualizarEstadoLogin(true);
                Usuario usuarioConectado = servicio.buscarUserName(registroDto.getUserName()).get(0);
                servicio.actualizarUsuarioConectado(usuarioConectado);
                return "redirect:desayunal";
            }
            
            /*isPasswordWrong = true;*/
            return "redirect:signUp?passwordError"; // las contaseñas no coinciden
        }

        /*isPasswordWrong = true;*/
        return "redirect:signUp?userError";// El nombre de usuario ya existe
    }
      
}
