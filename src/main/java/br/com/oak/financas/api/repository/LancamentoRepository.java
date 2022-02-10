package br.com.oak.financas.api.repository;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.TipoLancamento;
import br.com.oak.financas.api.model.dto.DespesasPorCategoriaDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LancamentoRepository extends CrudRepository<Lancamento, Long> {

  List<Lancamento> findAllByUsuarioGuidAndTipo(String guid, TipoLancamento tipo);

  List<Lancamento> findAllByUsuarioGuidAndTipoAndDescricaoLike(
      String guid, TipoLancamento tipo, String descricao);

  @Query("select l from Lancamento l where l.id = :id and l.usuario.guid = :guid")
  Optional<Lancamento> buscarLancamentoDoUsuarioPorId(
      @Param("guid") String guid, @Param("id") Long id);

  @Query(
      "select l from Lancamento l where l.usuario.id = :usuarioId and l.tipo = :tipo and l.descricao = :descricao and year(l.data) = :ano and month(l.data) = :mes")
  Optional<Lancamento> buscarLancamentoDoUsuarioNoMesmoDia(
      @Param("usuarioId") Long usuarioId,
      @Param("tipo") TipoLancamento tipo,
      @Param("descricao") String descricao,
      @Param("ano") Integer ano,
      @Param("mes") Integer mes);

  @Query(
      "select l from Lancamento l where l.usuario.id = :usuarioId and l.tipo = :tipo and l.descricao = :descricao and year(l.data) = :ano and month(l.data) = :mes and l.id <> :id")
  Optional<Lancamento> buscarLancamentoDoUsuarioNoMesmoDiaParaAlteracao(
      @Param("usuarioId") Long usuarioId,
      @Param("tipo") TipoLancamento tipo,
      @Param("descricao") String descricao,
      @Param("ano") Integer ano,
      @Param("mes") Integer mes,
      @Param("id") Long id);

  @Query(
      "select l from Lancamento l where l.usuario.guid = :guid and l.tipo = :tipo and year(l.data) = :ano and month(l.data) = :mes")
  List<Lancamento> buscarLancamentosDoUsuarioNoAnoMes(
      @Param("guid") String guid,
      @Param("tipo") TipoLancamento tipo,
      @Param("ano") Integer ano,
      @Param("mes") Integer mes);

  @Query(
      "select sum(l.valor) from Lancamento l where l.usuario.guid = :guid and l.tipo = :tipo and year(l.data) = :ano and month(l.data) = :mes")
  BigDecimal obterTotalLancamentosDoUsuarioPorAnoMes(
      @Param("guid") String guid,
      @Param("tipo") TipoLancamento tipo,
      @Param("ano") Integer ano,
      @Param("mes") Integer mes);

  @Query(
      "select new br.com.oak.financas.api.model.dto.DespesasPorCategoriaDto(c.descricao, sum(l.valor)) from Lancamento l join l.categoria c where l.usuario.guid = :guid and l.tipo = :tipo and year(l.data) = :ano and month(l.data) = :mes group by l.categoria")
  List<DespesasPorCategoriaDto> obterTotalLancamentosDoUsuarioPorAnoMesCategoria(
      @Param("guid") String guid,
      @Param("tipo") TipoLancamento tipo,
      @Param("ano") Integer ano,
      @Param("mes") Integer mes);
}
