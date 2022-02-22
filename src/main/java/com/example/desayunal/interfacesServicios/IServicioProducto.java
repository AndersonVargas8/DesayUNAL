package com.example.desayunal.interfacesServicios;

import com.example.desayunal.model.Producto;

import java.util.List;
import java.util.Optional;

public interface IServicioProducto {
    public List<Producto> listar();
    public Optional<Producto> listarId(int id);
    public List<Producto> listarDisponibles();
    public List<Producto> listarEstado(String estado);
    public List<Producto> listarCategoria(String categoria);
    public List<Producto> listarCategoriaDisponible(String categoria);
    public int guardar(Producto p, boolean editando);
    public void eliminar(int id);
    public boolean productoExiste(String nombre);
    public int  totalProductos();
}
