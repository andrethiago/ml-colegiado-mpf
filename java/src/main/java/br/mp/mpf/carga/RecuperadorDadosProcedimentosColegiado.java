package br.mp.mpf.carga;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.gson.Gson;

import br.mp.mpf.spring.AppConfig;

public class RecuperadorDadosProcedimentosColegiado {

	protected static ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
	protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate = ctx.getBean(NamedParameterJdbcTemplate.class);

	private RecuperaDadosProcedimentosColegiadoRepository repository;

	public RecuperadorDadosProcedimentosColegiado(RecuperaDadosProcedimentosColegiadoRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		RecuperadorDadosProcedimentosColegiado recuperador = new RecuperadorDadosProcedimentosColegiado(ctx.getBean(RecuperaDadosProcedimentosColegiadoRepository.class));
		recuperador.escreveProcedimentosDeliberadosEmArquivo();
	}

	private void escreveProcedimentosDeliberadosEmArquivo() {

		List<TipoProvidenciaTO> tipos = repository.consultarTodosTiposProvidenciasAtivos();

		List<ProcedimentoDeliberadoColegiado> todos = new ArrayList<>();
		Set<ProcedimentoDeliberadoColegiado> procedimentos;
		Integer pagina = 1;

		try (FileWriter fw = new FileWriter("/home/andrethiago/projetos/ml-colegiado-mpf/data/1A.CAM.homologacao-arquivamento.json"); BufferedWriter bw = new BufferedWriter(fw)) {
			do {
				procedimentos = new HashSet<>(repository.consultaProcedimentos(pagina));
				pagina++;
				configurarProvidenciasExecutadas(procedimentos, tipos);
				System.out.println(procedimentos);
				todos.addAll(procedimentos);
			} while (CollectionUtils.isNotEmpty(procedimentos));

			bw.write(new Gson().toJson(todos));

			System.out.println("Acabou.");
		} catch (Exception e) {
			System.err.println("Erro ao escrever arquivo.");
			e.printStackTrace();
		}

	}

	private void configurarProvidenciasExecutadas(Set<ProcedimentoDeliberadoColegiado> procedimentos, List<TipoProvidenciaTO> todosTipos) {
		if (CollectionUtils.isNotEmpty(procedimentos)) {

			for (ProcedimentoDeliberadoColegiado procedimento : procedimentos) {
				procedimento.setProvidenciasExecutadas(new ArrayList<>(Collections.nCopies(todosTipos.size(), 0L)));
			}

			List<TipoProvidenciaTO> executadas = repository.consultarProvidenciasExecutadas(procedimentos);
			for (TipoProvidenciaTO executada : executadas) {
				ProcedimentoDeliberadoColegiado procedimento = encontraProcedimento(procedimentos, executada);
				if (executada.getData().before(procedimento.getDataEntrada())) {
					int index = todosTipos.indexOf(executada);
					procedimento.getProvidenciasExecutadas().set(index, 1L);
				}
			}

		}
	}

	private ProcedimentoDeliberadoColegiado encontraProcedimento(Set<ProcedimentoDeliberadoColegiado> procedimentos, TipoProvidenciaTO executada) {
		return procedimentos.stream().filter(p -> p.getId().equals(executada.getIdDocumento())).findFirst().get();
	}

}
