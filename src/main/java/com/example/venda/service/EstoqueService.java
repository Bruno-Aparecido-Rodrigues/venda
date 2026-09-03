package com.example.venda.service;

import com.example.venda.entity.Produto;
import com.example.venda.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

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

    public Produto baixarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = consultarEstoquePorId(produtoId);

        if (produto.getQtd() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para o produto " + produto.getNome());
        }

        produto.setQtd(produto.getQtd() - quantidade);
        return produtoRepository.save(produto);
    }
}