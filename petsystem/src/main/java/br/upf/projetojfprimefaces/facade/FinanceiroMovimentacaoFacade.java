
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class FinanceiroMovimentacaoFacade extends AbstractFacade<FinanceiroMovimentacaoEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FinanceiroMovimentacaoFacade() {
        super(FinanceiroMovimentacaoEntity.class);
    }
    
}

