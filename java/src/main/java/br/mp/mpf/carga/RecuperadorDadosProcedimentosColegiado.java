package br.mp.mpf.carga;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
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
				associarDocumentoResponsavelPelaSaida(procedimentos);
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

	private void associarDocumentoResponsavelPelaSaida(Set<ProcedimentoDeliberadoColegiado> procedimentos) {
		// TODO Auto-generated method stub

		/**
		 * 
		 * 
		 * SELECT dp.ID_DOCUMENTO AS "id", dp.ETIQUETA AS "procedimento", 
		tc.ID_TIPO_CLASSE_CNMP AS "id_classe", tc.SG_TIPO_CLASSE_CNMP "classe", 
		ar.ID_AREA_ATUACAO AS "id_area_atuacao", ar.DESCRICAO "area_atuacao", 
		i.ID_ITEM_CNMP AS "id_item", i.DS_ITEM AS "tema", 
		aa.DT_AUTUACAO AS "data_autuacao", g.DT_ENTRADA AS "data_entrada", 
		decode(dp.ST_URGENTE, 'S', 1, 0) "urgente", aa.ST_PRIORITARIO "prioritario",
		m.ID_MUNICIPIO "id_municipio", m.NOME "municipio",
		peca.ID_DOCUMENTO AS "peca",
		decode(a.DESCRICAO, 'Homologação de Arquivamento', 1, 0) "resultado"
		FROM unico.GERENCIA_ENTRADA_SAIDA g JOIN unico.DOCUMENTO dp ON g.ID_DOCUMENTO = dp.ID_DOCUMENTO
		JOIN unico.AUTO_ADMINISTRATIVO aa ON aa.ID_DOCUMENTO = dp.ID_DOCUMENTO
		JOIN unico.documento deci ON g.ID_DOCUMENTO_DECISAO = deci.ID_DOCUMENTO
		JOIN unico.ASSUNTO a ON a.ID_ASSUNTO = deci.ID_ASSUNTO
		JOIN unico.TIPO_CLASSE_CNMP tc ON tc.ID_TIPO_CLASSE_CNMP = aa.ID_TIPO_CLASSE_CNMP_ATUAL
		JOIN unico.AREA_ATUACAO ar ON aa.ID_AREA_ATUACAO = ar.ID_AREA_ATUACAO
		JOIN unico.ITEM_CNMP_DOCUMENTO icd ON icd.ID_DOCUMENTO = aa.ID_DOCUMENTO
		JOIN unico.ITEM_CNMP i ON icd.ID_ITEM_CNMP = i.ID_ITEM_CNMP
		LEFT JOIN unico.AUTO_ADMIN_LOCAL_FATO lf ON lf.ID_DOCUMENTO = dp.ID_DOCUMENTO
		LEFT JOIN CORPORATIVO.MUNICIPIO m ON m.ID_MUNICIPIO = lf.ID_MUNICIPIO
		JOIN unico.REFERENCIA_NOVA r ON r.ID_DOCUMENTO_PRINCIPAL = aa.ID_DOCUMENTO
		JOIN unico.documento peca ON peca.ID_DOCUMENTO = r.ID_DOCUMENTO_SECUNDARIO
		WHERE g.ID_CONCENTRADOR_SETOR_DESTINO = 2639550 
		AND dp.ID_GENERO = 10
		AND g.DT_SAIDA > TO_DATE('02/07/2018','dd/mm/yyyy')
		AND g.ID_TIPO_ENTRADA_DESTINO = 1
		AND icd.ST_TEMA_PRINCIPAL = 1
		AND EXISTS (
		SELECT 1 FROM 
		unico.PROVIDENCIA p, unico.ASSUNTO ap, unico.ASSUNTO_TIPO_ENTRADA ate,
		unico.TIPO_ENTRADA te
		WHERE (p.ID_TP_PROVIDENCIA = 702 or p.ID_TP_PROVIDENCIA = 6324524)
		AND p.ID_DOCUMENTO = aa.ID_DOCUMENTO
		AND peca.ID_GENERO = 41
		AND r.ID_TIPO_REFERENCIA = 1 
		AND te.ST_ATIVO = 1
		AND ap.ID_ASSUNTO = ate.ID_ASSUNTO
		AND te.ID_TIPO_ENTRADA = 1
		AND ate.ID_TIPO_ENTRADA = te.ID_TIPO_ENTRADA
		AND peca.ID_ASSUNTO IN  (
		SELECT ate.ID_ASSUNTO 
		FROM unico.ASSUNTO_TIPO_ENTRADA ate 
		WHERE ate.ID_TIPO_ENTRADA = 1
		)
		);
		
		 * 
		 */
	}

	private void configurarProvidenciasExecutadas(Set<ProcedimentoDeliberadoColegiado> procedimentos, List<TipoProvidenciaTO> todosTipos) {
		if (CollectionUtils.isNotEmpty(procedimentos)) {

			List<TipoProvidenciaTO> executadas = repository.consultarProvidenciasExecutadas(procedimentos);
			for (TipoProvidenciaTO executada : executadas) {
				ProcedimentoDeliberadoColegiado procedimento = encontraProcedimento(procedimentos, executada);
				if (executada.getData().before(procedimento.getDataEntrada())) {
					procedimento.adicionarProvidencia(executada.getNome());
				}
			}

		}
	}

	private ProcedimentoDeliberadoColegiado encontraProcedimento(Set<ProcedimentoDeliberadoColegiado> procedimentos, TipoProvidenciaTO executada) {
		return procedimentos.stream().filter(p -> p.getId().equals(executada.getIdDocumento())).findFirst().get();
	}

}
