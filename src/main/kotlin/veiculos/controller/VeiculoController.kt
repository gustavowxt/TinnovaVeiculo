package org.example.veiculos.controller

import org.example.veiculos.entity.Veiculo
import org.example.veiculos.service.VeiculoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/veiculos")
class VeiculoController(private val veiculoService: VeiculoService) {

    @GetMapping
    fun getAllVeiculos(
        @RequestParam(required = false) marca: String?,
        @RequestParam(required = false) ano: Int?
    ): ResponseEntity<List<Veiculo>> {
        return ResponseEntity.ok(veiculoService.findByFilters(marca, ano))
    }

    @GetMapping("/{id}")
    fun getVeiculoById(@PathVariable id: Long): ResponseEntity<Veiculo> {
        return veiculoService.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createVeiculo(@RequestBody veiculo: Veiculo): ResponseEntity<Any> {
        return try {
            val veiculoSalvo = veiculoService.save(veiculo)
            ResponseEntity.status(HttpStatus.CREATED).body(veiculoSalvo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateVeiculo(@PathVariable id: Long, @RequestBody veiculo: Veiculo): ResponseEntity<Any> {
        return try {
            veiculoService.update(id, veiculo)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PatchMapping("/{id}")
    fun partialUpdateVeiculo(@PathVariable id: Long, @RequestBody updates: Map<String, Any>): ResponseEntity<Any> {
        return try {
            veiculoService.partialUpdate(id, updates)?.let {
                ResponseEntity.ok(it)
            } ?: ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteVeiculo(@PathVariable id: Long): ResponseEntity<Void> {
        return if (veiculoService.delete(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/estatisticas/nao-vendidos")
    fun getNaoVendidos(): ResponseEntity<Map<String, Long>> {
        val quantidade = veiculoService.countNaoVendidos()
        return ResponseEntity.ok(mapOf("naoVendidos" to quantidade))
    }

    @GetMapping("/estatisticas/distribuicao-decada")
    fun getDistribuicaoDecada(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(veiculoService.distribuicaoPorDecada())
    }

    @GetMapping("/estatisticas/distribuicao-marca")
    fun getDistribuicaoMarca(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(veiculoService.distribuicaoPorMarca())
    }

    @GetMapping("/ultima-semana")
    fun getUltimaSemana(): ResponseEntity<List<Veiculo>> {
        return ResponseEntity.ok(veiculoService.findUltimaSemana())
    }

    @GetMapping("/marcas-validas")
    fun getMarcasValidas(): ResponseEntity<Set<String>> {
        return ResponseEntity.ok(veiculoService.getMarcasValidas())
    }
}