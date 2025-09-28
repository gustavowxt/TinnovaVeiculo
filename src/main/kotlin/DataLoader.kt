package org.example
import org.example.Veiculo
import org.example.VeiculoRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataLoader(private val veiculoRepository: VeiculoRepository) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (veiculoRepository.count() == 0L) {
            val veiculos = listOf(
                Veiculo(
                    veiculo = "Fiesta",
                    marca = "Ford",
                    ano = 2020,
                    descricao = "Carro compacto econômico",
                    vendido = false,
                    createdAt = LocalDateTime.now().minusDays(2)
                ),
                Veiculo(
                    veiculo = "Civic",
                    marca = "Honda",
                    ano = 2019,
                    descricao = "Sedan médio confiável",
                    vendido = true,
                    createdAt = LocalDateTime.now().minusDays(5)
                ),
                Veiculo(
                    veiculo = "Corolla",
                    marca = "Toyota",
                    ano = 2018,
                    descricao = "Sedan popular",
                    vendido = false,
                    createdAt = LocalDateTime.now().minusDays(1)
                )
            )

            veiculoRepository.saveAll(veiculos)
            println("Dados iniciais carregados!")
        }
    }
}