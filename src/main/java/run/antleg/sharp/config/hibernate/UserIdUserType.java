package run.antleg.sharp.config.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.usertype.UserType;
import run.antleg.sharp.modules.user.model.UserId;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

@Slf4j
public class UserIdUserType implements UserType<UserId>, ResultSetIdentifierConsumer {

    @Override
    public int getSqlType() {
        return Types.BIGINT;
    }

    @Override
    public Class<UserId> returnedClass() {
        return UserId.class;
    }

    @Override
    public boolean equals(UserId x, UserId y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(UserId x) {
        return Objects.hashCode(x);
    }

    @Override
    public UserId nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Long value = rs.getLong(position);
        if (rs.wasNull()) {
            value = null;
        }

        log.debug("Result set column {} value is {}", position, value);
        return value == null ? null : new UserId(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, UserId userId, int index, SharedSessionContractImplementor session) throws SQLException {
        if (userId == null) {
            log.debug("Binding null to parameter {} ", index);
            st.setNull(index, Types.BIGINT);
        } else {
            var value = userId.value();
            log.debug("Binding {} to parameter {} ", value, index);
            st.setLong(index, value);
        }
    }

    @Override
    public UserId deepCopy(UserId value) {
        return new UserId(value.value());
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(UserId value) {
        return deepCopy(value);
    }

    @Override
    public UserId assemble(Serializable cached, Object owner) {
        return deepCopy((UserId) cached);
    }

    @Override
    public UserId replace(UserId detached, UserId managed, Object owner) {
        return deepCopy(detached);
    }

    @Override
    public Object consumeIdentifier(ResultSet rs) {
        try {
            var value = rs.getLong(1); // <4>
            return  rs.wasNull() ? null : new UserId(value);
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not extract ID from ResultSet", ex);
        }
    }
}
