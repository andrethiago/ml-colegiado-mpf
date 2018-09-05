package br.mp.mpf.carga;

import static br.mp.mpf.carga.RecuperadorDadosProcedimentosColegiado.ARQUIVO_PECAS_HOMOLOGACAO_ARQUIVAMENTO_JSON;
import static br.mp.mpf.carga.RecuperadorDadosProcedimentosColegiado.CAMINHO_PASTA_DATA;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.mp.mpf.spring.AppConfig;

public class RecuperadorTextoIntegra {

	protected static ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

	private RecuperadorTextoIntegraRepository repository;

	public RecuperadorTextoIntegra(RecuperadorTextoIntegraRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) throws IOException {
		RecuperadorTextoIntegra recuperador = new RecuperadorTextoIntegra(ctx.getBean(RecuperadorTextoIntegraRepository.class));
		recuperador.recuperar();
	}

	private void recuperar() throws IOException {
		List<PecaPedidoColegiado> pecas = lerArquivo();
		escreveArquivos(pecas);
	}

	private List<PecaPedidoColegiado> lerArquivo() throws IOException {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(ARQUIVO_PECAS_HOMOLOGACAO_ARQUIVAMENTO_JSON))) {
			String linha = "";
			while ((linha = br.readLine()) != null) {
				builder.append(linha);
			}

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(builder.toString(), new TypeReference<List<PecaPedidoColegiado>>() {});
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void escreveArquivos(List<PecaPedidoColegiado> pecas) {
		pecas.forEach(peca -> {
			if (peca.getIntegra() != null) {
				ArquivoIntegra integra = repository.consultarArquivoIntegra(peca);
				System.out.println(integra.getNomeArquivo());
				try (OutputStream fos =
					new FileOutputStream(CAMINHO_PASTA_DATA + "integras/" + peca.getIdDocumentoPrincipal() + "-" + peca.getIntegra() + "." + integra.getExtensao())) {
					byte[] buff = integra.getBytesConteudo();
					int length = buff.length;
					fos.write(buff, 0, length);
				} catch (IOException e) {
					System.err.println("Erro ao escrever conteúdo do arquivo " + integra.getNomeArquivo());
					e.printStackTrace();
				}
			}
		});
	}

}
