package run.antleg.sharp.config.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.ResultSetIdentifierConsumer;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public abstract class StringRecordUserType<LR extends Serializable> implements UserType<LR>, ResultSetIdentifierConsumer {

    private final Class<LR> lrClass;

    private final Function<String, LR> constructor;

    private final Function<LR, String> getValue;

    protected StringRecordUserType(Class<LR> lrClass, Function<String, LR> constructor, Function<LR, String> getValue) {
        this.lrClass = Objects.requireNonNull(lrClass);
        this.constructor = Objects.requireNonNull(constructor);
        this.getValue = Objects.requireNonNull(getValue);
    }

    @Override
    public int getSqlType() {
        return Types.BIGINT;
    }

    @Override
    public Class<LR> returnedClass() {
        return this.lrClass;
    }

    @Override
    public boolean equals(LR x, LR y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(LR x) {
        return Objects.hashCode(x);
    }

    @Override
    public LR nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        var value = rs.getString(position);
        if (rs.wasNull()) {
            value = null;
        }

        log.debug("Result set column {} value is {}", position, value);
        return value == null ? null : this.constructor.apply(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, LR record, int index, SharedSessionContractImplementor session) throws SQLException {
        if (record == null) {
            log.debug("Binding null to parameter {} ", index);
            st.setNull(index, Types.BIGINT);
        } else {
            var value = this.getValue.apply(record);
            log.debug("Binding {} to parameter {} ", value, index);
            st.setString(index, value);
        }
    }

    @Override
    public LR deepCopy(LR value) {
        return this.constructor.apply(this.getValue.apply(value));
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(LR value) {
        return deepCopy(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public LR assemble(Serializable cached, Object owner) {
        return deepCopy((LR) cached);
    }

    @Override
    public LR replace(LR detached, LR managed, Object owner) {
        return deepCopy(detached);
    }

    @Override
    public Object consumeIdentifier(ResultSet rs) {
        try {
            var value = rs.getString(1); // <4>
            return rs.wasNull() ? null : this.constructor.apply(value);
        } catch (SQLException ex) {
            throw new IllegalStateException("Could not extract ID from ResultSet", ex);
        }
    }
}
