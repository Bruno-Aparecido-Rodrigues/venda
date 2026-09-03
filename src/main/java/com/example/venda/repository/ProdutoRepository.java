package com.example.venda.repository;

import com.example.venda.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Modifying
    @Query("update Produto p set p.qtd = p.qtd - :quantidade where p.id = :id and p.qtd >= :quantidade")
    int baixarEstoqueAtomico(@Param("id") Long id, @Param("quantidade") Integer quantidade);
}