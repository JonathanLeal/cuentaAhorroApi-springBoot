package com.cooperativa.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooperativa.web.entity.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long>{
	public Optional<Cuenta> findByNumeroCuenta(String numCuenta);
}
