package br.mp.mpf.carga;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class RecuperaDadosProcedimentosColegiadoRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	List<ProcedimentoDeliberadoColegiado> consultaProcedimentos(Integer pagina) {
		StringBuilder select = new StringBuilder();
		select.append("SELECT * FROM ( SELECT a.*, rownum r__ FROM ( ");
		select.append("SELECT dp.ID_DOCUMENTO AS \"id\", dp.ETIQUETA AS \"procedimento\", ");
		select.append("tc.ID_TIPO_CLASSE_CNMP AS \"classe\", ");
		select.append("ar.ID_AREA_ATUACAO AS \"areaAtuacao\", ");
		select.append("i.ID_ITEM_CNMP AS \"itemCnmp\", ");
		select.append("aa.DT_AUTUACAO AS \"dataAutuacao\", g.DT_ENTRADA AS \"dataEntrada\", ");
		select.append("decode(dp.ST_URGENTE, 'S', 1, 0) \"urgente\", aa.ST_PRIORITARIO \"prioritario\",");
		select.append("m.ID_MUNICIPIO \"municipio\", ");
		select.append("(SELECT count(p.id_providencia) ");
		select.append("		FROM unico.PROVIDENCIA p ");
		select.append("		WHERE p.ID_DOCUMENTO = aa.ID_DOCUMENTO");
		select.append("		AND p.ID_TP_PROVIDENCIA <> 702 AND p.ID_TP_PROVIDENCIA <> 6324524");
		select.append("		AND p.DT_PROVIDENCIA < g.DT_ENTRADA");
		select.append(") as \"quantidadeProvidencias\", ");
		select.append("(SELECT count(c.ID_CONTROLE_PRAZO_FINALIZACAO) ");
		select.append("		FROM unico.CONTROLE_PRAZO_FINALIZACAO c ");
		select.append("		WHERE c.ID_DOCUMENTO = aa.ID_DOCUMENTO ");
		select.append("		AND c.ID_TIPO_ACAO_DOCUMENTO <> 1");
		select.append(") as \"quantidadeConversoes\", ");
		select.append("decode(a.DESCRICAO, 'Homologação de Arquivamento', 1, 0) \"homologado\" ");
		select.append("FROM unico.GERENCIA_ENTRADA_SAIDA g JOIN unico.DOCUMENTO dp ON g.ID_DOCUMENTO = dp.ID_DOCUMENTO ");
		select.append("JOIN unico.AUTO_ADMINISTRATIVO aa ON aa.ID_DOCUMENTO = dp.ID_DOCUMENTO ");
		select.append("JOIN unico.DOCUMENTO deci ON g.ID_DOCUMENTO_DECISAO = deci.ID_DOCUMENTO ");
		select.append("JOIN unico.ASSUNTO a ON a.ID_ASSUNTO = deci.ID_ASSUNTO ");
		select.append("JOIN unico.TIPO_CLASSE_CNMP tc ON tc.ID_TIPO_CLASSE_CNMP = aa.ID_TIPO_CLASSE_CNMP_ATUAL ");
		select.append("JOIN unico.AREA_ATUACAO ar ON aa.ID_AREA_ATUACAO = ar.ID_AREA_ATUACAO ");
		select.append("JOIN unico.ITEM_CNMP_DOCUMENTO icd ON icd.ID_DOCUMENTO = aa.ID_DOCUMENTO ");
		select.append("JOIN unico.ITEM_CNMP i ON icd.ID_ITEM_CNMP = i.ID_ITEM_CNMP  ");
		select.append("LEFT JOIN unico.AUTO_ADMIN_LOCAL_FATO lf ON lf.ID_DOCUMENTO = dp.ID_DOCUMENTO ");
		select.append("LEFT JOIN CORPORATIVO.MUNICIPIO m ON m.ID_MUNICIPIO = lf.ID_MUNICIPIO ");
		select.append("WHERE g.ID_CONCENTRADOR_SETOR_DESTINO = 2639550 ");
		select.append("AND dp.ID_GENERO = 10 ");
		select.append("AND g.DT_SAIDA > TO_DATE('02/07/2018','dd/mm/yyyy') ");
		select.append("AND g.ID_TIPO_ENTRADA_DESTINO = 1 ");
		select.append("AND icd.ST_TEMA_PRINCIPAL = 1 ");
		select.append("ORDER BY g.DT_ENTRADA desc ");
		select.append(") a WHERE rownum < ((:pagina * 100) + 1 ) ) WHERE r__ >= (((:pagina-1) * 100) + 1)");

		Map<String, Object> params = new HashMap<>();
		params.put("pagina", pagina);

		return namedParameterJdbcTemplate.query(select.toString(), params, new BeanPropertyRowMapper<ProcedimentoDeliberadoColegiado>(ProcedimentoDeliberadoColegiado.class));
	}

	List<TipoProvidenciaTO> consultarTodosTiposProvidenciasAtivos() {
		StringBuilder select = new StringBuilder();
		select.append("SELECT tp.ID_TP_PROVIDENCIA AS \"id\", tp.NM_TP_PROVIDENCIA AS \"nome\" ");
		select.append("FROM unico.TP_PROVIDENCIA tp ");
		select.append("WHERE tp.ID_GENERO = 10 AND tp.ST_ATIVO = 1 ");
		select.append("ORDER BY tp.NM_TP_PROVIDENCIA ");

		return namedParameterJdbcTemplate.query(select.toString(), new HashMap<String, Object>(), new BeanPropertyRowMapper<TipoProvidenciaTO>(TipoProvidenciaTO.class));
	}

	List<TipoProvidenciaTO> consultarProvidenciasExecutadas(Set<ProcedimentoDeliberadoColegiado> procedimentos) {
		StringBuilder select = new StringBuilder();
		select.append("SELECT DISTINCT tp.ID_TP_PROVIDENCIA AS \"id\", tp.NM_TP_PROVIDENCIA AS \"nome\",  ");
		select.append("p.DT_PROVIDENCIA as \"data\", p.ID_DOCUMENTO as \"idDocumento\" ");
		select.append("FROM unico.TP_PROVIDENCIA tp JOIN unico.PROVIDENCIA p ON tp.ID_TP_PROVIDENCIA = p.ID_TP_PROVIDENCIA ");
		select.append("WHERE tp.ID_GENERO = 10 AND tp.ST_ATIVO = 1 AND  p.ID_DOCUMENTO in (:listaDocumentos) ");
		select.append("AND p.ID_TP_PROVIDENCIA <> 702 AND p.ID_TP_PROVIDENCIA <> 6324524 ");
		select.append("ORDER BY p.ID_DOCUMENTO ");

		List<Long> ids = getIdentificadores(procedimentos);

		Map<String, Object> params = new HashMap<>();
		params.put("listaDocumentos", ids);

		return namedParameterJdbcTemplate.query(select.toString(), params, new BeanPropertyRowMapper<TipoProvidenciaTO>(TipoProvidenciaTO.class));
	}

	public List<PecaPedidoColegiado> consultarPecasPromocaoArquivamento(Set<ProcedimentoDeliberadoColegiado> procedimentos) {
		List<Long> ids = getIdentificadores(procedimentos);

		StringBuilder select = new StringBuilder();

		select.append("SELECT d.ID_DOCUMENTO as \"id\", r.ID_DOCUMENTO_PRINCIPAL \"idDocumentoPrincipal\", ");
		select.append("vm.ID_VINCULO \"membroResponsavel\", d.DT_CADASTRO \"dataCadastro\", i.ID_INTEGRA \"integra\" ");
		select.append("FROM unico.documento d JOIN unico.REFERENCIA_NOVA r ON d.ID_DOCUMENTO = r.ID_DOCUMENTO_SECUNDARIO ");
		select.append("LEFT JOIN unico.RESPONSAVEL_ASSINATURA ra ON ra.ID_DOCUMENTO = d.ID_DOCUMENTO ");
		select.append("LEFT JOIN unico.INTEGRA i ON i.ID_DOCUMENTO = d.ID_DOCUMENTO ");
		select.append("JOIN corporativo.vinculo vm ON vm.ID_VINCULO = ra.ID_VINCULO_RESP_ASSINATURA ");
		select.append("JOIN unico.ASSUNTO a ON a.ID_ASSUNTO = d.ID_ASSUNTO ");
		select.append("LEFT JOIN unico.ASSUNTO_TIPO_ENTRADA ate ON ate.ID_ASSUNTO = a.ID_ASSUNTO ");
		select.append("JOIN unico.TIPO_ENTRADA te ON ate.ID_TIPO_ENTRADA = te.ID_TIPO_ENTRADA ");
		select.append("WHERE r.ID_TIPO_REFERENCIA = 1  ");
		select.append("AND d.ID_GENERO = 41 ");
		select.append("AND te.ST_ATIVO = 1 ");
		select.append("AND i.ID_TP_CATEGORIA_INTEGRA in (0,1) ");
		select.append("AND te.ID_TIPO_ENTRADA = 1 ");
		select.append("AND r.ID_DOCUMENTO_PRINCIPAL IN (:procedimentos) ");
		select.append("AND vm.ID_TIPO_VINCULO = 10 ");

		Map<String, Object> params = new HashMap<>();
		params.put("procedimentos", ids);

		return namedParameterJdbcTemplate.query(select.toString(), params, new BeanPropertyRowMapper<PecaPedidoColegiado>(PecaPedidoColegiado.class));

	}

	private List<Long> getIdentificadores(Set<ProcedimentoDeliberadoColegiado> procedimentos) {
		return procedimentos.stream().map(p -> p.getId()).collect(Collectors.toList());
	}

}
