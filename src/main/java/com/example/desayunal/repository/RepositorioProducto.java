package com.example.desayunal.repository;

import java.util.List;

import com.example.desayunal.model.Producto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface    RepositorioProducto extends CrudRepository<Producto,Integer> {
    
    public List<Producto> findByCategoria(String categoria);

    public List<Producto> findByEstado(String estado);

    @Query(value = "SELECT * FROM producto WHERE estado = 'Disponible' OR estado = 'Oferta'", nativeQuery = true)
    public List<Producto> findDisponibles();

    @Query(value = "SELECT p FROM Producto p WHERE p.categoria = ?1 AND (p.estado = 'Disponible' OR p.estado = 'Oferta')")
    public List<Producto> findCategoriaDisponible(String categoria);

    @Query(value = "SELECT COUNT(p.id) FROM Producto p WHERE p.nombre = ?1 AND p.estado <> 'Eliminado'")
    public long productoExiste(String nombre);

    @Query(value = "SELECT COUNT(id) FROM Producto")
    public int totalProducts();

}
