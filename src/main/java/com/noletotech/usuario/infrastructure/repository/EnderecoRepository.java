package com.noletotech.usuario.infrastructure.repository;

import com.noletotech.usuario.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository <Endereco, Long>{
}
