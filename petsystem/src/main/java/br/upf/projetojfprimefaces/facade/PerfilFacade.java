
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.PerfilEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PerfilFacade extends AbstractFacade<PerfilEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PerfilFacade() {
        super(PerfilEntity.class);
    }
    
}

