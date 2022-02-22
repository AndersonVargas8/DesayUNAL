package com.example.desayunal.services;

import java.util.List;

import com.example.desayunal.interfacesServicios.IServicioOrden;
import com.example.desayunal.model.DetallesOrden;
import com.example.desayunal.model.Orden;
import com.example.desayunal.model.Producto;
import com.example.desayunal.model.Usuario;
import com.example.desayunal.repository.RepositorioDetallesOrden;
import com.example.desayunal.repository.RepositorioOrden;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioOrden implements IServicioOrden {
    @Autowired
    private RepositorioOrden data;

    @Autowired
    private RepositorioDetallesOrden repDetalles;

    @Override
    public int guardar(Orden o) {
        int res = 0;
        Orden orden = data.save(o);
        if(!orden.equals(null)){
            res = 1;
        }
        return res;
    }

    @Override
    public int guardarDetalles(DetallesOrden dO){
        int res = 0;
        DetallesOrden dOrden = repDetalles.save(dO);
        if(!dOrden.equals(null)){
            res = 1;
        }
        return res;
    }

    @Override
    public List<Orden> listarPorUsuario(Usuario usuario){
        return (List<Orden>) data.findByUsuario(usuario);
    }

    @Override
    public List<DetallesOrden> listarDetalles(Orden orden) {
        return (List<DetallesOrden>) repDetalles.findByOrdenID(orden);
    }

    @Override 
    public List<Orden> idsOrdenesPorMes(int mes){
        return (List<Orden>) data.idsByMes(mes);
    }

    @Override
    public int ventasProductoPorOrden(int idProducto, int idOrden){
        return repDetalles.timesSoldProductByOrdenId(idProducto, idOrden);
    }

    @Override
    public List<DetallesOrden> detallesOrden(Orden orden){
        return repDetalles.productsId(orden);
    }
    
    @Override
    public List<Orden> idsOrdenesPorFecha(int dia, int mes, int año){
        return data.idsByFecha(dia, mes, año);
    }
    
    @Override
    public List<Orden> idsOrdenesPorUsuario(Usuario usuario){
        return data.findByUsuario(usuario);
    }
    
    @Override
    public List<Integer[]> usuariosMasFrecuentes(){
        return data.usuariosMasFrecuentes();
    }

    @Override
    public int ventasMes(int anio, int mes){
    return data.ventasMes(anio, mes);
    }

    @Override
    public int ordenesPorProducto(Producto producto){
        return (int) repDetalles.ordenesPorProducto(producto);
    }

    @Override
    public int cantProductosVendidos() {
        return repDetalles.cantProductosVendidos();
    }

    @Override
    public int numOrdenes() {
        return repDetalles.numOrdenes();
    }

    @Override
    public int pedidosFranja(int mes, int limMin, int limMax) {
        // TODO Auto-generated method stub
        return data.pedidosFranja(mes, limMin, limMax);
    }

    @Override
    public List<Orden> ordenesDelMes(int mes, int anio) {
        return data.ordenesDelMes(mes, anio);
    }

    @Override
    public List<Orden> ordenesDelAnio(int anio) {
        return data.ordenesDelAnio(anio);
    }

    @Override
    public int ventasDia(int dia, int mes, int anio) {
        return data.ventasDia(dia, mes, anio);
    }

    @Override
    public int ventasAnio(int anio) {
        return data.ventasAnio(anio);
    }

    @Override
    public List<Integer[]> productoMasVendido(int mes, int anio) {
        return repDetalles.productoMasVendido(mes,anio);
    }

    

}

   

