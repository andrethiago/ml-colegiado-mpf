package br.gov.mpf.unico.scriptsdb.autoadministrativo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.google.gson.Gson;

public class RecuperadorDadosProcedimentosColegiado {

	protected static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-ScriptsDB.xml");
	protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) ctx.getBean(getDefaultName(NamedParameterJdbcTemplate.class));

	private RecuperaDadosProcedimentosColegiadoRepository repository;

	public RecuperadorDadosProcedimentosColegiado(RecuperaDadosProcedimentosColegiadoRepository repository) {
		this.repository = repository;
	}

	private String getDefaultName(Class<?> beanClass) {
		String name = beanClass.getSimpleName();
		return (name.charAt(0) + "").toLowerCase() + name.substring(1);
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

	private void configurarProvidenciasExecutadas(Set<ProcedimentoDeliberadoColegiado> procedimentos, List<TipoProvidenciaTO> tipos) {
		if (CollectionUtils.isNotEmpty(procedimentos)) {
			for (ProcedimentoDeliberadoColegiado procedimento : procedimentos) {
				List<Long> providencias = new ArrayList<>(Collections.nCopies(tipos.size(), 0L));
				List<TipoProvidenciaTO> executadas = repository.consultarProvidenciasExecutadas(procedimento);
				for (TipoProvidenciaTO executada : executadas) {
					int index = tipos.indexOf(executada);
					providencias.set(index, 1L);
				}
				procedimento.setProvidenciasExecutadas(providencias);
			}
		}
	}

}
