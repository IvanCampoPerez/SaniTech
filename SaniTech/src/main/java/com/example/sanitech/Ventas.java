package com.example.sanitech;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ventas {
    private final IntegerProperty VentaId = new SimpleIntegerProperty();
    private final IntegerProperty ClienteId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> FechaFactura = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> TotalNeto = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> TotalIVA = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> Total = new SimpleObjectProperty<>();

    public Ventas() {}

    public Ventas(int VentaId, int ClienteId, LocalDate FechaFactura, BigDecimal TotalNeto, BigDecimal TotalIVA, BigDecimal Total) {
        setVentaId(VentaId);
        setClienteId(ClienteId);
        setFechaFactura(FechaFactura);
        setTotalNeto(TotalNeto);
        setTotalIVA(TotalIVA);
        setTotal(Total);
    }

    public int getVentaId() {return VentaId.get();}

    public void setVentaId(int ventaId) {this.VentaId.set(ventaId);}

    public int getClienteId() {return ClienteId.get();}

    public void setClienteId(int clienteId) {this.ClienteId.set(clienteId);}

    public LocalDate getFechaFactura() {return FechaFactura.get();}

    public void setFechaFactura(LocalDate fechaFactura) {this.FechaFactura.set(fechaFactura);}

    public BigDecimal getTotalNeto() {return TotalNeto.get();}

    public void setTotalNeto(BigDecimal totalNeto) {this.TotalNeto.set(totalNeto);}

    public BigDecimal getTotalIVA() {return TotalIVA.get();}

    public void setTotalIVA(BigDecimal totalIVA) {this.TotalIVA.set(totalIVA);}

    public BigDecimal getTotal() {return Total.get();}

    public void setTotal(BigDecimal total) {this.Total.set(total);}
}
