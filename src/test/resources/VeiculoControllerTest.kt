import kotlin.jvm.java
import kotlin.text.trimIndent

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VeiculoControllerTest {

    @org.springframework.beans.factory.annotation.Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `testar todos os endpoints`() {
        kotlin.io.println("=== TESTANDO GET /api/veiculos ===")
        val response1 = restTemplate.getForEntity<String>("/api/veiculos")
        kotlin.io.println("Status: ${response1.statusCode}")
        kotlin.io.println("Body: ${response1.body}")

        kotlin.io.println("\n=== TESTANDO POST /api/veiculos ===")
        val novoVeiculo = """
            {
                "veiculo": "Onix",
                "marca": "Chevrolet",
                "ano": 2023,
                "descricao": "Carro popular",
                "vendido": false
            }
        """.trimIndent()

        val response2 = restTemplate.postForEntity<String>("/api/veiculos", novoVeiculo, String::class.java)
        kotlin.io.println("Status: ${response2.statusCode}")
        kotlin.io.println("Body: ${response2.body}")

        kotlin.io.println("\n=== TESTANDO ESTATÍSTICAS ===")
        val statsResponse = restTemplate.getForEntity<String>("/api/veiculos/estatisticas/nao-vendidos")
        kotlin.io.println("Não vendidos: ${statsResponse.body}")
    }
}