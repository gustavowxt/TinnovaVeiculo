package org.example.veiculos.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "veiculos")
data class Veiculo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var veiculo: String = "",

    @Column(nullable = false)
    var marca: String = "",

    @Column(nullable = false)
    var ano: Int = 0,

    @Column(nullable = false)
    var descricao: String = "",

    @Column(nullable = false)
    var vendido: Boolean = false,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)