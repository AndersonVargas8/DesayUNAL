package com.example.desayunal.controller;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.sql.rowset.serial.SerialClob;

import com.example.desayunal.services.ServicioOrden;
import com.example.desayunal.services.ServicioProducto;
import com.example.desayunal.services.ServicioUsuario;

import org.apache.catalina.users.SparseUserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorAnalisis{
    @Autowired
    private ServicioOrden servicioOrden;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioProducto servicioProducto;
    @Autowired
    private ServicioUsuario sUsuario;

    private int mes;

    @GetMapping("/analisis")
    public String analisis( Model model){
        if(!sUsuario.getEstadoLogin() || !sUsuario.getUsuarioConectado().getRole().equals("Administrador")){
            return "redirect:desayunal";
        }
        int [] ventas20 = ventasAño(2020);
        int [] ventas21 = ventasAño(2021);
        int [] ventas22 = ventasAño(2022);
        mes=obtenerMes();
        model.addAttribute("ventas20",ventas20);
        model.addAttribute("ventas21",ventas21);
        model.addAttribute("ventas22",ventas22);


        // Promedio producto por compra
        int numOrdenes = servicioOrden.numOrdenes();
        int cantProductosVendidos = servicioOrden.cantProductosVendidos();
        double ppp = cantProductosVendidos/numOrdenes;

        model.addAttribute("promedio",ppp);

        //Tendencias hora de pedido por franjas
        int [] pedidosFranja = pedidoFranjaArreglo();
        model.addAttribute("pedidosFranja",pedidosFranja);

        //proyeccion
        double []ingresosMesesAnteriores = new double[mesesAnteriores().size()];
        ArrayList<Double> sesem = mesesAnteriores();
        ingresosMesesAnteriores[0]=sesem.get(0);
        ingresosMesesAnteriores[1]=sesem.get(1);
        ingresosMesesAnteriores[2]=sesem.get(2);
        model.addAttribute("ingresosMesesAnteriores",ingresosMesesAnteriores);
        model.addAttribute("proyeccion",proyectar(mesesAnteriores()));
        model.addAttribute("fechaAnterior",averiguarMesyAnio());

        //Las siguientes 2 lineas se utilizan para que se muestre correctamente la info en la barra de navegación
        model.addAttribute("page", "admin");
        model = sUsuario.actualizarEstados(model);

        return  "analisis";
    }

    public int obtenerMes(){
        LocalDateTime fechaActual=LocalDateTime.now();
        int mes=fechaActual.getMonthValue();
        return mes;
    }
    public int[] ventasAño(int anio){

        int [] ventas = new int[12]; 

        for(int i=0;i<12;i++){

            try {
                ventas[i] = servicioOrden.ventasMes(anio, i + 1);

            }catch (Exception e){
                ventas[i] = 0;
            }
            System.out.println("las ventas son " + ventas[i]);
        }
        mesesAnteriores();
        return ventas;
    }
    
    public int[] pedidoFranjaArreglo(){
        int [] cantPedidosFranja = new int[4];
        int i = 6;
        int j =0;
        while(i<=18){
            cantPedidosFranja[j] = servicioOrden.pedidosFranja(mes, i, i+3);
            j++;
            i+=4;
        }
        return cantPedidosFranja;
    }
    /**
     * Esta función permite proyectar las ventas del próximo mes
     * @param arregloMeses: arreglo con las ventas de los meses anteriores (se recomienda máximo 3)
     * @return double con el valor de las ventas proyectadas para el próximo mes
     */
    private double proyectar(ArrayList<Double> arregloMeses){
        int n = arregloMeses.size();
        ArrayList<Double> x = new ArrayList<>();
        for(double i = 0; i<n; i++){
            x.add(i);
        }
        double result = 0;



        for(int i = 0; i< n; i++){
            double valor = 1;

            for(int j = 0; j< n; j++){
                if(j!=i){
                    valor = valor * (n - x.get(j))/(x.get(i)-x.get(j));
                }
            }
            result = result + arregloMeses.get(i)*valor;
        }
        return result;
    }
    public String[] averiguarMesyAnio(){
        LocalDateTime dia = LocalDateTime.now();
        dia.minusMonths(3);
        int[] mesesAnteriores = new int[3];
        mesesAnteriores[0]= dia.minusMonths(3).getMonthValue();
        mesesAnteriores[1]= dia.minusMonths(2).getMonthValue();
        mesesAnteriores[2]= dia.minusMonths(1).getMonthValue();
        int[] aniosMesesAnteriores = new int[3];
        aniosMesesAnteriores[0]=dia.minusMonths(3).getYear();
        aniosMesesAnteriores[1]=dia.minusMonths(2).getYear();
        aniosMesesAnteriores[2]=dia.minusMonths(1).getYear();

        String [] fechaAnterior = new String[4] ;
        for (int i = 0; i < 3; i++) {
            fechaAnterior[i]=mesesAnteriores[i]+" "+aniosMesesAnteriores[i];
        }
        fechaAnterior[3]=dia.getMonth()+" "+dia.getYear();
        return fechaAnterior;
    }
    public ArrayList<Double> mesesAnteriores(){
        LocalDateTime dia = LocalDateTime.now();
        dia.minusMonths(3);
        int[] mesesAnteriores = new int[3];
        mesesAnteriores[0]= dia.minusMonths(3).getMonthValue();
        mesesAnteriores[1]= dia.minusMonths(2).getMonthValue();
        mesesAnteriores[2]= dia.minusMonths(1).getMonthValue();
        int[] aniosMesesAnteriores = new int[3];
        aniosMesesAnteriores[0]=dia.minusMonths(3).getYear();
        aniosMesesAnteriores[1]=dia.minusMonths(2).getYear();
        aniosMesesAnteriores[2]=dia.minusMonths(1).getYear();

        ArrayList<Double> ventasMesesAnteriores = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int ventas;
            try {
                ventas = servicioOrden.ventasMes(aniosMesesAnteriores[i],mesesAnteriores[i]);
            }catch (Exception e){
                ventas = 0;
            }
            ventasMesesAnteriores.add((double)ventas);
        }
        return ventasMesesAnteriores;

    }

}

