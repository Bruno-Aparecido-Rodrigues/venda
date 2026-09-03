package com.example.venda.service;

import com.example.venda.entity.Produto;
import com.example.venda.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;

    public EstoqueService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> consultarEstoque() {
        return produtoRepository.findAll();
    }

    public Produto consultarEstoquePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com id " + id));
    }

    @Transactional
    public Produto baixarEstoque(Long produtoId, Integer quantidade) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Venda interrompida", e);
        }

        int linhasAfetadas = produtoRepository.baixarEstoqueAtomico(produtoId, quantidade);

        if (linhasAfetadas == 0) {
            if (produtoRepository.findById(produtoId).isEmpty()) {
                throw new IllegalArgumentException("Produto não encontrado com id " + produtoId);
            }
            throw new IllegalStateException("Estoque insuficiente para o produto de id " + produtoId);
        }

        return produtoRepository.findById(produtoId).orElseThrow();
    }
}