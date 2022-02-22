package com.example.desayunal.services;

import java.util.List;
import java.util.Optional;

import com.example.desayunal.interfacesServicios.IServicioUsuario;
import com.example.desayunal.model.Usuario;
import com.example.desayunal.repository.RepositorioUsuario;
import com.example.desayunal.web.dto.RegistroUsuarioDto;

import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ServicioUsuario implements IServicioUsuario {

    private RepositorioUsuario repositorioUsuario;
    private boolean logueado;
    private Usuario usuarioConectado;

    public ServicioUsuario(RepositorioUsuario repositorioUsuario ){
        super();
        this.repositorioUsuario = repositorioUsuario;
        this.logueado = false;
    }

    public void actualizarEstadoLogin(boolean estado){
        this.logueado = estado;
    }

    public boolean getEstadoLogin(){
        return this.logueado;
    }

    public void actualizarUsuarioConectado(Usuario usuario){
        this.usuarioConectado = usuario;
    }

    public Usuario getUsuarioConectado(){
        return this.usuarioConectado;
    }
    @Override
    public Usuario save(RegistroUsuarioDto registroDto)
    {
        Usuario usuario = new Usuario
                              (
                                registroDto.getUserName(), 
                                registroDto.getPassword(), 
                                registroDto.getEstado(), 
                                registroDto.getRole()
                              );
        
        return repositorioUsuario.save(usuario);
    }

    @Override
    public String userPassword(String userName)
    {
        return repositorioUsuario.findByUserName(userName);
    }

    public Model actualizarEstados(Model model){
        model.addAttribute("logueado", getEstadoLogin());
        if(getUsuarioConectado() != null)
            model.addAttribute("usuarioConectado", getUsuarioConectado());
        else
            model.addAttribute("usuarioConectado", new RegistroUsuarioDto());
        
        return model;
    }

    @Override
    public Optional<Usuario> buscarId(int id) {
        return repositorioUsuario.findById(id);
    }

    @Override
    public List<Usuario> buscarUserName(String userName) {
        return repositorioUsuario.buscarUserName(userName);
    }

    @Override
    public List<Usuario> todosUsuarios(){
        return repositorioUsuario.allUsers();
    }

    @Override
    public Usuario usuarioPorId(int id){
        return repositorioUsuario.getById(id);
    }
    
}
