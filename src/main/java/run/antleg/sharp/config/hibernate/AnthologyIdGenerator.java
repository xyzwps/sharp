package run.antleg.sharp.config.hibernate;

import com.github.f4b6a3.ulid.UlidCreator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.factory.spi.StandardGenerator;
import run.antleg.sharp.modules.anthology.model.AnthologyId;

public class AnthologyIdGenerator implements StandardGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return new AnthologyId(UlidCreator.getUlid().toString());
    }
}
