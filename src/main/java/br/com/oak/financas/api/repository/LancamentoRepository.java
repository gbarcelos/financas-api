package br.com.oak.financas.api.repository;

import br.com.oak.financas.api.entity.Lancamento;
import br.com.oak.financas.api.model.TipoLancamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LancamentoRepository extends CrudRepository<Lancamento, Long> {

  List<Lancamento> findAllByTipo(TipoLancamento tipo);

  List<Lancamento> findByTipoAndDescricaoLike(TipoLancamento receita, String descricao);

  @Query(
      "select l from Lancamento l where l.tipo = :tipo and l.descricao = :descricao and month(l.data) = :mes")
  Optional<Lancamento> findByTipoAndDescricaoAndMes(
      @Param("tipo") TipoLancamento tipo,
      @Param("descricao") String descricao,
      @Param("mes") Integer mes);

  @Query(
      "select l from Lancamento l where l.tipo = :tipo and l.descricao = :descricao and month(l.data) = :mes and l.id <> :id")
  Optional<Lancamento> findByTipoAndDescricaoAndMesAndDifferentId(
      @Param("tipo") TipoLancamento tipo,
      @Param("descricao") String descricao,
      @Param("mes") Integer mes,
      @Param("id") Long id);
}
