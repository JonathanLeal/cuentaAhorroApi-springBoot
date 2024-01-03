package com.cooperativa.web.service;

import java.util.Optional;

import com.cooperativa.web.entity.Cuenta;

public interface CuentaService {
	public Optional<Cuenta> cuentaEncontrada(String cuenta);
	public double obtenerMonto(String numCuenta);
	public void actualizarMonto(String numCuenta, double monto);
	public void transferencia(String numCuentaUno, String numCuentaDos, double montoSumado, double montoRestado);
}
