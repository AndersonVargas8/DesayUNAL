package com.example.desayunal.interfacesServicios;

import java.util.List;
import java.util.Optional;

import com.example.desayunal.model.Usuario;
import com.example.desayunal.web.dto.RegistroUsuarioDto;

public interface IServicioUsuario {

    Usuario save(RegistroUsuarioDto registroDto);
    String userPassword(String userName);
    
    public Optional<Usuario> buscarId(int id);
    public List<Usuario> buscarUserName(String userName);
    public List<Usuario> todosUsuarios();
    public Usuario usuarioPorId(int id);
}
