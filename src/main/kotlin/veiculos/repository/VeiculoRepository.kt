package org.example.veiculos.repository

import org.example.veiculos.entity.Veiculo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface VeiculoRepository : JpaRepository<Veiculo, Long> {

    fun findByMarca(marca: String): List<Veiculo>
    fun findByAno(ano: Int): List<Veiculo>
    fun findByVendido(vendido: Boolean): List<Veiculo>

    @Query("SELECT v FROM Veiculo v WHERE v.marca = :marca AND v.ano = :ano")
    fun findByMarcaAndAno(@Param("marca") marca: String, @Param("ano") ano: Int): List<Veiculo>

    @Query("SELECT COUNT(v) FROM Veiculo v WHERE v.vendido = false")
    fun countByVendidoFalse(): Long

    @Query("SELECT FLOOR(v.ano / 10.0) * 10 as decada, COUNT(v) as quantidade FROM Veiculo v GROUP BY FLOOR(v.ano / 10.0) * 10 ORDER BY decada")
    fun countByDecada(): List<Array<Any>>

    @Query("SELECT v.marca, COUNT(v) as quantidade FROM Veiculo v GROUP BY v.marca ORDER BY quantidade DESC")
    fun countByMarca(): List<Array<Any>>

    @Query("SELECT v FROM Veiculo v WHERE v.createdAt >= :dataInicio")
    fun findCreatedAfter(@Param("dataInicio") dataInicio: LocalDateTime): List<Veiculo>
}