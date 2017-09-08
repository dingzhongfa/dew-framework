package your.group.entity;

import com.tairanchina.csp.dew.core.entity.Column;
import com.tairanchina.csp.dew.core.entity.Entity;
import com.tairanchina.csp.dew.core.entity.PkColumn;

import java.io.Serializable;

@Entity(tableName = "t_order")
public class Order implements Serializable {

    @PkColumn
    private long id;
    @Column(notNull = true)
    private int petId;
    @Column(notNull = true)
    private int customerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
