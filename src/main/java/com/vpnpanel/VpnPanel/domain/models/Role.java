package com.vpnpanel.VpnPanel.domain.models;

import com.vpnpanel.VpnPanel.domain.enums.RoleType;

public class Role {
    private Long id;

    private RoleType name;

    public Role() {
    }

    public Role(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public RoleType getName() {return name;}
    public void setName(RoleType name) {this.name = name;}
}
