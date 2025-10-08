
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.LogAcessoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

