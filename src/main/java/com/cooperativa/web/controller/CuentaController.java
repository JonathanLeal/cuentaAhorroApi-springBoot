package com.cooperativa.web.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cooperativa.web.entity.Cuenta;
import com.cooperativa.web.service.CuentaService;

@RestController
public class CuentaController {

	@Autowired
	private CuentaService cuentaSer;
	
	@PostMapping("/cuentaEncontrada")
	public ResponseEntity<?> encontrarCuenta(@RequestBody HashMap<String, Object> request){
		HashMap<String, String> respuesta = new HashMap<String, String>();
		try {
			String numCuenta = (String) request.get("numCuenta");
			Optional<Cuenta> cuentaEncontrada = cuentaSer.cuentaEncontrada(numCuenta);
			if (numCuenta == null || numCuenta.isEmpty() || numCuenta.isBlank()) {
				respuesta.put("validacion", "Es obligatorio el numero de cuenta");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
			} else if (cuentaEncontrada.isEmpty()) {
				respuesta.put("validacion", "No se encontro la cuenta");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(cuentaEncontrada.get());
		} catch (Exception e) {
			respuesta.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	@PostMapping("/actualizarMonto")
	public ResponseEntity<?> ingresarDinero(@RequestBody HashMap<String, Object> request){
		HashMap<String, String> respuesta = new HashMap<String, String>();
		try {
			String numCuenta = (String) request.get("numCuenta");
			Optional<Cuenta> cuentaEncontrada = cuentaSer.cuentaEncontrada(numCuenta);
			if (numCuenta == null || numCuenta.isEmpty() || numCuenta.isBlank()) {
				respuesta.put("validacion", "Es obligatorio el numero de cuenta");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
			} else if (cuentaEncontrada.isEmpty()) {
				respuesta.put("validacion", "No se encontro la cuenta");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			double montoActual = cuentaSer.obtenerMonto(numCuenta);
			double montoEntrante = (double) request.get("dineroEntrante");
			if (montoEntrante <= 0) {
				respuesta.put("validacion", "No se puede ingresar una cantidad menor o igual a 0");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			double nuevoMonto = montoActual + montoEntrante;
			cuentaSer.actualizarMonto(numCuenta, nuevoMonto);
			
			respuesta.put("mensaje", "Monto actualizado con exito");
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	@PostMapping("/retirarMonto")
	public ResponseEntity<?> retirarDinero(@RequestBody HashMap<String, Object> request){
		HashMap<String, String> respuesta = new HashMap<String, String>();
		try {
			String numCuenta = (String) request.get("numCuenta");
			Optional<Cuenta> cuentaEncontrada = cuentaSer.cuentaEncontrada(numCuenta);
			if (numCuenta == null || numCuenta.isEmpty() || numCuenta.isBlank()) {
				respuesta.put("validacion", "Es obligatorio el numero de cuenta");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
			} else if (cuentaEncontrada.isEmpty()) {
				respuesta.put("validacion", "No se encontro la cuenta");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			double montoActual = cuentaSer.obtenerMonto(numCuenta);
			double montoEntrante = (double) request.get("dineroEntrante");
			if (montoEntrante <= 0) {
				respuesta.put("validacion", "No se puede ingresar una cantidad menor o igual a 0");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			} else if (montoEntrante > montoActual) {
				respuesta.put("validacion", "no puede retirar mas dinero del que posee");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
			}
			
			double nuevoMonto = montoActual - montoEntrante;
			cuentaSer.actualizarMonto(numCuenta, nuevoMonto);
			
			respuesta.put("mensaje", "Monto retirado con exito");
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	@PostMapping("/transaccion")
	public ResponseEntity<?> transaccion(@RequestBody HashMap<String, Object> request){
		HashMap<String, String> respuesta = new HashMap<String, String>();
		try {
			String numCuentaDa = (String) request.get("numCuentaDa");
			Optional<Cuenta> cuentaDaEncontrada = cuentaSer.cuentaEncontrada(numCuentaDa);
			if (cuentaDaEncontrada == null || cuentaDaEncontrada.isEmpty()) {
				respuesta.put("validacion", "Es obligatorio el numero de cuenta que dara el dinero");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
			} else if (cuentaDaEncontrada.isEmpty()) {
				respuesta.put("validacion", "No se encontro la cuenta que dara el dinero");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			String numCuentaRecibe = (String) request.get("numCuentaRecibe");
			Optional<Cuenta> cuentaRecibeEncontrada = cuentaSer.cuentaEncontrada(numCuentaRecibe);
			if (cuentaRecibeEncontrada == null || cuentaRecibeEncontrada.isEmpty()) {
				respuesta.put("validacion", "Es obligatorio el numero de cuenta que recibira el dinero");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
			} else if (cuentaRecibeEncontrada.isEmpty()) {
				respuesta.put("validacion", "No se encontro la cuenta que recibira el dinero");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			double montoAdar = (double) request.get("montoDad");
			double montoActual = cuentaSer.obtenerMonto(numCuentaDa);
			if (montoAdar > montoActual) {
				respuesta.put("validacion", "No se puede dar mas dinero del que se tiene");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			double montoActualCuentaRecibe = cuentaSer.obtenerMonto(numCuentaRecibe);
			double nuevoMontoCuentaRecibe = montoActualCuentaRecibe + montoAdar;
			double nuevoMontoCuentaDa = montoActual - montoAdar;
			
			cuentaSer.transferencia(numCuentaDa, numCuentaRecibe, nuevoMontoCuentaRecibe, nuevoMontoCuentaDa);
			respuesta.put("mensaje", "Transaccion realizada con exito");
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
			
		} catch (Exception e) {
			respuesta.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
}
