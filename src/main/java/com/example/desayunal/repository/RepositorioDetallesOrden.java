package com.example.desayunal.repository;

import java.util.List;

import com.example.desayunal.model.DetallesOrden;
import com.example.desayunal.model.Orden;
import com.example.desayunal.model.Producto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RepositorioDetallesOrden extends CrudRepository<DetallesOrden,Integer>{
    public List<DetallesOrden> findByOrdenID(Orden usuario);

    @Query(value = "SELECT cantidadProducto FROM DetallesOrden WHERE ordenID = ?2 AND productoID = ?1")
    public int timesSoldProductByOrdenId(int idProducto, int idOrden);

    @Query(value = "SELECT p FROM DetallesOrden p WHERE p.ordenID = ?1")
    public List<DetallesOrden>  productsId(Orden Orden);

    @Query(value = "SELECT COUNT(o.id) FROM DetallesOrden o WHERE o.productoID = ?1")
    public long ordenesPorProducto(Producto producto);

    @Query(value = "SELECT SUM(cantidad_producto) FROM detalles_orden", nativeQuery = true)
    public int cantProductosVendidos();

    @Query(value="SELECT COUNT(*) FROM ORDEN",nativeQuery = true)
    public int numOrdenes();

    @Query(value = "SELECT SUM(d.cantidad_producto) suma, d.fk_productoid FROM detalles_orden d INNER JOIN Orden ON Orden.id = d.fk_ordenid WHERE Orden.mes = ?1 AND Orden.anio = ?2 GROUP BY d.fk_productoid ORDER BY suma DESC limit 1",nativeQuery = true)
    public List<Integer[]> productoMasVendido(int mes, int anio);
}
