package com.cooperativa.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooperativa.web.entity.Cuenta;
import com.cooperativa.web.repository.CuentaRepository;

@Service
public class CuentaServiceImpl implements CuentaService{
	
	@Autowired
	private CuentaRepository cuentaR;

	@Override
	@Transactional(readOnly = true)
	public Optional<Cuenta> cuentaEncontrada(String cuenta) {
		Optional<Cuenta> cuentaEncontrada = cuentaR.findByNumeroCuenta(cuenta);
		return cuentaEncontrada;
	}

	@Override
	@Transactional(readOnly = true)
	public double obtenerMonto(String numCuenta) {
		Optional<Cuenta> cuentaEncontrada = cuentaR.findByNumeroCuenta(numCuenta);
		double monto = cuentaEncontrada.get().getMonto();
		return monto;
	}

	@Override
	@Transactional
	public void actualizarMonto(String numCuenta, double monto) {
		Optional<Cuenta> cuentaEncontrada = cuentaR.findByNumeroCuenta(numCuenta);
		Cuenta cuenta = cuentaEncontrada.get();
		cuenta.setMonto(monto);
		cuentaR.save(cuenta);
	}

	@Override
	@Transactional
	public void transferencia(String numCuenta,String numCuentaDos, double montoSumado, double montoRestado) {
		Optional<Cuenta> cuentaQueDa = cuentaR.findByNumeroCuenta(numCuenta);
		Optional<Cuenta> cuentaQueRecibe = cuentaR.findByNumeroCuenta(numCuentaDos);
		Cuenta cuentaDa = cuentaQueDa.get();
		Cuenta CuentaRecibe = cuentaQueRecibe.get();
		cuentaDa.setMonto(montoRestado);
		cuentaR.save(cuentaDa);
		CuentaRecibe.setMonto(montoSumado);
		cuentaR.save(CuentaRecibe);
	}
}
