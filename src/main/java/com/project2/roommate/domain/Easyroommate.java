package com.project2.roommate.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Easyroommate.
 */
@Entity
@Table(name = "easyroommate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "easyroommate")
public class Easyroommate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "rent_per_a_month")
    private Integer rent_per_a_month;

    @Column(name = "property_type")
    private String property_type;

    @Column(name = "age_range")
    private String age_range;

    @Column(name = "gender")
    private String gender;

    @Column(name = "more_infomation")
    private String more_infomation;

    @Column(name = "search")
    private String search;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Easyroommate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Easyroommate address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Easyroommate phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRent_per_a_month() {
        return rent_per_a_month;
    }

    public Easyroommate rent_per_a_month(Integer rent_per_a_month) {
        this.rent_per_a_month = rent_per_a_month;
        return this;
    }

    public void setRent_per_a_month(Integer rent_per_a_month) {
        this.rent_per_a_month = rent_per_a_month;
    }

    public String getProperty_type() {
        return property_type;
    }

    public Easyroommate property_type(String property_type) {
        this.property_type = property_type;
        return this;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getAge_range() {
        return age_range;
    }

    public Easyroommate age_range(String age_range) {
        this.age_range = age_range;
        return this;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public String getGender() {
        return gender;
    }

    public Easyroommate gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMore_infomation() {
        return more_infomation;
    }

    public Easyroommate more_infomation(String more_infomation) {
        this.more_infomation = more_infomation;
        return this;
    }

    public void setMore_infomation(String more_infomation) {
        this.more_infomation = more_infomation;
    }

    public String getSearch() {
        return search;
    }

    public Easyroommate search(String search) {
        this.search = search;
        return this;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Easyroommate easyroommate = (Easyroommate) o;
        if(easyroommate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, easyroommate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Easyroommate{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", address='" + address + "'" +
            ", phone='" + phone + "'" +
            ", rent_per_a_month='" + rent_per_a_month + "'" +
            ", property_type='" + property_type + "'" +
            ", age_range='" + age_range + "'" +
            ", gender='" + gender + "'" +
            ", more_infomation='" + more_infomation + "'" +
            ", search='" + search + "'" +
            '}';
    }
}
