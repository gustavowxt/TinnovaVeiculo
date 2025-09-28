package org.example

import org.example.Veiculo
import org.example.VeiculoRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VeiculoService(private val veiculoRepository: VeiculoRepository) {

    private val marcasValidas = setOf(
        "Ford", "Chevrolet", "Volkswagen", "Fiat", "Toyota",
        "Honda", "Hyundai", "Nissan", "Renault", "BMW",
        "Mercedes-Benz", "Audi", "Volvo", "Jeep", "Mitsubishi"
    )

    fun findAll(): List<Veiculo> = veiculoRepository.findAll()

    fun findById(id: Long): Veiculo? = veiculoRepository.findById(id).orElse(null)

    fun findByFilters(marca: String?, ano: Int?): List<Veiculo> {
        return when {
            marca != null && ano != null -> veiculoRepository.findByMarcaAndAno(marca, ano)
            marca != null -> veiculoRepository.findByMarca(marca)
            ano != null -> veiculoRepository.findByAno(ano)
            else -> findAll()
        }
    }

    fun save(veiculo: Veiculo): Veiculo {
        validarMarca(veiculo.marca)
        veiculo.updatedAt = LocalDateTime.now()
        return veiculoRepository.save(veiculo)
    }

    fun update(id: Long, veiculo: Veiculo): Veiculo? {
        return if (veiculoRepository.existsById(id)) {
            validarMarca(veiculo.marca)
            veiculo.id = id
            veiculo.updatedAt = LocalDateTime.now()
            veiculoRepository.save(veiculo)
        } else {
            null
        }
    }

    fun partialUpdate(id: Long, updates: Map<String, Any>): Veiculo? {
        return veiculoRepository.findById(id).map { veiculoExistente ->
            updates.forEach { (campo, valor) ->
                when (campo) {
                    "veiculo" -> veiculoExistente.veiculo = valor as String
                    "marca" -> {
                        validarMarca(valor as String)
                        veiculoExistente.marca = valor
                    }
                    "ano" -> veiculoExistente.ano = valor as Int
                    "descricao" -> veiculoExistente.descricao = valor as String
                    "vendido" -> veiculoExistente.vendido = valor as Boolean
                }
            }
            veiculoExistente.updatedAt = LocalDateTime.now()
            veiculoRepository.save(veiculoExistente)
        }.orElse(null)
    }

    fun delete(id: Long): Boolean {
        return if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun countNaoVendidos(): Long = veiculoRepository.countByVendidoFalse()

    fun distribuicaoPorDecada(): Map<String, Long> {
        return veiculoRepository.countByDecada()
            .associate { (decada, quantidade) ->
                "Década ${(decada as Double).toInt()}" to (quantidade as Long)
            }
    }

    fun distribuicaoPorMarca(): Map<String, Long> {
        return veiculoRepository.countByMarca()
            .associate { (marca, quantidade) ->
                marca as String to (quantidade as Long)
            }
    }

    fun findUltimaSemana(): List<Veiculo> {
        val umaSemanaAtras = LocalDateTime.now().minusWeeks(1)
        return veiculoRepository.findCreatedAfter(umaSemanaAtras)
    }

    private fun validarMarca(marca: String) {
        if (!marcasValidas.contains(marca)) {
            throw IllegalArgumentException("Marca '$marca' não é válida. Marcas válidas: ${marcasValidas.joinToString()}")
        }
    }

    fun getMarcasValidas(): Set<String> = marcasValidas
}