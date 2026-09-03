package com.example.venda.controller;

import com.example.venda.entity.Produto;
import com.example.venda.service.EstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venda")
public class VendaController {

    private final EstoqueService estoqueService;

    public VendaController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public ResponseEntity<Produto> realizarVenda(@RequestBody VendaRequest request) {
        Produto produtoAtualizado = estoqueService.baixarEstoque(request.produtoId(), request.quantidade());
        return ResponseEntity.ok(produtoAtualizado);
    }

    @GetMapping("/estoque")
    public ResponseEntity<List<Produto>> consultarEstoque() {
        return ResponseEntity.ok(estoqueService.consultarEstoque());
    }

    @GetMapping("/estoque/{id}")
    public ResponseEntity<Produto> consultarEstoquePorId(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueService.consultarEstoquePorId(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNaoEncontrado(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleEstoqueInsuficiente(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    public record VendaRequest(Long produtoId, Integer quantidade) {}
}