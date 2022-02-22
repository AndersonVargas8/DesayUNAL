package com.example.desayunal.interfacesServicios;

import java.util.List;

import com.example.desayunal.model.DetallesOrden;
import com.example.desayunal.model.Orden;
import com.example.desayunal.model.Producto;
import com.example.desayunal.model.Usuario;

public interface IServicioOrden  {
    public int guardar(Orden o);
    public int guardarDetalles(DetallesOrden dO);
    public List<Orden> listarPorUsuario(Usuario usuario);
    public List<DetallesOrden> listarDetalles(Orden orden);
    public List<Orden> idsOrdenesPorMes(int mes);
    public List<Orden> idsOrdenesPorFecha(int dia, int mes, int año);
    public List<Orden> ordenesDelMes(int mes, int anio);
    public List<Orden> ordenesDelAnio(int anio);
    public int ventasProductoPorOrden(int idProducto, int idOrden);
    public List<DetallesOrden> detallesOrden(Orden orden);
    public List<Orden> idsOrdenesPorUsuario(Usuario usuario);
    public List<Integer[]> usuariosMasFrecuentes();
    public int ventasMes(int anio, int mes);
    public int ventasDia(int dia, int mes, int anio);
    public int ventasAnio(int anio);
    public int ordenesPorProducto(Producto producto);
    public int cantProductosVendidos();
    public int numOrdenes();
    public int pedidosFranja(int mes,int limMin, int limMax);

    public List<Integer[]> productoMasVendido(int mes, int anio);

}
