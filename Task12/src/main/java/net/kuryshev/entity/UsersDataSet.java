package net.kuryshev.entity;

import javax.persistence.*;
import java.util.Set;

@MyEntity(table = "SIMPLE_ENTITY")
@Entity
@Table(name = "user")
public class UsersDataSet extends DataSet {

    @Column(name = "age")
    private int age;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressDataSet address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PhoneDataSet> phones;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public Set<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(Set<PhoneDataSet> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "UsersDataSet{" + "age=" + age + ", name='" + name + '\'' + ", address=" + address + ", phones=" + phones + "} " + super.toString();
    }
}
