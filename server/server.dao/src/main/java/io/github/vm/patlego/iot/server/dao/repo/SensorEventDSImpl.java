package io.github.vm.patlego.iot.server.dao.repo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.vm.patlego.iot.server.dao.exceptions.InvalidSensorEventException;
import io.github.vm.patlego.iot.server.dao.tables.SensorEvent;

@Component(service = SensorEventDS.class, immediate = true)
public class SensorEventDSImpl implements SensorEventDS {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference(target = "(osgi.unit.name=karafdb-hibernate-iot)")
    private JpaTemplate jpaTemplate;

    @Override
    public SensorEvent createEvent(SensorEvent event) {
        this.jpaTemplate.tx(TransactionType.RequiresNew, entityManager -> {
            entityManager.persist(event);
        });

        return event;
    }

    @Override
    public SensorEvent getEvent(final long id) throws InvalidSensorEventException {
        if (id < 1) {
            logger.error("Cannot retrieve a sensor event with an id that is less then 1");
            throw new InvalidSensorEventException("Cannot retrieve a sensor event with an id that is less then 1");
        }
        return this.jpaTemplate.txExpr(TransactionType.RequiresNew, emFunction -> {
            return emFunction.find(SensorEvent.class, id);
        });
    }

    @Override
    public List<SensorEvent> getEvents() {
        return this.jpaTemplate.txExpr(TransactionType.RequiresNew, emFunction -> {
            CriteriaQuery<SensorEvent> criteriaQuerySensorEvent = emFunction.getCriteriaBuilder()
                    .createQuery(SensorEvent.class);
            Root<SensorEvent> variableRoot = criteriaQuerySensorEvent.from(SensorEvent.class);

            criteriaQuerySensorEvent.select(variableRoot);
            return emFunction.createQuery(criteriaQuerySensorEvent).getResultList();
        });
    }

    @Override
    public int deleteEvent(Timestamp time) {
        return this.jpaTemplate.txExpr(TransactionType.RequiresNew, entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaDelete<SensorEvent> query = cb.createCriteriaDelete(SensorEvent.class).where();
            Root<SensorEvent> root = query.from(SensorEvent.class);
            query.where(cb.lessThanOrEqualTo(root.get("time"), time));

            return entityManager.createQuery(query).executeUpdate();
        });
    }

}
