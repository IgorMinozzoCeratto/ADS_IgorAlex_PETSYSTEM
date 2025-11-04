package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FinanceiroMovimentacaoFacade extends AbstractFacade<FinanceiroMovimentacaoEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    public FinanceiroMovimentacaoFacade() {
        super(FinanceiroMovimentacaoEntity.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /** Cria ou atualiza conforme o ID (compatível com AbstractFacade que retorna void). */
    public void save(FinanceiroMovimentacaoEntity entity) {
        if (entity.getId() == null) {
            create(entity);
        } else {
            edit(entity);
        }
    }

    /** Ajuste o filtro conforme o seu campo/tipo de dado. */
    public List<FinanceiroMovimentacaoEntity> listarReceitas() {
        try {
            return em.createQuery(
                "SELECT m FROM FinanceiroMovimentacaoEntity m WHERE UPPER(m.tipo) = 'RECEITA'",
                FinanceiroMovimentacaoEntity.class
            ).getResultList();
        } catch (Exception e) {
            return findAll();
        }
    }
}
