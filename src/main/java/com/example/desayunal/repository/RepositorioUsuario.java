package com.example.desayunal.repository;

import java.util.List;
import java.util.Optional;

import com.example.desayunal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Integer> 
{
    @Query(value = "SELECT password FROM Usuario WHERE userName = ?1")
    public String findByUserName(String userName);

    @Query(value = "SELECT u FROM Usuario u WHERE u.userName = ?1")
    public List<Usuario> buscarUserName(String userName);

    @Query(value = "SELECT u FROM Usuario u")
    public List<Usuario> allUsers();

    

}
