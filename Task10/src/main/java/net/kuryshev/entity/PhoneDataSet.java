package net.kuryshev.entity;

import org.hibernate.annotations.MetaValue;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet {

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersDataSet user;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UsersDataSet getUser() {
        return user;
    }

    public void setUser(UsersDataSet user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" + "number='" + number + '\'' + "} " + super.toString();
    }
}
