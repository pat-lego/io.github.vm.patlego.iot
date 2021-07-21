package io.github.vm.patlego.iot.server.dao.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;

import io.github.vm.patlego.iot.server.dao.tables.SensorConfig;
import io.github.vm.patlego.iot.server.dao.tables.config.Config;

@Component(service =  SensorConfigDS.class, immediate = true)
public class SensorConfigDSImpl implements SensorConfigDS {

    Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Reference(target = "(osgi.unit.name=karafdb-hibernate-iot)")
    private JpaTemplate jpaTemplate;

    @Override
    public SensorConfig getConfig(long id) {
        return this.jpaTemplate.txExpr(TransactionType.RequiresNew, emFunction -> {
            return emFunction.find(SensorConfig.class, id);
        });
    }
}
