package br.mp.mpf.carga;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class RecuperadorTextoIntegraRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public ArquivoIntegra consultarArquivoIntegra(PecaPedidoColegiado peca) {
		StringBuilder select = new StringBuilder();
		select.append("SELECT i.nome_arquivo as \"nomeArquivo\", ");
		select.append("i.conteudo as \"conteudo\" ");
		select.append("FROM unico.INTEGRA i ");
		select.append("WHERE i.id_integra = :id ");

		Map<String, Object> params = new HashMap<>();
		params.put("id", peca.getIntegra());

		return namedParameterJdbcTemplate.queryForObject(select.toString(), params, getArquivoIntegraRowMapper());
	}

	private static RowMapper<ArquivoIntegra> getArquivoIntegraRowMapper() {

		return new RowMapper<ArquivoIntegra>() {
			@Override
			public ArquivoIntegra mapRow(ResultSet rs, int rowNum) throws SQLException {
				ArquivoIntegra arquivo = new ArquivoIntegra();

				arquivo.setNomeArquivo(rs.getString("nomeArquivo"));
				try {
					arquivo.setBytesConteudo(IOUtils.toByteArray(rs.getBlob("conteudo").getBinaryStream()));
				} catch (IOException e) {
					System.err.println("Erro ao processar conteúdo do arquivo " + arquivo.getNomeArquivo());
				}

				return arquivo;
			}
		};
	}

}
