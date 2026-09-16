package com.example.venda.service;

import com.example.venda.entity.Produto;
import com.example.venda.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);

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
        String thread = Thread.currentThread().getName();

        logger.info("[{}] tentando adquirir lock para o produto {}", thread, produtoId);

        Produto produto = produtoRepository.findByIdComLock(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com id " + produtoId));

        logger.info("[{}] lock adquirido, estoque atual: {}", thread, produto.getQtd());

        if (produto.getQtd() < quantidade) {
            logger.info("[{}] estoque insuficiente ({} < {})", thread, produto.getQtd(), quantidade);
            throw new IllegalStateException("Estoque insuficiente para o produto " + produto.getNome());
        }

        produto.setQtd(produto.getQtd() - quantidade);
        Produto salvo = produtoRepository.save(produto);

        logger.info("[{}] venda concluída, novo estoque: {}", thread, salvo.getQtd());

        return salvo;
    }
}