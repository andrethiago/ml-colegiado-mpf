package br.mp.mpf.carga;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.mp.mpf.spring.AppConfig;

public class RecuperadorDadosProcedimentosColegiado {

	public static final String CAMINHO_PASTA_DATA = "/home/andrethiago/projetos/ml-colegiado-mpf/data/";

	public static final String ARQUIVO_PECAS_HOMOLOGACAO_ARQUIVAMENTO_JSON = CAMINHO_PASTA_DATA + "1A.CAM.pecas-homologacao-arquivamento.json";

	public static final String ARQUIVO_PROCEDIMENTOS_JSON = CAMINHO_PASTA_DATA + "1A.CAM.homologacao-arquivamento.json";

	protected static ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

	private RecuperaDadosProcedimentosColegiadoRepository repository;

	public RecuperadorDadosProcedimentosColegiado(RecuperaDadosProcedimentosColegiadoRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		RecuperadorDadosProcedimentosColegiado recuperador = new RecuperadorDadosProcedimentosColegiado(ctx.getBean(RecuperaDadosProcedimentosColegiadoRepository.class));
		recuperador.escreveProcedimentosDeliberadosEmArquivo();
	}

	private void escreveProcedimentosDeliberadosEmArquivo() {
		List<ProcedimentoDeliberadoColegiado> todos = new ArrayList<>();
		Set<ProcedimentoDeliberadoColegiado> procedimentos;
		Integer pagina = 1;

		try (BufferedWriter bwArquivoProcedimentos = new BufferedWriter(new FileWriter(ARQUIVO_PROCEDIMENTOS_JSON));
				BufferedWriter bwArquivoPecas = new BufferedWriter(new FileWriter(ARQUIVO_PECAS_HOMOLOGACAO_ARQUIVAMENTO_JSON));) {
			procedimentos = new HashSet<>(repository.consultaProcedimentos(pagina));
			while (CollectionUtils.isNotEmpty(procedimentos)) {
				System.out.println(procedimentos);
				configurarProvidenciasExecutadas(procedimentos);
				associarPecasPromocaoArquivamento(procedimentos);
				todos.addAll(procedimentos);
				pagina++;
				procedimentos = new HashSet<>(repository.consultaProcedimentos(pagina));
			}

			ObjectMapper mapper = new ObjectMapper();
			bwArquivoProcedimentos.write(mapper.writeValueAsString(todos));
			List<PecaPedidoColegiado> pecas = todos.stream().map(procedimento -> procedimento.getPecaPromocao()).collect(Collectors.toList());
			bwArquivoPecas.write(mapper.writeValueAsString(pecas));
			System.out.println("Acabou.");
		} catch (Exception e) {
			System.err.println("Erro ao escrever arquivo.");
			e.printStackTrace();
		}

	}

	private void associarPecasPromocaoArquivamento(Set<ProcedimentoDeliberadoColegiado> procedimentos) {
		List<PecaPedidoColegiado> pecas = repository.consultarPecasPromocaoArquivamento(procedimentos);

		for (ProcedimentoDeliberadoColegiado procedimento : procedimentos) {
			System.out.println();
			PecaPedidoColegiado pecaPedido = pecas.stream()
				.filter(peca -> peca.getIdDocumentoPrincipal().equals(procedimento.getId()) && peca.getDataCadastro().before(procedimento.getDataEntrada()))
				.findFirst()
				.orElse(new PecaPedidoColegiado());
			procedimento.setPecaPromocao(pecaPedido);
		}
	}

	private void configurarProvidenciasExecutadas(Set<ProcedimentoDeliberadoColegiado> procedimentos) {
		if (CollectionUtils.isNotEmpty(procedimentos)) {
			List<TipoProvidenciaTO> executadas = repository.consultarProvidenciasExecutadas(procedimentos);
			for (TipoProvidenciaTO executada : executadas) {
				List<ProcedimentoDeliberadoColegiado> procedimentosComProvidencias = encontraProcedimentos(procedimentos, executada);
				ordenarProcedimentos(procedimentosComProvidencias);

				Date dataEntradaAnterior = null;
				for (ProcedimentoDeliberadoColegiado procedimento : procedimentosComProvidencias) {
					if (dataEntradaAnterior != null) {
						if (executada.getData().after(dataEntradaAnterior) && executada.getData().before(procedimento.getDataEntrada())) {
							procedimento.adicionarProvidencia(executada.getNome());
						}
					} else if (executada.getData().before(procedimento.getDataEntrada())) {
						procedimento.adicionarProvidencia(executada.getNome());
					}
					dataEntradaAnterior = procedimento.getDataEntrada();
				}
			}
		}
	}

	private void ordenarProcedimentos(List<ProcedimentoDeliberadoColegiado> procedimentosComProvidencias) {
		procedimentosComProvidencias.sort(new Comparator<ProcedimentoDeliberadoColegiado>() {

			@Override
			public int compare(ProcedimentoDeliberadoColegiado o1, ProcedimentoDeliberadoColegiado o2) {
				return o1.getDataEntrada().compareTo(o2.getDataEntrada());
			}
		});
	}

	/*
	 * Pode retornar mais de um, pois, o procedimento pode ter mais de uma entrada no Colegiado.
	 */
	private List<ProcedimentoDeliberadoColegiado> encontraProcedimentos(Set<ProcedimentoDeliberadoColegiado> procedimentos, TipoProvidenciaTO executada) {
		return procedimentos.stream().filter(p -> p.getId().equals(executada.getIdDocumento())).collect(Collectors.toList());
	}

}
