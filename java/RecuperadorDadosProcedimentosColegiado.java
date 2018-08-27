package br.gov.mpf.unico.scriptsdb.autoadministrativo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
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
		recuperador.recuperarExpedientes();

	}

	private void recuperarExpedientes() {
		List<ExpedienteDeliberadoColegiado> todos = new ArrayList<>();
		Set<ExpedienteDeliberadoColegiado> expedientes;
		Integer pagina = 1;

		try (FileWriter fw = new FileWriter("/home/andrethiago/projetos/ml-colegiado-mpf/data/1A.CAM.homologacao-arquivamento.json"); BufferedWriter bw = new BufferedWriter(fw)) {
			do {
				expedientes = new HashSet<>(repository.consultaProcedimentos(pagina));
				pagina++;
				System.out.println(expedientes);
				todos.addAll(expedientes);
			} while (CollectionUtils.isNotEmpty(expedientes));

			bw.write(new Gson().toJson(todos));

			System.out.println("Acabou.");
		} catch (Exception e) {
			System.err.println("Erro ao escrever arquivo.");
			e.printStackTrace();
		}

	}

}
