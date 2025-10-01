
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.LogAcessoEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class LogAcessoFacade extends AbstractFacade<LogAcessoEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogAcessoFacade() {
        super(LogAcessoEntity.class);
    }
    
}

