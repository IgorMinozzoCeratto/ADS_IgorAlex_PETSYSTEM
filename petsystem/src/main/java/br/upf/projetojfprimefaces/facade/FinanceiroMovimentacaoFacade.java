
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

