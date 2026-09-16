package com.example.venda;

import com.example.venda.entity.Produto;
import com.example.venda.service.EstoqueService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class VendaApplication {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(VendaApplication.class, args);

		EstoqueService estoqueService = context.getBean(EstoqueService.class);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		CompletableFuture<Produto> venda1 = CompletableFuture.supplyAsync(
				() -> estoqueService.baixarEstoque(2L, 5), executor);
		CompletableFuture<Produto> venda2 = CompletableFuture.supplyAsync(
				() -> estoqueService.baixarEstoque(2L, 5), executor);

		venda1.get();
		venda2.get();

		executor.shutdown();
	}

}